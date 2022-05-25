package eu.happycoders.collections.deque;

import java.util.NoSuchElementException;

/**
 * A deque (double-ended queue) of objects.
 *
 * <p>This interface provides only the basic deque methods &ndash; in contrast to the <code>
 * java.util.Deque</code> interface that extends <code>Queue</code>, <code>Collection</code> and
 * <code>Iterable</code>.
 *
 * @param <E> the type of elements held in this deque
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public interface Deque<E> {

  /**
   * Inserts an element at the front/head of the deque.
   *
   * @param element the element to insert into the deque
   */
  void enqueueFront(E element);

  /**
   * Inserts an element at the back/tail of the deque.
   *
   * @param element the element to insert into the deque
   */
  void enqueueBack(E element);

  /**
   * Dequeues an element at the front/head of the deque.
   *
   * @return the element from the front/head of the deque
   * @throws NoSuchElementException if the deque is empty
   */
  E dequeueFront();

  /**
   * Dequeues an element at the back/tail of the deque.
   *
   * @return the element from the back/tail of the deque
   * @throws NoSuchElementException if the deque is empty
   */
  E dequeueBack();

  /**
   * Returns the element from the front/head of the deque without removing it.
   *
   * @return the element from the front/head of the deque
   * @throws NoSuchElementException if the deque is empty
   */
  E peekFront();

  /**
   * Returns the element from the back/tail of the deque without removing it.
   *
   * @return the element from the back/tail of the deque
   * @throws NoSuchElementException if the deque is empty
   */
  E peekBack();

  /**
   * Returns {@code true} if this deque contains no elements.
   *
   * @return {@code true} if this deque contains no elements
   */
  boolean isEmpty();
}
