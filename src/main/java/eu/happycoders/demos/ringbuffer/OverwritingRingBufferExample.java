package eu.happycoders.demos.ringbuffer;

import eu.happycoders.collections.ringbuffer.OverwritingRingBuffer;
import eu.happycoders.collections.ringbuffer.RingBuffer;

/**
 * Writes five elements into an overwriting ring buffer of capacity 3: the buffer keeps the three
 * most recent ones.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class OverwritingRingBufferExample {

  public static void main(String[] args) {
    RingBuffer<Integer> buffer = new OverwritingRingBuffer<>(3);

    for (int i = 1; i <= 5; i++) {
      buffer.write(i);
    }
    System.out.println("size: " + buffer.size());

    while (!buffer.isEmpty()) {
      System.out.println("read: " + buffer.read());
    }
  }
}
