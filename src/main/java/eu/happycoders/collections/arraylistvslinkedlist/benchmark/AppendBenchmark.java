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
 * Appends {@code size} elements to an empty list with {@code add(e)}: to an {@link ArrayList}
 * created with the default capacity, to one created with a capacity of {@code size}, and to a
 * {@link LinkedList}.
 *
 * <p>The {@code Integer} objects are created in the setup, so that every variant adds the same
 * references and the measurement does not contain the boxing.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class AppendBenchmark {

  @Param({"1000", "100000", "10000000"})
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
    List<Integer> list = new ArrayList<>();
    for (Integer value : values) {
      list.add(value);
    }
    return list;
  }

  @Benchmark
  public List<Integer> arrayListPresized() {
    List<Integer> list = new ArrayList<>(size);
    for (Integer value : values) {
      list.add(value);
    }
    return list;
  }

  @Benchmark
  public List<Integer> linkedList() {
    List<Integer> list = new LinkedList<>();
    for (Integer value : values) {
      list.add(value);
    }
    return list;
  }
}
