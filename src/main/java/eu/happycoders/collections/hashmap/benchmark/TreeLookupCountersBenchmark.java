package eu.happycoders.collections.hashmap.benchmark;

import eu.happycoders.collections.hashmap.benchmark.TreeificationBenchmark.KeyType;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.AuxCounters;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

/**
 * {@link TreeLookupOrderBenchmark} with the hardware counters of the benchmark thread read around
 * every iteration: cycles, instructions, mispredicted branches and L1 data cache misses, together
 * with the number of {@code get()} calls they belong to. Divide a counter by {@code operations} to
 * get its value per call.
 *
 * <p>The question it answers: {@code get()} on a treeified bucket with {@code Comparable} keys is
 * about twice as fast when the keys are asked for in a repeating order as with a key picked at
 * random on every call, if the map has 1,000 entries. Does the branch predictor learn the repeating
 * sequence?
 *
 * <p>The counters come from the library built from {@code native/kpc-counters.c}, called through
 * the Foreign Function &amp; Memory API. They exist on Apple Silicon only and need root; without
 * root the benchmark runs all the same and reports zeros.
 *
 * <pre>
 * mvn package
 * clang -O2 -dynamiclib -o target/libkpccounters.dylib native/kpc-counters.c
 * sudo "$JAVA_HOME/bin/java" -jar target/benchmarks.jar TreeLookupCountersBenchmark
 * </pre>
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 1,
    jvmArgsAppend = {
      "-Xms4g",
      "-Xmx4g",
      "-XX:+AlwaysPreTouch",
      "-XX:-UsePerfData",
      "--enable-native-access=ALL-UNNAMED"
    })
@State(Scope.Benchmark)
public class TreeLookupCountersBenchmark {

  private static final String LIBRARY = "target/libkpccounters.dylib";

  private static final int COUNTER_COUNT = 4;

  /** The hardware counters of one iteration and the number of calls they were counted over. */
  @State(Scope.Thread)
  @AuxCounters(AuxCounters.Type.EVENTS)
  public static class HardwareCounters {

    public long operations;
    public long cycles;
    public long instructions;
    public long mispredictedBranches;
    public long l1dCacheMisses;

    private MethodHandle read;
    private MemorySegment buffer;
    private long[] before;

    @Setup(Level.Trial)
    public void startCounters() throws Throwable {
      Linker linker = Linker.nativeLinker();
      SymbolLookup library = SymbolLookup.libraryLookup(Path.of(LIBRARY), Arena.global());
      MethodHandle start =
          linker.downcallHandle(
              library.find("kpc_counters_start").orElseThrow(),
              FunctionDescriptor.of(ValueLayout.JAVA_INT));
      if ((int) start.invokeExact() == 0) {
        read =
            linker.downcallHandle(
                library.find("kpc_counters_read").orElseThrow(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        buffer = Arena.global().allocate(ValueLayout.JAVA_LONG, COUNTER_COUNT);
      }
    }

    @Setup(Level.Iteration)
    public void beforeIteration() throws Throwable {
      operations = 0;
      cycles = 0;
      instructions = 0;
      mispredictedBranches = 0;
      l1dCacheMisses = 0;
      before = read();
    }

    @TearDown(Level.Iteration)
    public void afterIteration() throws Throwable {
      long[] after = read();
      cycles = after[0] - before[0];
      instructions = after[1] - before[1];
      mispredictedBranches = after[2] - before[2];
      l1dCacheMisses = after[3] - before[3];
    }

    private long[] read() throws Throwable {
      if (read == null) {
        return new long[COUNTER_COUNT];
      }
      if ((int) read.invokeExact(buffer) != 0) {
        throw new IllegalStateException("kpc_counters_read() failed");
      }
      return buffer.toArray(ValueLayout.JAVA_LONG);
    }
  }

  @Param({"DISTINCT", "COLLIDING_COMPARABLE"})
  private KeyType keyType;

  @Param({"1000", "10000", "20000"})
  private int size;

  private Map<Object, Integer> map;

  /** Keys equal to those in the map, but not the same objects, in shuffled order. */
  private Object[] lookupKeys;

  private int nextLookup;

  private long randomState = 0x9E3779B97F4A7C15L;

  @Setup
  public void setUp() {
    map = new HashMap<>();
    for (int i = 0; i < size; i++) {
      map.put(keyType.createKey(i), i);
    }

    lookupKeys = new Object[size];
    for (int i = 0; i < size; i++) {
      lookupKeys[i] = keyType.createKey(i);
    }
    shuffle(lookupKeys, new Random(4711));
  }

  private static void shuffle(Object[] array, Random random) {
    for (int i = array.length - 1; i > 0; i--) {
      int j = random.nextInt(i + 1);
      Object swap = array[i];
      array[i] = array[j];
      array[j] = swap;
    }
  }

  @Benchmark
  public Integer fixedOrder(HardwareCounters counters) {
    counters.operations++;
    Object key = lookupKeys[nextLookup];
    nextLookup = (nextLookup + 1) % size;
    return map.get(key);
  }

  @Benchmark
  public Integer randomOrder(HardwareCounters counters) {
    counters.operations++;
    // xorshift64: a new pseudo-random index on every call, without a sequence that repeats
    long x = randomState;
    x ^= x << 13;
    x ^= x >>> 7;
    x ^= x << 17;
    randomState = x;
    int index = (int) (((x >>> 32) * size) >>> 32);
    return map.get(lookupKeys[index]);
  }
}
