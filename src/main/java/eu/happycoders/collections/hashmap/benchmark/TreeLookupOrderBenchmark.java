package eu.happycoders.collections.hashmap.benchmark;

import eu.happycoders.collections.hashmap.benchmark.TreeificationBenchmark.KeyType;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Control for {@link TreeificationBenchmark}: there, {@code get()} on a treeified bucket with
 * {@code Comparable} keys takes 21 ns with 1,000 entries and 54 ns with 10,000 - more than the
 * three additional levels of the tree explain.
 *
 * <p>{@code TreeificationBenchmark} asks for the keys in a shuffled order that repeats after {@code
 * size} calls. A branch predictor can learn a short repeating sequence, so the small sizes might
 * come out too fast. This benchmark reads the same maps in two ways: {@code fixedOrder()} as
 * before, {@code randomOrder()} with a key picked at random on every call. It adds the sizes
 * between and beyond 1,000 and 10,000, and the map with one bucket per key as the baseline for the
 * cost of picking the key.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class TreeLookupOrderBenchmark {

  @Param({"DISTINCT", "COLLIDING_COMPARABLE"})
  private KeyType keyType;

  @Param({"1000", "2000", "5000", "10000", "20000"})
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
  public Integer fixedOrder() {
    Object key = lookupKeys[nextLookup];
    nextLookup = (nextLookup + 1) % size;
    return map.get(key);
  }

  @Benchmark
  public Integer randomOrder() {
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
