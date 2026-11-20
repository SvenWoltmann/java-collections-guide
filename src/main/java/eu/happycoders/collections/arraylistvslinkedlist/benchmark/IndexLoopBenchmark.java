package eu.happycoders.collections.arraylistvslinkedlist.benchmark;

import java.util.ArrayList;
import java.util.LinkedList;
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
 * Sums all elements of an {@link ArrayList} and of a {@link LinkedList} with an index loop: {@code
 * for (int i = 0; i < list.size(); i++) sum += list.get(i)}. For the {@code LinkedList}, every
 * {@code get(i)} walks from the nearer end of the list to index {@code i}, so the loop takes
 * quadratic time; the sizes end at 100,000. {@link IterateBenchmark} sums the same lists with a
 * for-each loop.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class IndexLoopBenchmark {

  @Param({"1000", "10000", "100000"})
  private int size;

  private List<Integer> arrayList;
  private List<Integer> linkedList;

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
  }

  @Benchmark
  public long arrayList() {
    return sumByIndex(arrayList);
  }

  @Benchmark
  public long linkedList() {
    return sumByIndex(linkedList);
  }

  private static long sumByIndex(List<Integer> list) {
    long sum = 0;
    for (int i = 0; i < list.size(); i++) {
      sum += list.get(i);
    }
    return sum;
  }
}
