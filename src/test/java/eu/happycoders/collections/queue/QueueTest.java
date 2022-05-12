package eu.happycoders.collections.queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

abstract class QueueTest {

  @Test
  void enqueueDequeueShouldBeRepeatable() {
    Queue<Integer> queue = createQueueForBaseTests();

    queue.enqueue(1);
    assertThat(queue.dequeue()).isEqualTo(1);

    queue.enqueue(2);
    assertThat(queue.dequeue()).isEqualTo(2);
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
