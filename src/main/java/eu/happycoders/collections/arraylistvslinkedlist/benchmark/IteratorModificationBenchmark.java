package eu.happycoders.collections.arraylistvslinkedlist.benchmark;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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
 * Changes a list while iterating over it - the case in which the {@link LinkedList} inserts and
 * removes in constant time, because the iterator already stands at the position:
 *
 * <ul>
 *   <li>{@code *Remove*}: removes every element with an odd value, i.e., every second one - with
 *       {@code Iterator.remove()} and with {@code removeIf()}.
 *   <li>{@code *Insert}: inserts a new element after every element with {@code ListIterator.add()}.
 * </ul>
 *
 * <p>Every method first copies a prepared list of {@code size} elements, because it changes the
 * list. {@code *Copy} measures that copy alone, so it can be subtracted. The {@link ArrayList}
 * shifts the rest of its array on every {@code Iterator.remove()} and {@code ListIterator.add()},
 * so these take quadratic time; the sizes end at 100,000.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class IteratorModificationBenchmark {

  private static final Integer INSERTED = 1_000_000_000;

  @Param({"1000", "10000", "100000"})
  private int size;

  private List<Integer> source;

  @Setup
  public void setUp() {
    source = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      source.add(i);
    }
  }

  @Benchmark
  public List<Integer> arrayListCopy() {
    return new ArrayList<>(source);
  }

  @Benchmark
  public List<Integer> linkedListCopy() {
    return new LinkedList<>(source);
  }

  @Benchmark
  public List<Integer> arrayListIteratorRemove() {
    return removeOddWithIterator(new ArrayList<>(source));
  }

  @Benchmark
  public List<Integer> linkedListIteratorRemove() {
    return removeOddWithIterator(new LinkedList<>(source));
  }

  @Benchmark
  public List<Integer> arrayListRemoveIf() {
    List<Integer> list = new ArrayList<>(source);
    list.removeIf(value -> value % 2 != 0);
    return list;
  }

  @Benchmark
  public List<Integer> linkedListRemoveIf() {
    List<Integer> list = new LinkedList<>(source);
    list.removeIf(value -> value % 2 != 0);
    return list;
  }

  @Benchmark
  public List<Integer> arrayListInsert() {
    return insertAfterEach(new ArrayList<>(source));
  }

  @Benchmark
  public List<Integer> linkedListInsert() {
    return insertAfterEach(new LinkedList<>(source));
  }

  private static List<Integer> removeOddWithIterator(List<Integer> list) {
    Iterator<Integer> iterator = list.iterator();
    while (iterator.hasNext()) {
      if (iterator.next() % 2 != 0) {
        iterator.remove();
      }
    }
    return list;
  }

  private static List<Integer> insertAfterEach(List<Integer> list) {
    ListIterator<Integer> iterator = list.listIterator();
    while (iterator.hasNext()) {
      iterator.next();
      iterator.add(INSERTED);
    }
    return list;
  }
}
