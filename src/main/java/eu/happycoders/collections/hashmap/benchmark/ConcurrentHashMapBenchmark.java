package eu.happycoders.collections.hashmap.benchmark;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
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
 * Compares a {@link HashMap} with a {@link ConcurrentHashMap} that is used by a single thread:
 *
 * <ul>
 *   <li>{@code get()} - only reading: one existing key per invocation;
 *   <li>{@code replace()} - only writing: {@code put()} with an existing key, one per invocation;
 *   <li>{@code fill()} - only writing: {@code put()} with {@code size} new keys into an empty map
 *       of default capacity, including its growth. The result is the time for the whole map; divide
 *       it by {@code size};
 *   <li>{@code mixed()} - nine {@code get()} calls followed by one {@code put()} with an existing
 *       key. One invocation is one of these ten calls.
 * </ul>
 *
 * <p>The map type is a parameter and not a benchmark method each, so that a forked JVM sees one map
 * class only and the calls on {@link Map} stay monomorphic.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class ConcurrentHashMapBenchmark {

  private static final int CALLS_PER_WRITE = 10;

  /** The map class under test. */
  public enum MapType {
    HASH_MAP(HashMap::new),
    CONCURRENT_HASH_MAP(ConcurrentHashMap::new);

    private final Supplier<Map<Integer, Integer>> factory;

    MapType(Supplier<Map<Integer, Integer>> factory) {
      this.factory = factory;
    }

    Map<Integer, Integer> createMap() {
      return factory.get();
    }
  }

  @Param private MapType mapType;

  @Param({"100", "10000", "1000000"})
  private int size;

  private Map<Integer, Integer> map;

  /** Keys for {@code fill()}, in shuffled order. */
  private Integer[] insertKeys;

  /** Keys equal to those in the map, but not the same objects, in a different shuffled order. */
  private Integer[] lookupKeys;

  private int nextLookup;
  private int nextCall;

  @Setup
  public void setUp() {
    insertKeys = createShuffledKeys(new Random(4711));
    lookupKeys = createShuffledKeys(new Random(4712));

    map = mapType.createMap();
    for (Integer key : insertKeys) {
      map.put(key, key);
    }
  }

  private Integer[] createShuffledKeys(Random random) {
    Integer[] keys = new Integer[size];
    for (int i = 0; i < size; i++) {
      keys[i] = i;
    }
    for (int i = size - 1; i > 0; i--) {
      int j = random.nextInt(i + 1);
      Integer swap = keys[i];
      keys[i] = keys[j];
      keys[j] = swap;
    }
    return keys;
  }

  private Integer nextLookupKey() {
    Integer key = lookupKeys[nextLookup];
    nextLookup++;
    if (nextLookup == size) {
      nextLookup = 0;
    }
    return key;
  }

  @Benchmark
  public Integer get() {
    return map.get(nextLookupKey());
  }

  @Benchmark
  public Integer replace() {
    Integer key = nextLookupKey();
    return map.put(key, key);
  }

  @Benchmark
  public Map<Integer, Integer> fill() {
    Map<Integer, Integer> newMap = mapType.createMap();
    for (Integer key : insertKeys) {
      newMap.put(key, key);
    }
    return newMap;
  }

  @Benchmark
  public Integer mixed() {
    Integer key = nextLookupKey();
    nextCall++;
    if (nextCall == CALLS_PER_WRITE) {
      nextCall = 0;
      return map.put(key, key);
    }
    return map.get(key);
  }
}
