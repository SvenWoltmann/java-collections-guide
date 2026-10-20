package eu.happycoders.demos.queue;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Demo showing a {@link PriorityBlockingQueue} that orders its elements by a {@link Comparator}
 * instead of their natural ordering.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class PriorityBlockingQueueComparatorExample {
  static void main() throws InterruptedException {
    BlockingQueue<Integer> queue = new PriorityBlockingQueue<>(11, Comparator.reverseOrder());
    queue.put(31);
    queue.put(87);
    queue.put(15);
    IO.println(queue.take() + " " + queue.take() + " " + queue.take());
  }
}
