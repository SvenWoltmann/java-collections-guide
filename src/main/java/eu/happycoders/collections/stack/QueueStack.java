package eu.happycoders.collections.stack;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A last-in-first-out (LIFO) stack of objects implemented using two first-in-first-out (FIFO)
 * queues.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this stack
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class QueueStack<E> implements Stack<E> {

  private Queue<E> queue = new ArrayDeque<>();

  @Override
  public void push(E element) {
    Queue<E> newQueue = new ArrayDeque<>();
    newQueue.add(element);

    while (!queue.isEmpty()) {
      newQueue.add(queue.remove());
    }

    queue = newQueue;
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
