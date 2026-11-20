package eu.happycoders.collections.arraylistvslinkedlist.benchmark;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
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
 * A queue in steady state: it holds {@code size} elements, and every operation appends one element
 * with {@code addLast(e)} and takes one from the front with {@code removeFirst()}. Compared are a
 * {@link LinkedList}, which creates a node for every element it appends, and an {@link ArrayDeque},
 * which reuses the slots of its ring buffer once it has grown to {@code size}.
 *
 * <p>{@link FrontBenchmark} fills a collection once and then empties it; this benchmark measures
 * how a queue is used for a long time.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class QueueBenchmark {

  private static final int VALUE_COUNT = 4096;
  private static final int VALUE_MASK = VALUE_COUNT - 1;

  @Param({"100", "10000", "1000000"})
  private int size;

  private Integer[] values;
  private int cursor;

  private Deque<Integer> linkedList;
  private Deque<Integer> arrayDeque;

  @Setup
  public void setUp() {
    // Outside the cache of Integer.valueOf(), so that every value is an object of its own.
    values = new Integer[VALUE_COUNT];
    for (int i = 0; i < VALUE_COUNT; i++) {
      values[i] = 1_000 + i;
    }

    linkedList = new LinkedList<>();
    arrayDeque = new ArrayDeque<>();
    for (int i = 0; i < size; i++) {
      linkedList.addLast(values[i & VALUE_MASK]);
      arrayDeque.addLast(values[i & VALUE_MASK]);
    }
  }

  private Integer nextValue() {
    return values[cursor++ & VALUE_MASK];
  }

  @Benchmark
  public Integer linkedList() {
    linkedList.addLast(nextValue());
    return linkedList.removeFirst();
  }

  @Benchmark
  public Integer arrayDeque() {
    arrayDeque.addLast(nextValue());
    return arrayDeque.removeFirst();
  }
}
