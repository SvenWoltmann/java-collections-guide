package eu.happycoders.demos.queue;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Demo showing the order in which elements are taken from a {@link PriorityQueue}.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class PriorityQueueExample {
  public static void main(String[] args) {
    Queue<Integer> queue = new PriorityQueue<>();

    // Enqueue random numbers
    for (int i = 0; i < 8; i++) {
      Integer e = ThreadLocalRandom.current().nextInt(100);
      queue.offer(e);
      System.out.printf("queue.offer(%2d)    -->  queue = %s%n", e, queue);
    }

    // Dequeue all elements
    while (!queue.isEmpty()) {
      System.out.printf("queue.poll() = %2d  -->  queue = %s%n", queue.poll(), queue);
    }
  }
}
