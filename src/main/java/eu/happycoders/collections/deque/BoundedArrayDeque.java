package eu.happycoders.collections.deque;

import java.util.NoSuchElementException;

/**
 * A fixed-size deque (double-ended queue) of objects implemented using a circular array.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this deque
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class BoundedArrayDeque<E> implements Deque<E> {

  private final Object[] elements;
  private int headIndex;
  private int tailIndex;
  private int numberOfElements;

  /**
   * Constructs an empty queue with a capacity sufficient to hold the specified number of elements.
   *
   * @param capacity the capacity
   */
  public BoundedArrayDeque(int capacity) {
    if (capacity < 1) {
      throw new IllegalArgumentException("Capacity must be 1 or higher");
    }

    elements = new Object[capacity];
  }

  @Override
  public void enqueueFront(E element) {
    if (numberOfElements == elements.length) {
      throw new IllegalStateException("The deque is full");
    }
    headIndex = decreaseIndex(headIndex);
    elements[headIndex] = element;
    numberOfElements++;
  }

  @Override
  public void enqueueBack(E element) {
    if (numberOfElements == elements.length) {
      throw new IllegalStateException("The deque is full");
    }
    elements[tailIndex] = element;
    tailIndex = increaseIndex(tailIndex);
    numberOfElements++;
  }

  @Override
  public E dequeueFront() {
    final E element = elementAtHead();
    elements[headIndex] = null;
    headIndex = increaseIndex(headIndex);
    numberOfElements--;
    return element;
  }

  @Override
  public E dequeueBack() {
    final E element = elementAtTail();
    tailIndex = decreaseIndex(tailIndex);
    elements[tailIndex] = null;
    numberOfElements--;
    return element;
  }

  @Override
  public E peekFront() {
    return elementAtHead();
  }

  @Override
  public E peekBack() {
    return elementAtTail();
  }

  private E elementAtHead() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }

    @SuppressWarnings("unchecked")
    E element = (E) elements[headIndex];

    return element;
  }

  private E elementAtTail() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }

    @SuppressWarnings("unchecked")
    E element = (E) elements[decreaseIndex(tailIndex)];

    return element;
  }

  private int decreaseIndex(int index) {
    index--;
    if (index < 0) {
      index = elements.length - 1;
    }
    return index;
  }

  private int increaseIndex(int index) {
    index++;
    if (index == elements.length) {
      index = 0;
    }
    return index;
  }

  @Override
  public boolean isEmpty() {
    return numberOfElements == 0;
  }
}
