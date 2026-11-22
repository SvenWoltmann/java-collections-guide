package eu.happycoders.collections.hashmap.benchmark;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;
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
 * Reads an existing key from a {@link HashMap} with {@code get(key)}, for three kinds of keys:
 *
 * <ul>
 *   <li>{@code DISTINCT} - every key has a hash code of its own, so every key has a bucket to
 *       itself;
 *   <li>{@code COLLIDING} - all keys return the same hash code and land in one bucket; the key
 *       class is not {@link Comparable};
 *   <li>{@code COLLIDING_COMPARABLE} - the same, but the key class implements {@link Comparable}.
 * </ul>
 *
 * <p>From a certain number of entries on, the {@code HashMap} stores the one bucket of the
 * colliding keys as a red-black tree. With {@code Comparable} keys it can descend the tree by
 * {@code compareTo()}; without, it has to search both subtrees.
 *
 * <p>The key type is a parameter and not a benchmark method each, so that a forked JVM sees one key
 * class only and the calls to {@code hashCode()} and {@code equals()} inside the {@code HashMap}
 * stay monomorphic.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class TreeificationBenchmark {

  private static final int COLLIDING_HASH_CODE = 42;

  /** A key with a hash code of its own. */
  record DistinctKey(int id) {
    @Override
    public int hashCode() {
      return id;
    }
  }

  /** A key that shares its hash code with all other keys and has no order. */
  record CollidingKey(int id) {
    @Override
    public int hashCode() {
      return COLLIDING_HASH_CODE;
    }
  }

  /** A key that shares its hash code with all other keys and is ordered by its ID. */
  record ComparableCollidingKey(int id) implements Comparable<ComparableCollidingKey> {
    @Override
    public int hashCode() {
      return COLLIDING_HASH_CODE;
    }

    @Override
    public int compareTo(ComparableCollidingKey other) {
      return Integer.compare(id, other.id);
    }
  }

  /** The kind of key the map is filled with. */
  public enum KeyType {
    DISTINCT(DistinctKey::new),
    COLLIDING(CollidingKey::new),
    COLLIDING_COMPARABLE(ComparableCollidingKey::new);

    private final IntFunction<Object> factory;

    KeyType(IntFunction<Object> factory) {
      this.factory = factory;
    }

    Object createKey(int id) {
      return factory.apply(id);
    }
  }

  @Param private KeyType keyType;

  @Param({"10", "100", "1000", "10000"})
  private int size;

  private Map<Object, Integer> map;

  /** Keys equal to those in the map, but not the same objects, in shuffled order. */
  private Object[] lookupKeys;

  private int nextLookup;

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
  public Integer get() {
    Object key = lookupKeys[nextLookup];
    nextLookup = (nextLookup + 1) % size;
    return map.get(key);
  }
}
