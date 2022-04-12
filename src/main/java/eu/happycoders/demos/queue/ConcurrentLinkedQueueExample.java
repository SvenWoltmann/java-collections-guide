package eu.happycoders.demos.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Demo for concurrently inserting into / removing elements from a {@link ConcurrentLinkedQueue}.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class ConcurrentLinkedQueueExample {

  private static final int NUMBER_OF_PRODUCERS = 4;
  private static final int NUMBER_OF_CONSUMERS = 3;
  private static final int NUMBER_OF_ELEMENTS_TO_PUT_INTO_QUEUE_PER_THREAD = 5;
  private static final int MIN_SLEEP_TIME_MILLIS = 500;
  private static final int MAX_SLEEP_TIME_MILLIS = 2000;

  private static final int POISON_PILL = -1;

  public static void main(String[] args) throws InterruptedException {
    Queue<Integer> queue = new ConcurrentLinkedQueue<>();

    // Start producers
    CountDownLatch producerFinishLatch = new CountDownLatch(NUMBER_OF_PRODUCERS);
    for (int i = 0; i < NUMBER_OF_PRODUCERS; i++) {
      createProducerThread(queue, producerFinishLatch).start();
    }

    // Start consumers
    for (int i = 0; i < NUMBER_OF_CONSUMERS; i++) {
      createConsumerThread(queue).start();
    }

    // Wait until all producers are finished
    producerFinishLatch.await();

    // Put poison pills on the queue (one for each consumer)
    for (int i = 0; i < NUMBER_OF_CONSUMERS; i++) {
      queue.offer(POISON_PILL);
    }

    // We'll let the program end when all consumers are finished
  }

  private static Thread createProducerThread(Queue<Integer> queue, CountDownLatch finishLatch) {
    return new Thread(
        () -> {
          ThreadLocalRandom random = ThreadLocalRandom.current();
          for (int i = 0; i < NUMBER_OF_ELEMENTS_TO_PUT_INTO_QUEUE_PER_THREAD; i++) {
            sleepRandomTime();

            Integer element = random.nextInt(1000);
            queue.offer(element);
            System.out.printf(
                "[%s] queue.offer(%3d)        --> queue = %s%n",
                Thread.currentThread().getName(), element, queue);
          }

          finishLatch.countDown();
        });
  }

  private static Thread createConsumerThread(Queue<Integer> queue) {
    return new Thread(
        () -> {
          while (true) {
            sleepRandomTime();

            Integer element = queue.poll();
            System.out.printf(
                "[%s]     queue.poll() = %4d --> queue = %s%n",
                Thread.currentThread().getName(), element, queue);

            // End the thread when a poison pill is detected
            if (element != null && element == POISON_PILL) {
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
