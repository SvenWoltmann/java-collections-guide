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
 * Asks a {@link HashMap} for a key it contains ({@code existingKey()}) and for one it does not
 * contain ({@code missingKey()}), for the three kinds of keys of {@link TreeificationBenchmark}.
 *
 * <p>The map holds the keys with the even IDs 0, 2, 4, ...; the missing keys are those with the odd
 * IDs in between. For the {@code Comparable} keys a missing key therefore ends somewhere among the
 * leaves of the tree, not always at its right edge.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class MissingKeyBenchmark {

  @Param private KeyType keyType;

  @Param({"10", "100", "1000", "10000"})
  private int size;

  private Map<Object, Integer> map;

  private Object[] existingKeys;
  private Object[] missingKeys;

  private int nextLookup;

  @Setup
  public void setUp() {
    map = new HashMap<>();
    for (int i = 0; i < size; i++) {
      map.put(keyType.createKey(2 * i), i);
    }

    existingKeys = new Object[size];
    missingKeys = new Object[size];
    for (int i = 0; i < size; i++) {
      existingKeys[i] = keyType.createKey(2 * i);
      missingKeys[i] = keyType.createKey(2 * i + 1);
    }
    shuffle(existingKeys, new Random(4711));
    shuffle(missingKeys, new Random(4712));
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
  public Integer existingKey() {
    Object key = existingKeys[nextLookup];
    nextLookup = (nextLookup + 1) % size;
    return map.get(key);
  }

  @Benchmark
  public Integer missingKey() {
    Object key = missingKeys[nextLookup];
    nextLookup = (nextLookup + 1) % size;
    return map.get(key);
  }
}
