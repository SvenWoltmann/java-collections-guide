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
 * Sums all elements of an {@link ArrayList} and of a {@link LinkedList} with a for-each loop. Both
 * lists are filled with {@code add(e)} in the setup, so the nodes of the {@code LinkedList} lie in
 * the heap in the order of the list - the most favorable case for it. {@link
 * ScatteredIterateBenchmark} measures the other case.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class IterateBenchmark {

  @Param({"1000", "100000", "10000000"})
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
    return sum(arrayList);
  }

  @Benchmark
  public long linkedList() {
    return sum(linkedList);
  }

  static long sum(List<Integer> list) {
    long sum = 0;
    for (int value : list) {
      sum += value;
    }
    return sum;
  }
}
