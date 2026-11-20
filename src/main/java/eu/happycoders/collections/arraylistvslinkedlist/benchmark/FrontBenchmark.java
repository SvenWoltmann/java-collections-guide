package eu.happycoders.collections.arraylistvslinkedlist.benchmark;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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
 * Inserts {@code size} elements at the front of an empty collection and then removes them all from
 * the front - the way a stack is used. Compared are an {@link ArrayList} ({@code add(0, e)}, {@code
 * remove(0)}), a {@link LinkedList} and an {@link ArrayDeque} (both {@code addFirst(e)}, {@code
 * removeFirst()}).
 *
 * <p>The {@code ArrayList} shifts all elements on every call, so it takes quadratic time; the sizes
 * end at 100,000.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class FrontBenchmark {

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
  public long arrayList() {
    List<Integer> list = new ArrayList<>();
    for (Integer value : values) {
      list.add(0, value);
    }
    long sum = 0;
    while (!list.isEmpty()) {
      sum += list.remove(0);
    }
    return sum;
  }

  @Benchmark
  public long linkedList() {
    LinkedList<Integer> list = new LinkedList<>();
    for (Integer value : values) {
      list.addFirst(value);
    }
    long sum = 0;
    while (!list.isEmpty()) {
      sum += list.removeFirst();
    }
    return sum;
  }

  @Benchmark
  public long arrayDeque() {
    Deque<Integer> deque = new ArrayDeque<>();
    for (Integer value : values) {
      deque.addFirst(value);
    }
    long sum = 0;
    while (!deque.isEmpty()) {
      sum += deque.removeFirst();
    }
    return sum;
  }
}
