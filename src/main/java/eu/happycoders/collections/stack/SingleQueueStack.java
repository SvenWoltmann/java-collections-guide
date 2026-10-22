package eu.happycoders.collections.stack;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A last-in-first-out (LIFO) stack of objects implemented using a single first-in-first-out (FIFO)
 * queue: after each insertion, the other elements are rotated once through the queue so that the
 * new element is at its head.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this stack
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class SingleQueueStack<E> implements Stack<E> {

  private final Queue<E> queue = new ArrayDeque<>();

  @Override
  public void push(E element) {
    queue.add(element);
    for (int i = 1; i < queue.size(); i++) {
      queue.add(queue.remove());
    }
  }

  @Override
  public E pop() {
    return queue.remove();
  }

  @Override
  public E peek() {
    return queue.element();
  }

  @Override
  public boolean isEmpty() {
    return queue.isEmpty();
  }
}
