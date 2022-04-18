package eu.happycoders.demos.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Demo showing how elements are enqueued into and dequeued from a {@link DelayQueue}.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class DelayQueueExample {
  public static void main(String[] args) {
    BlockingQueue<DelayedElement<Integer>> queue = new DelayQueue<>();
    ThreadLocalRandom random = ThreadLocalRandom.current();
    long startTime = System.currentTimeMillis();

    // Enqueue random numbers with random initial delays
    for (int i = 0; i < 7; i++) {
      int randomNumber = random.nextInt(10, 100);
      int initialDelayMillis = random.nextInt(100, 1000);
      DelayedElement<Integer> e = new DelayedElement<>(randomNumber, initialDelayMillis);
      queue.offer(e);
      System.out.printf(
          "[%3dms] queue.offer(%s)   --> queue = %s%n",
          System.currentTimeMillis() - startTime, e, queue);
    }

    // Dequeue all elements
    while (!queue.isEmpty()) {
      try {
        DelayedElement<Integer> e = queue.take();
        System.out.printf(
            "[%3dms] queue.poll() = %s --> queue = %s%n",
            System.currentTimeMillis() - startTime, e, queue);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
