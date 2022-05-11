package eu.happycoders.collections.queue;

import java.util.NoSuchElementException;

/**
 * A first-in-first-out (FIFO) queue of objects.
 *
 * <p>This interface provides only the basic queue methods &ndash; in contrast to the <code>
 * java.util.Queue</code> interface that extends <code>Collection</code> and <code>Iterable</code>.
 *
 * @param <E> the type of elements held in this queue
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public interface Queue<E> {

  /**
   * Inserts an element at the tail of the queue.
   *
   * @param element the element to insert into the queue
   */
  void enqueue(E element);

  /**
   * Dequeues an element from the head of the queue.
   *
   * @return the element from the head of the queue
   * @throws NoSuchElementException if the queue is empty
   */
  E dequeue();

  /**
   * Returns the element from the head of the queue without removing it.
   *
   * @return the element from the head of the queue
   * @throws NoSuchElementException if the queue is empty
   */
  E peek();

  /**
   * Returns {@code true} if this queue contains no elements.
   *
   * @return {@code true} if this queue contains no elements
   */
  boolean isEmpty();
}
