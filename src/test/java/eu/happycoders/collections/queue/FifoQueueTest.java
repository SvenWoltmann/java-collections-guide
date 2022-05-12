package eu.happycoders.collections.queue;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

abstract class FifoQueueTest extends QueueTest {

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 5, 8})
  void dequeueShouldReturnAllElementsPutIntoTheQueueInFifoOrder(int size) {
    Queue<Integer> queue = createQueueForBaseTests();

    for (int i = 0; i < size; i++) {
      queue.enqueue(i);
    }

    int[] returnedElements = new int[size];

    for (int i = 0; i < size; i++) {
      returnedElements[i] = queue.dequeue();
    }

    assertThat(returnedElements).containsExactly(IntStream.range(0, size).toArray());
  }

  @Test
  void peekShouldReturnHeadElement() {
    Queue<Integer> queue = createQueueForBaseTests();
    queue.enqueue(1);
    queue.enqueue(2);
    queue.enqueue(3);

    Integer element = queue.peek();
    assertThat(element).isEqualTo(1);
  }

  @Test
  void peekShouldNotRemoveHeadElement() {
    Queue<Integer> queue = createQueueForBaseTests();
    queue.enqueue(1);
    queue.enqueue(2);
    queue.enqueue(3);

    queue.peek();

    Integer element = queue.dequeue();
    assertThat(element).isEqualTo(1);
  }
}
