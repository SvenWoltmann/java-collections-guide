package eu.happycoders.demos.queue;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Demo showing a {@link ThreadPoolExecutor} with a {@link PriorityBlockingQueue} as its work queue:
 * tasks submitted with {@code execute()} run by priority.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class PriorityThreadPoolExample {
  static void main() throws InterruptedException {
    ThreadPoolExecutor pool =
        new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new PriorityBlockingQueue<>());

    pool.execute(new PrioritizedTask(5, "E")); // runs at once on the only thread
    pool.execute(new PrioritizedTask(3, "C")); // these three wait in the queue
    pool.execute(new PrioritizedTask(1, "A"));
    pool.execute(new PrioritizedTask(2, "B"));

    pool.shutdown();
    pool.awaitTermination(1, TimeUnit.MINUTES);
  }
}
