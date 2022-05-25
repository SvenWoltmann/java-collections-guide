package eu.happycoders.demos.deque;

import eu.happycoders.collections.deque.BoundedArrayDeque;
import eu.happycoders.collections.deque.Deque;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Demo for the HappyCoders {@link Deque} interface and its implementations.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class DequeDemo {
  public static void main(String[] args) {
    runDemo(new BoundedArrayDeque<>(10));
  }

  private static void runDemo(Deque<Integer> deque) {
    System.out.println("---------- " + deque.getClass().getSimpleName() + " ----------");
    System.out.println("deque.isEmpty() = " + deque.isEmpty());

    for (int i = 1; i <= 5; i++) {
      if (ThreadLocalRandom.current().nextBoolean()) {
        deque.enqueueFront(i);
        System.out.println("deque.enqueueFront(" + i + ")");
      } else {
        deque.enqueueBack(i);
        System.out.println("deque.enqueueBack(" + i + ")");
      }
    }

    System.out.println();

    System.out.println("deque.isEmpty() = " + deque.isEmpty());
    System.out.println("deque.peekFront() = " + deque.peekFront());
    System.out.println("deque.peekBack() = " + deque.peekBack());

    System.out.println();

    while (!deque.isEmpty()) {
      if (ThreadLocalRandom.current().nextBoolean()) {
        System.out.println("deque.dequeueFront() = " + deque.dequeueFront());
      } else {
        System.out.println("deque.dequeueBack() = " + deque.dequeueBack());
      }
    }

    System.out.println();

    System.out.println("deque.isEmpty() = " + deque.isEmpty());
    try {
      System.out.println("deque.dequeueFront() = " + deque.dequeueFront());
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }
}
