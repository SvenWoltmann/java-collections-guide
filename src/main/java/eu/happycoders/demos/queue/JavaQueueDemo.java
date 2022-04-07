package eu.happycoders.demos.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Demo for Java's {@link Queue} interface.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class JavaQueueDemo {
  public static void main(String[] args) {
    Queue<Integer> queue = new ConcurrentLinkedQueue<>();

    for (int i = 1; i <= 5; i++) {
      queue.offer(i);
      System.out.println("queue.offer(" + i + ") --> queue = " + queue);
    }

    System.out.println();

    System.out.println("queue.peek() = " + queue.peek());

    System.out.println();

    while (!queue.isEmpty()) {
      System.out.println("queue.poll() = " + queue.poll() + " --> queue = " + queue);
    }

    System.out.println();

    System.out.println("queue.poll() = " + queue.poll());
    System.out.println("queue.peek() = " + queue.peek());
  }
}
