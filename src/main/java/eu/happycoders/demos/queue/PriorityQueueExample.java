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
      int element = ThreadLocalRandom.current().nextInt(100);
      queue.offer(element);
      System.out.printf("queue.offer(%2d)    -->  queue = %s%n", element, queue);
    }

    // Dequeue all elements
    while (!queue.isEmpty()) {
      Integer element = queue.poll();
      System.out.printf("queue.poll() = %2d  -->  queue = %s%n", element, queue);
    }
  }
}
