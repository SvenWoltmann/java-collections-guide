package eu.happycoders.demos.queue;

import java.util.Locale;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 * Demo for Java's {@link LinkedTransferQueue} implementation.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class LinkedTransferQueueExample {
  public static void main(String[] args) throws InterruptedException {
    TransferQueue<Integer> queue = new LinkedTransferQueue<>();

    // Start 2 threads calling queue.transfer(),
    startTransferThread(queue, 1);
    startTransferThread(queue, 2);

    // ... then put one element directly,
    enqueueViaPut(queue, 3);

    // ... then start 2 more threads calling queue.transfer().
    startTransferThread(queue, 4);
    startTransferThread(queue, 5);

    // Now take all elements until the queue is empty
    while (!queue.isEmpty()) {
      dequeueViaTake(queue);
    }
  }

  private static void startTransferThread(TransferQueue<Integer> queue, int element)
      throws InterruptedException {
    new Thread(() -> enqueueViaTransfer(queue, element)).start();

    // Wait a bit to let the thread enqueue the element
    Thread.sleep(100);
    log("                            --> queue = " + queue);
  }

  private static void enqueueViaTransfer(TransferQueue<Integer> queue, int element) {
    log("Calling queue.transfer(%d)...", element);
    try {
      queue.transfer(element);
      log("queue.transfer(%d) returned  --> queue = %s", element, queue);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private static void enqueueViaPut(TransferQueue<Integer> queue, int element)
      throws InterruptedException {
    log("Calling queue.put(%d)...", element);
    queue.put(element);
    log("queue.put(%d) returned       --> queue = %s", element, queue);
  }

  private static void dequeueViaTake(TransferQueue<Integer> queue) throws InterruptedException {
    log("    Calling queue.take() (queue = %s)...", queue);
    Integer e = queue.take();
    log("    queue.take() returned %d --> queue = %s", e, queue);

    // Wait a bit to get the log output in a readable order
    Thread.sleep(10);
  }

  private static void log(String format, Object... args) {
    System.out.printf(
        Locale.US, "[%-8s] %s%n", Thread.currentThread().getName(), String.format(format, args));
  }
}
