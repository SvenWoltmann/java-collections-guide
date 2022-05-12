package eu.happycoders.collections.queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

abstract class QueueTest {

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
  void enqueueDequeueShouldBeRepeatable() {
    Queue<Integer> queue = createQueueForBaseTests();

    queue.enqueue(1);
    assertThat(queue.dequeue()).isEqualTo(1);

    queue.enqueue(2);
    assertThat(queue.dequeue()).isEqualTo(2);
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

  @Test
  void dequeueShouldThrowNoSuchElementExceptionWhenQueueIsEmpty() {
    Queue<String> queue = createQueueForBaseTests();

    assertThatThrownBy(queue::dequeue).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void peekShouldThrowNoSuchElementExceptionWhenQueueIsEmpty() {
    Queue<String> queue = createQueueForBaseTests();

    assertThatThrownBy(queue::peek).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void isEmptyShouldReturnTrueForEmptyQueue() {
    Queue<String> queue = createQueueForBaseTests();
    boolean empty = queue.isEmpty();
    assertThat(empty).isTrue();
  }

  @Test
  void isEmptyShouldReturnFalseIfQueueContainsAnElement() {
    Queue<String> queue = createQueueForBaseTests();
    queue.enqueue("foo");
    boolean empty = queue.isEmpty();
    assertThat(empty).isFalse();
  }

  abstract <E> Queue<E> createQueueForBaseTests();
}
