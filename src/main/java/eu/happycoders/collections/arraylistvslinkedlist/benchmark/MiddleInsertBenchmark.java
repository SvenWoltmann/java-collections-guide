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
 * Inserts {@code size} elements into the middle of an initially empty list with {@code
 * add(list.size() / 2, e)} - into an {@link ArrayList}, which shifts the back half of its array on
 * every call, and into a {@link LinkedList}, which walks from one end to the middle on every call.
 * Both take quadratic time; the sizes end at 100,000.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class MiddleInsertBenchmark {

  @Param({"1000", "10000", "100000"})
  private int size;

  private Integer[] values;

  @Setup
  public void setUp() {
    values = new Integer[size];
    for (int i = 0; i < size; i++) {
      values[i] = i;
    }
  }

  @Benchmark
  public List<Integer> arrayList() {
    return insertInMiddle(new ArrayList<>());
  }

  @Benchmark
  public List<Integer> linkedList() {
    return insertInMiddle(new LinkedList<>());
  }

  private List<Integer> insertInMiddle(List<Integer> list) {
    for (Integer value : values) {
      list.add(list.size() / 2, value);
    }
    return list;
  }
}
