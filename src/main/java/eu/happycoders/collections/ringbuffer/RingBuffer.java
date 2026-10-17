package eu.happycoders.collections.ringbuffer;

import java.util.NoSuchElementException;

/**
 * A ring buffer (circular buffer) of fixed capacity: elements are written at the write index and
 * read at the read index, in first-in-first-out order. Both indices wrap around to 0 when they
 * reach the end of the array, so neither writing nor reading ever moves an element.
 *
 * <p>Writing to a full buffer throws an {@link IllegalStateException}; {@link
 * OverwritingRingBuffer} discards the oldest element instead.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this buffer
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class RingBuffer<E> {

  private final Object[] elements;
  private int readIndex;
  private int writeIndex;
  private int size;

  /**
   * Constructs an empty ring buffer that can hold the specified number of elements.
   *
   * @param capacity the capacity
   * @throws IllegalArgumentException if the capacity is less than 1
   */
  public RingBuffer(int capacity) {
    if (capacity < 1) {
      throw new IllegalArgumentException("Capacity must be 1 or higher");
    }
    elements = new Object[capacity];
  }

  /**
   * Writes an element at the write index and advances the write index.
   *
   * @param element the element to write
   * @throws IllegalStateException if the buffer is full
   */
  public void write(E element) {
    if (isFull()) {
      throw new IllegalStateException("The ring buffer is full");
    }
    elements[writeIndex] = element;
    writeIndex = next(writeIndex);
    size++;
  }

  /**
   * Reads and removes the oldest element, the one at the read index, and advances the read index.
   *
   * @return the oldest element
   * @throws NoSuchElementException if the buffer is empty
   */
  public E read() {
    if (isEmpty()) {
      throw new NoSuchElementException("The ring buffer is empty");
    }

    @SuppressWarnings("unchecked")
    E element = (E) elements[readIndex];

    elements[readIndex] = null;
    readIndex = next(readIndex);
    size--;
    return element;
  }

  /**
   * Returns {@code true} if this buffer contains no elements.
   *
   * @return {@code true} if this buffer contains no elements
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns {@code true} if this buffer holds as many elements as its capacity allows.
   *
   * @return {@code true} if this buffer is full
   */
  public boolean isFull() {
    return size == elements.length;
  }

  /**
   * Returns the number of elements in this buffer.
   *
   * @return the number of elements in this buffer
   */
  public int size() {
    return size;
  }

  /**
   * Returns the number of elements this buffer can hold.
   *
   * @return the capacity
   */
  public int capacity() {
    return elements.length;
  }

  private int next(int index) {
    index++;
    if (index == elements.length) {
      index = 0;
    }
    return index;
  }
}
