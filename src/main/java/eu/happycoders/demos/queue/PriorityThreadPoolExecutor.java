package eu.happycoders.demos.queue;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ThreadPoolExecutor} with a {@link PriorityBlockingQueue} that also runs tasks submitted
 * with {@code submit()} by priority: {@code newTaskFor()} wraps them in a {@link
 * PrioritizedFutureTask} instead of a plain {@link java.util.concurrent.FutureTask}.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {
  PriorityThreadPoolExecutor(int poolSize) {
    super(poolSize, poolSize, 0, TimeUnit.SECONDS, new PriorityBlockingQueue<>());
  }

  @Override
  protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
    return new PrioritizedFutureTask<>((PrioritizedTask) runnable, value);
  }
}
