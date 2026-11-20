package eu.happycoders.collections.arraylistvslinkedlist.benchmark;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
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
 * The same two lists as in {@link ScatteredIterateBenchmark}, but summed only after garbage
 * collections have moved their nodes - the situation of a list that lives longer than a few
 * milliseconds:
 *
 * <ul>
 *   <li>{@code young}: the setup allocates short-lived garbage until 20 young collections have run.
 *       They copy the live nodes from region to region and finally into the old generation.
 *   <li>{@code full}: the setup calls {@code System.gc()}, which compacts the whole heap.
 * </ul>
 *
 * <p>If a collection puts the nodes back into list order, the scattered list becomes as fast as the
 * sequential one. The setup prints the number of collections it caused.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class ScatteredAfterGcBenchmark {

  private static final int YOUNG_COLLECTIONS = 20;

  @Param({"1000", "10000", "100000"})
  private int size;

  @Param({"young", "full"})
  private String gc;

  private List<Integer> linkedListSequential;
  private List<Integer> linkedListScattered;

  // Holds the garbage just long enough that the JIT cannot remove the allocations.
  private static volatile Object sink;

  @Setup
  public void setUp() {
    linkedListSequential = ScatteredIterateBenchmark.sequential(size);
    linkedListScattered = ScatteredIterateBenchmark.scattered(size);

    long youngBefore = collectionCount("Young");
    long oldBefore = collectionCount("Old");
    switch (gc) {
      case "young" -> allocateUntilYoungCollections(youngBefore + YOUNG_COLLECTIONS);
      case "full" -> System.gc();
      default -> throw new IllegalArgumentException(gc);
    }
    System.out.printf(
        "%n(setup: %d young, %d full collections)%n",
        collectionCount("Young") - youngBefore, collectionCount("Old") - oldBefore);
  }

  private static void allocateUntilYoungCollections(long target) {
    Object[] window = new Object[1024];
    int i = 0;
    while (collectionCount("Young") < target) {
      window[i++ & 1023] = new byte[256];
    }
    sink = window;
  }

  private static long collectionCount(String namePart) {
    long count = 0;
    for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
      if (bean.getName().contains(namePart)) {
        count += bean.getCollectionCount();
      }
    }
    return count;
  }

  @Benchmark
  public long linkedListSequential() {
    return IterateBenchmark.sum(linkedListSequential);
  }

  @Benchmark
  public long linkedListScattered() {
    return IterateBenchmark.sum(linkedListScattered);
  }
}
