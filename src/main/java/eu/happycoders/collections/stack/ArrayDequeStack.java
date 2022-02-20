package eu.happycoders.collections.stack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A last-in-first-out (LIFO) stack of objects implemented as an adapter around an {@link
 * ArrayDeque}.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this stack
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class ArrayDequeStack<E> implements Stack<E> {

  private final Deque<E> deque = new ArrayDeque<>();

  @Override
  public void push(E item) {
    deque.addFirst(item);
  }

  @Override
  public E pop() {
    return deque.removeFirst();
  }

  @Override
  public E peek() {
    return deque.getFirst();
  }
}
