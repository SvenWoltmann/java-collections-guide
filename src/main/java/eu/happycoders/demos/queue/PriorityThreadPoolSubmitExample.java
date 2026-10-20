package eu.happycoders.demos.queue;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Demo showing why a priority thread pool needs {@code submit()}: the returned {@link Future}
 * cancels a task that is still waiting in the queue. The other tasks still run by priority, because
 * {@link PriorityThreadPoolExecutor} wraps them in a comparable {@link PrioritizedFutureTask}.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class PriorityThreadPoolSubmitExample {
  static void main() throws InterruptedException {
    ThreadPoolExecutor pool = new PriorityThreadPoolExecutor(1);

    pool.submit(new PrioritizedTask(5, "E"));
    pool.submit(new PrioritizedTask(3, "C"));
    pool.submit(new PrioritizedTask(1, "A"));
    Future<?> taskB = pool.submit(new PrioritizedTask(2, "B"));

    taskB.cancel(false); // B is still waiting in the queue

    pool.shutdown();
    pool.awaitTermination(1, TimeUnit.MINUTES);
  }
}
