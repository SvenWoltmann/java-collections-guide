package eu.happycoders.demos.queue;

import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Demo showing multiple threads enqueueing and dequeueing elements in and from a {@link
 * SynchronousQueue}.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class SynchronousQueueExample {
  private static final boolean FAIR = false;

  public static void main(String[] args) throws InterruptedException {
    BlockingQueue<Integer> queue = new SynchronousQueue<>(FAIR);

    // Start 3 producing threads
    for (int i = 0; i < 3; i++) {
      final int finalI = i;
      new Thread(() -> enqueue(queue, finalI)).start();
      Thread.sleep(250);
    }

    // Start 6 consuming threads
    for (int i = 0; i < 6; i++) {
      new Thread(() -> dequeue(queue)).start();
      Thread.sleep(250);
    }

    // Start 3 more producing threads
    for (int i = 3; i < 6; i++) {
      final int finalI = i;
      new Thread(() -> enqueue(queue, finalI)).start();
      Thread.sleep(250);
    }
  }

  private static void enqueue(BlockingQueue<Integer> queue, int finalI) {
    log("Calling queue.put(%d) (queue = %s)...", finalI, queue);
    try {
      queue.put(finalI);
      log("queue.put(%d) returned (queue = %s)", finalI, queue);
    } catch (InterruptedException e) {
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
        Locale.US, "[%-9s] %s%n", Thread.currentThread().getName(), String.format(format, args));
  }
}
