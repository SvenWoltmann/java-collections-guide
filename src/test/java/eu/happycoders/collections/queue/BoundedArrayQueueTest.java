package eu.happycoders.collections.queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BoundedArrayQueueTest extends FifoQueueTest {

  @Override
  <E> Queue<E> createQueueForBaseTests() {
    return new BoundedArrayQueue<>(100);
  }

  @Test
  void constructorShouldThrowIllegalArgumentExceptionWhenCapacityIs0() {
    assertThatThrownBy(() -> new BoundedArrayQueue<>(0))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void enqueueShouldThrowIllegalStateExceptionWhenQueueIsFull() {
    Queue<Integer> queue = new BoundedArrayQueue<>(3);
    queue.enqueue(1);
    queue.enqueue(2);
    queue.enqueue(3);
    assertThatThrownBy(() -> queue.enqueue(4)).isInstanceOf(IllegalStateException.class);
  }

  @Test
  void afterTwoElementsAreRemovedFromAQueueWithACapacityOfThreeItShouldAcceptTwoMoreElements() {
    Queue<Integer> queue = new BoundedArrayQueue<>(3);

    queue.enqueue(1);
    queue.enqueue(2);
    queue.enqueue(3);

    assertThat(queue.dequeue()).isEqualTo(1);
    assertThat(queue.dequeue()).isEqualTo(2);

    queue.enqueue(4);
    queue.enqueue(5);

    assertThat(queue.dequeue()).isEqualTo(3);
    assertThat(queue.dequeue()).isEqualTo(4);
    assertThat(queue.dequeue()).isEqualTo(5);
  }
}
