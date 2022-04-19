package eu.happycoders.demos.queue;

import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Demo showing the order in which elements are taken from a {@link PriorityBlockingQueue}.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class PriorityBlockingQueueExample {
  private static final long startTime = System.currentTimeMillis();

  public static void main(String[] args) throws InterruptedException {
    BlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
    ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);

    // Start reading from the queue immediately, every 3 seconds
    for (int i = 0; i < 8; i++) {
      int delaySeconds = i * 3;
      pool.schedule(() -> dequeue(queue), delaySeconds, TimeUnit.SECONDS);
    }

    // Start writing to the queue after 3.5 seconds (so there are already 2
    // threads waiting), every 1 seconds (so that the queue fills faster than
    // it's emptied, so that we see some more elements and their order in the queue)
    for (int i = 0; i < 8; i++) {
      int delayMillis = 3500 + i * 1000;
      pool.schedule(() -> enqueue(queue), delayMillis, TimeUnit.MILLISECONDS);
    }

    pool.shutdown();
    pool.awaitTermination(1, TimeUnit.MINUTES);
  }

  private static void enqueue(BlockingQueue<Integer> queue) {
    int e = ThreadLocalRandom.current().nextInt(10, 100);
    log("Calling queue.put(%d) (queue = %s)...", e, queue);
    try {
      queue.put(e);
      log("queue.put(%d) returned (queue = %s)", e, queue);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
  }

  private static void dequeue(BlockingQueue<Integer> queue) {
    log("    Calling queue.take() (queue = %s)...", queue);
    try {
      Integer e = queue.take();
      log("    queue.take() returned %d (queue = %s)", e, queue);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private static void log(String format, Object... args) {
    System.out.printf(
        Locale.US,
        "[%4.1fs] [%-16s] %s%n",
        (System.currentTimeMillis() - startTime) / 1000.0,
        Thread.currentThread().getName(),
        String.format(format, args));
  }
}
