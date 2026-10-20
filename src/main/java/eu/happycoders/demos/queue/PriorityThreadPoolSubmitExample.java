package eu.happycoders.demos.queue;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Demo showing that tasks submitted with {@code submit()} run by priority, too, once the pool wraps
 * them in a comparable {@link PrioritizedFutureTask} (see {@link PriorityThreadPoolExecutor}).
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class PriorityThreadPoolSubmitExample {
  static void main() throws InterruptedException {
    ThreadPoolExecutor pool = new PriorityThreadPoolExecutor(1);

    pool.submit(new PrioritizedTask(5, "E"));
    pool.submit(new PrioritizedTask(3, "C"));
    pool.submit(new PrioritizedTask(1, "A"));
    pool.submit(new PrioritizedTask(2, "B"));

    pool.shutdown();
    pool.awaitTermination(1, TimeUnit.MINUTES);
  }
}
