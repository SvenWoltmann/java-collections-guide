package eu.happycoders.demos.deque;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Demo for Java's {@link Deque} interface.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class JavaDequeDemo {
  public static void main(String[] args) {
    Deque<Integer> deque = new ArrayDeque<>();

    for (int i = 20; i <= 22; i++) {
      deque.offerFirst(i);
      System.out.println("deque.offerFirst(" + i + ") --> deque = " + deque);
    }

    for (int i = 23; i <= 25; i++) {
      deque.offerLast(i);
      System.out.println("deque.offerLast(" + i + ")  --> deque = " + deque);
    }

    for (int i = 26; i <= 27; i++) {
      deque.offerFirst(i);
      System.out.println("deque.offerFirst(" + i + ") --> deque = " + deque);
    }

    System.out.println();
    System.out.println("deque.isEmpty()   = " + deque.isEmpty());
    System.out.println("deque.peekFirst() = " + deque.peekFirst());
    System.out.println("deque.peekLast()  = " + deque.peekLast());
    System.out.println();

    while (!deque.isEmpty()) {
      System.out.println("deque.pollFirst() = " + deque.pollFirst() + " --> deque = " + deque);
      System.out.println("deque.pollLast()  = " + deque.pollLast() + " --> deque = " + deque);
    }

    System.out.println();
    System.out.println("deque.isEmpty()   = " + deque.isEmpty());
    System.out.println("deque.peekFirst() = " + deque.peekFirst());
    System.out.println("deque.peekLast()  = " + deque.peekLast());
  }
}
