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
 * Sums all elements of a {@link LinkedList} whose nodes lie in the heap in the order of the list
 * ({@code linkedListSequential}, filled with {@code add(e)}) and of one whose nodes lie in random
 * order ({@code linkedListScattered}, filled with {@code add(index, e)} at random positions), with
 * an {@link ArrayList} as reference.
 *
 * <p>The scattered list stands for a list that has grown by insertions in the middle: neighbors in
 * the list were created at different times and are therefore not neighbors in memory. Building it
 * takes quadratic time, so the sizes end at 100,000.
 *
 * <p>The setup does not trigger a garbage collection, and the measured loops allocate nothing - the
 * nodes stay where they were created. What a garbage collection does to their order is a question
 * of its own.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(
    value = 2,
    jvmArgsAppend = {"-Xms4g", "-Xmx4g", "-XX:+AlwaysPreTouch"})
@State(Scope.Benchmark)
public class ScatteredIterateBenchmark {

  @Param({"1000", "10000", "100000"})
  private int size;

  private List<Integer> arrayList;
  private List<Integer> linkedListSequential;
  private List<Integer> linkedListScattered;

  @Setup
  public void setUp() {
    arrayList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      arrayList.add(i);
    }

    linkedListSequential = new LinkedList<>();
    for (int i = 0; i < size; i++) {
      linkedListSequential.add(i);
    }

    Random random = new Random(42);
    linkedListScattered = new LinkedList<>();
    for (int i = 0; i < size; i++) {
      linkedListScattered.add(random.nextInt(i + 1), i);
    }
  }

  @Benchmark
  public long arrayList() {
    return IterateBenchmark.sum(arrayList);
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
