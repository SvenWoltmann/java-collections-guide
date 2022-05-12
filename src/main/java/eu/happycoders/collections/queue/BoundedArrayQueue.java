package eu.happycoders.collections.queue;

import java.util.NoSuchElementException;

/**
 * A fixed-size first-in-first-out (FIFO) queue of objects implemented using a circular array.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this stack
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class BoundedArrayQueue<E> implements Queue<E> {

  private final Object[] elements;
  private int headIndex;
  private int tailIndex;
  private int numberOfElements;

  /**
   * Constructs an empty queue with a capacity sufficient to hold the specified number of elements.
   *
   * @param capacity the capacity
   */
  public BoundedArrayQueue(int capacity) {
    if (capacity < 1) {
      throw new IllegalArgumentException("Capacity must be 1 or higher");
    }

    elements = new Object[capacity];
  }

  @Override
  public void enqueue(E element) {
    if (numberOfElements == elements.length) {
      throw new IllegalStateException("The queue is full");
    }
    elements[tailIndex] = element;
    tailIndex++;
    if (tailIndex == elements.length) {
      tailIndex = 0;
    }
    numberOfElements++;
  }

  @Override
  public E dequeue() {
    final E element = elementAtHead();
    elements[headIndex] = null;
    headIndex++;
    if (headIndex == elements.length) {
      headIndex = 0;
    }
    numberOfElements--;
    return element;
  }

  @Override
  public E peek() {
    return elementAtHead();
  }

  private E elementAtHead() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }

    @SuppressWarnings("unchecked")
    E element = (E) elements[headIndex];

    return element;
  }

  @Override
  public boolean isEmpty() {
    return numberOfElements == 0;
  }
}
