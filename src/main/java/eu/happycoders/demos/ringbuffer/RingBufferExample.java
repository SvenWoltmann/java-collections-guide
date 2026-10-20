package eu.happycoders.demos.ringbuffer;

import eu.happycoders.collections.ringbuffer.RingBuffer;

/**
 * Writes and reads a ring buffer of capacity 3 across the end of its array: after two reads, the
 * next two writes land on slots 0 and 1 while the oldest element still sits on slot 2.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class RingBufferExample {

  static void main() {
    RingBuffer<String> buffer = new RingBuffer<>(3);

    buffer.write("a");
    buffer.write("b");
    buffer.write("c");
    System.out.println("read: " + buffer.read());
    System.out.println("read: " + buffer.read());

    buffer.write("d"); // lands on slot 0
    buffer.write("e"); // lands on slot 1
    System.out.println("full: " + buffer.isFull());

    while (!buffer.isEmpty()) {
      System.out.println("read: " + buffer.read());
    }
  }
}
