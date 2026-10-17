package eu.happycoders.collections.ringbuffer;

/**
 * A ring buffer that never rejects a write: when the buffer is full, writing discards the oldest
 * element to make room for the new one. The buffer thus always holds the most recent elements, up
 * to its capacity &ndash; the usual choice for the last N log lines or sensor readings.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this buffer
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class OverwritingRingBuffer<E> extends RingBuffer<E> {

  /**
   * Constructs an empty overwriting ring buffer that can hold the specified number of elements.
   *
   * @param capacity the capacity
   * @throws IllegalArgumentException if the capacity is less than 1
   */
  public OverwritingRingBuffer(int capacity) {
    super(capacity);
  }

  /**
   * Writes an element at the write index. If the buffer is full, the oldest element is discarded
   * first.
   *
   * @param element the element to write
   */
  @Override
  public void write(E element) {
    if (isFull()) {
      read(); // discards the oldest element
    }
    super.write(element);
  }
}
