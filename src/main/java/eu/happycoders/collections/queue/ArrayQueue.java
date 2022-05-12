package eu.happycoders.collections.queue;

import java.util.NoSuchElementException;

/**
 * A first-in-first-out (FIFO) queue of objects implemented using a circular array.
 *
 * <p>The array grows as needed to accommodate additional elements; however, it never shrinks.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this stack
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class ArrayQueue<E> implements Queue<E> {

  public static final int MAX_SIZE = Integer.MAX_VALUE - 8;

  private static final int DEFAULT_INITIAL_CAPACITY = 10;

  private Object[] elements;
  private int headIndex;
  private int tailIndex;
  private int numberOfElements;

  /** Constructs an empty queue with an initial capacity sufficient to hold 10 elements. */
  public ArrayQueue() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Constructs an empty queue with a capacity sufficient to hold the specified number of elements.
   *
   * @param capacity the capacity
   */
  public ArrayQueue(int capacity) {
    if (capacity < 1) {
      throw new IllegalArgumentException("Capacity must be 1 or higher");
    }

    elements = new Object[capacity];
  }

  @Override
  public void enqueue(E element) {
    if (numberOfElements == elements.length) {
      grow();
    }
    elements[tailIndex] = element;
    tailIndex++;
    if (tailIndex == elements.length) {
      tailIndex = 0;
    }
    numberOfElements++;
  }

  private void grow() {
    int newCapacity = calculateNewCapacity(elements.length);
    growToNewCapacity(newCapacity);
  }

  static int calculateNewCapacity(int currentCapacity) {
    if (currentCapacity == MAX_SIZE) {
      throw new IllegalStateException("Can't grow further");
    }

    int newCapacity = currentCapacity + calculateIncrement(currentCapacity);

    if (newCapacity > MAX_SIZE || newCapacity < 0 /* overflow */) {
      newCapacity = MAX_SIZE;
    }

    return newCapacity;
  }

  private static int calculateIncrement(int currentCapacity) {
    return currentCapacity < 64 ? currentCapacity : currentCapacity / 2;
  }

  private void growToNewCapacity(int newCapacity) {
    Object[] newArray = new Object[newCapacity];

    // Copy to the beginning of the new array: tailIndex to end of the old array
    int oldArrayLength = elements.length;
    int numberOfElementsAfterTail = oldArrayLength - tailIndex;
    System.arraycopy(elements, tailIndex, newArray, 0, numberOfElementsAfterTail);

    // Append to the new array: beginning to tailIndex of the old array
    if (tailIndex > 0) {
      System.arraycopy(elements, 0, newArray, numberOfElementsAfterTail, tailIndex);
    }

    // Adjust head and tail
    headIndex = 0;
    tailIndex = oldArrayLength;
    elements = newArray;
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
