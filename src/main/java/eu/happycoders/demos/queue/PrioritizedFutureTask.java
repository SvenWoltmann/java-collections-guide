package eu.happycoders.demos.queue;

import java.util.concurrent.FutureTask;

/**
 * A {@link FutureTask} that a {@link java.util.concurrent.PriorityBlockingQueue} can order: it
 * compares the priorities of the {@link PrioritizedTask}s it wraps.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class PrioritizedFutureTask<T> extends FutureTask<T>
    implements Comparable<PrioritizedFutureTask<?>> {
  private final PrioritizedTask task;

  PrioritizedFutureTask(PrioritizedTask task, T value) {
    super(task, value);
    this.task = task;
  }

  @Override
  public int compareTo(PrioritizedFutureTask<?> other) {
    return task.compareTo(other.task);
  }
}
