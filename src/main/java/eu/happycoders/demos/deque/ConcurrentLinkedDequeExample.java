package eu.happycoders.demos.deque;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Demo for concurrently inserting into / removing elements from a {@link ConcurrentLinkedDeque}.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class ConcurrentLinkedDequeExample {

  private static final int NUMBER_OF_PRODUCERS = 4;
  private static final int NUMBER_OF_CONSUMERS = 3;
  private static final int NUMBER_OF_ELEMENTS_TO_PUT_INTO_DEQUE_PER_THREAD = 5;
  private static final int MIN_SLEEP_TIME_MILLIS = 500;
  private static final int MAX_SLEEP_TIME_MILLIS = 2000;

  private static volatile boolean consumerShouldBeStoppedWhenDequeIsEmpty = false;

  public static void main(String[] args) throws InterruptedException {
    Deque<Integer> deque = new ConcurrentLinkedDeque<>();

    // Start producers
    CountDownLatch producerFinishLatch = new CountDownLatch(NUMBER_OF_PRODUCERS);
    for (int i = 0; i < NUMBER_OF_PRODUCERS; i++) {
      createProducerThread(deque, producerFinishLatch).start();
    }

    // Start consumers
    for (int i = 0; i < NUMBER_OF_CONSUMERS; i++) {
      createConsumerThread(deque).start();
    }

    // Wait until all producers are finished
    producerFinishLatch.await();

    consumerShouldBeStoppedWhenDequeIsEmpty = true;

    // We'll let the program end when all consumers are finished
  }

  private static Thread createProducerThread(Deque<Integer> deque, CountDownLatch finishLatch) {
    return new Thread(
        () -> {
          ThreadLocalRandom random = ThreadLocalRandom.current();
          for (int i = 0; i < NUMBER_OF_ELEMENTS_TO_PUT_INTO_DEQUE_PER_THREAD; i++) {
            sleepRandomTime();

            Integer element = random.nextInt(1000);
            if (random.nextBoolean()) {
              deque.offerFirst(element);
              System.out.printf(
                  "[%s] deque.offerFirst(%3d)        --> deque = %s%n",
                  Thread.currentThread().getName(), element, deque);
            } else {
              deque.offerLast(element);
              System.out.printf(
                  "[%s] deque.offerLast(%3d)         --> deque = %s%n",
                  Thread.currentThread().getName(), element, deque);
            }
          }

          finishLatch.countDown();
        });
  }

  private static Thread createConsumerThread(Deque<Integer> deque) {
    return new Thread(
        () -> {
          ThreadLocalRandom random = ThreadLocalRandom.current();
          while (true) {
            sleepRandomTime();

            Integer element;
            if (random.nextBoolean()) {
              element = deque.pollFirst();
              System.out.printf(
                  "[%s]     deque.pollFirst() = %4d --> deque = %s%n",
                  Thread.currentThread().getName(), element, deque);
            } else {
              element = deque.pollLast();
              System.out.printf(
                  "[%s]     deque.pollLast()  = %4d --> deque = %s%n",
                  Thread.currentThread().getName(), element, deque);
            }

            if (consumerShouldBeStoppedWhenDequeIsEmpty && element == null) {
              break;
            }
          }
        });
  }

  private static void sleepRandomTime() {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    try {
      Thread.sleep(random.nextInt(MIN_SLEEP_TIME_MILLIS, MAX_SLEEP_TIME_MILLIS));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
