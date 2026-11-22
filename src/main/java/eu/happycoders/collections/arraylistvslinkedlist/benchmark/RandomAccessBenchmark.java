package eu.happycoders.collections.arraylistvslinkedlist.benchmark;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
 * Reads one element at a random index with {@code get(index)} - from an {@link ArrayList} and from
 * a {@link LinkedList}. The indices come from a precomputed table of random numbers, so the random
 * number generator is not part of the measurement.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class RandomAccessBenchmark {

  private static final int INDEX_COUNT = 10_000;
  private static final int NEAR_RANGE = 1_000;

  @Param({"1000", "100000", "1000000"})
  private int size;

  private List<Integer> arrayList;
  private List<Integer> linkedList;
  private int[] indices;
  private int[] nearIndices;
  private int cursor;

  @Setup
  public void setUp() {
    arrayList = new ArrayList<>();
    linkedList = new LinkedList<>();
    for (int i = 0; i < size; i++) {
      arrayList.add(i);
    }
    for (int i = 0; i < size; i++) {
      linkedList.add(i);
    }

    Random random = new Random(42);
    indices = new int[INDEX_COUNT];
    for (int i = 0; i < INDEX_COUNT; i++) {
      indices[i] = random.nextInt(size);
    }
    // the same number of random indices, but only from the first NEAR_RANGE positions
    nearIndices = new int[INDEX_COUNT];
    for (int i = 0; i < INDEX_COUNT; i++) {
      nearIndices[i] = random.nextInt(Math.min(size, NEAR_RANGE));
    }
  }

  @Benchmark
  public Integer arrayList() {
    return arrayList.get(nextIndex());
  }

  /**
   * Control for {@link #arrayList()}: random indices only from the first 1,000 positions. If the
   * time rises with the size in {@code arrayList()} but not here, the rise comes from the memory
   * region the accesses touch, not from the size of the list.
   */
  @Benchmark
  public Integer arrayListNear() {
    int index = nearIndices[cursor];
    cursor = cursor + 1 < INDEX_COUNT ? cursor + 1 : 0;
    return arrayList.get(index);
  }

  @Benchmark
  public Integer linkedList() {
    return linkedList.get(nextIndex());
  }

  private int nextIndex() {
    int index = indices[cursor];
    cursor = cursor + 1 < INDEX_COUNT ? cursor + 1 : 0;
    return index;
  }
}
