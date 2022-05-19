package eu.happycoders.demos.deque;

import java.util.Locale;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Demo for Java's {@link LinkedBlockingDeque} implementation.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class LinkedBlockingDequeExample {
  private static final long startTime = System.currentTimeMillis();

  public static void main(String[] args) throws InterruptedException {
    BlockingDeque<Integer> deque = new LinkedBlockingDeque<>(3);
    ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);

    // Start reading from the deque immediately, every 3 seconds
    for (int i = 0; i < 10; i++) {
      int delaySeconds = i * 3;
      pool.schedule(() -> dequeue(deque), delaySeconds, TimeUnit.SECONDS);
    }

    // Start writing to the deque after 3.5 seconds (so there are already 2
    // threads waiting), every 1 seconds (so that the deque fills faster than
    // it's emptied, so that we see a full deque soon)
    for (int i = 0; i < 10; i++) {
      int element = i;
      int delayMillis = 3500 + i * 1000;
      pool.schedule(() -> enqueue(deque, element), delayMillis, TimeUnit.MILLISECONDS);
    }

    pool.shutdown();
    pool.awaitTermination(1, TimeUnit.MINUTES);
  }

  private static void enqueue(BlockingDeque<Integer> deque, int i) {
    if (ThreadLocalRandom.current().nextBoolean()) {
      enqueueAtFront(deque, i);
    } else {
      enqueueAtBack(deque, i);
    }
  }

  private static void enqueueAtFront(BlockingDeque<Integer> deque, int element) {
    log("Calling deque.putFirst(%d) (deque = %s)...", element, deque);
    try {
      deque.putFirst(element);
      log("deque.putFirst(%d) returned (deque = %s)", element, deque);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private static void enqueueAtBack(BlockingDeque<Integer> deque, int element) {
    log("Calling deque.putLast(%d) (deque = %s)...", element, deque);
    try {
      deque.putLast(element);
      log("deque.putLast(%d) returned (deque = %s)", element, deque);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private static void dequeue(BlockingDeque<Integer> deque) {
    if (ThreadLocalRandom.current().nextBoolean()) {
      dequeueAtFront(deque);
    } else {
      dequeueAtBack(deque);
    }
  }

  private static void dequeueAtFront(BlockingDeque<Integer> deque) {
    log("    Calling deque.takeFirst() (deque = %s)...", deque);
    try {
      Integer element = deque.takeFirst();
      log("    deque.takeFirst() returned %d (deque = %s)", element, deque);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private static void dequeueAtBack(BlockingDeque<Integer> deque) {
    log("    Calling deque.takeLast() (deque = %s)...", deque);
    try {
      Integer element = deque.takeLast();
      log("    deque.takeLast() returned %d (deque = %s)", element, deque);
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
