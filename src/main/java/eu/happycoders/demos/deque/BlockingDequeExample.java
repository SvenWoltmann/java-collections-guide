package eu.happycoders.demos.deque;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Demo for the blocking methods of Java's {@link BlockingDeque} interface: a consumer thread takes
 * one element per second from the tail; the main thread inserts five elements at the head as fast
 * as it can, and the fourth and fifth call block until the consumer makes room.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class BlockingDequeExample {
  private static final long START = System.currentTimeMillis();

  static void main() throws InterruptedException {
    BlockingDeque<Integer> deque = new LinkedBlockingDeque<>(3);

    Thread consumer =
        Thread.ofPlatform()
            .start(
                () -> {
                  try {
                    for (int i = 0; i < 5; i++) {
                      Thread.sleep(1000);
                      log("takeLast() returned " + deque.takeLast());
                    }
                  } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                  }
                });

    for (int i = 1; i <= 5; i++) {
      deque.putFirst(i);
      log("putFirst(" + i + ") returned");
    }

    consumer.join();
  }

  private static void log(String message) {
    long seconds = Math.round((System.currentTimeMillis() - START) / 1000.0);
    System.out.println(seconds + " s: " + message);
  }
}
