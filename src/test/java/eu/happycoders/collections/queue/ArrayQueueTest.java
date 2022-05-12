package eu.happycoders.collections.queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eu.happycoders.collections.stack.ArrayStack;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ArrayQueueTest extends QueueTest {

  @Override
  <E> Queue<E> createQueueForBaseTests() {
    return new ArrayQueue<>();
  }

  @Test
  void constructorShouldThrowIllegalArgumentExceptionWhenCapacityIs0() {
    assertThatThrownBy(() -> new ArrayQueue<>(0)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void queueShouldEnqueueAndDequeueAroundTheCircularArray() {
    Queue<Integer> queue = new ArrayQueue<>(3);

    queue.enqueue(1);
    queue.enqueue(2);

    assertThat(queue.dequeue()).isEqualTo(1);
    assertThat(queue.dequeue()).isEqualTo(2);

    queue.enqueue(3);
    queue.enqueue(4); // wrap around --> will be written at index 0
    queue.enqueue(5);

    assertThat(queue.dequeue()).isEqualTo(3);
    assertThat(queue.dequeue()).isEqualTo(4);
    assertThat(queue.dequeue()).isEqualTo(5);
  }

  @Test
  void queueShouldAllowMoreElementsThanTheInitialCapacity() {
    int capacity = 3;
    int numberOfElements = 10;

    Queue<Integer> queue = new ArrayQueue<>(capacity);

    for (int i = 0; i < numberOfElements; i++) {
      queue.enqueue(i);
    }

    int[] returnedElements = new int[numberOfElements];

    for (int i = 0; i < numberOfElements; i++) {
      returnedElements[i] = queue.dequeue();
    }

    assertThat(returnedElements).containsExactly(IntStream.range(0, numberOfElements).toArray());
  }

  @Test
  void queueShouldGrowCorrectlyWhenElementsAreWrapped() {
    Queue<Integer> queue = new ArrayQueue<>(3);

    queue.enqueue(1);
    queue.enqueue(2);

    assertThat(queue.dequeue()).isEqualTo(1);
    assertThat(queue.dequeue()).isEqualTo(2);

    queue.enqueue(3);
    queue.enqueue(4); // wrap around --> will be written at index 0
    queue.enqueue(5);
    queue.enqueue(6); // will make the queue grow

    assertThat(queue.dequeue()).isEqualTo(3);
    assertThat(queue.dequeue()).isEqualTo(4);
    assertThat(queue.dequeue()).isEqualTo(5);
    assertThat(queue.dequeue()).isEqualTo(6);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 30, 63})
  void newCapacityShouldBeDoubledIfCapacityIsLessThan64OrLess(int oldCapacity) {
    int newCapacity = ArrayQueue.calculateNewCapacity(oldCapacity);
    assertThat(newCapacity).isEqualTo(oldCapacity * 2);
  }

  @ParameterizedTest
  @ValueSource(ints = {64, 65, 54_321, 87_654_321, 1_431_655_759})
  void newCapacityShouldBeOneAndAHalfTimesAsHighIfCapacityIs64OrHigher(int oldCapacity) {
    int newCapacity = ArrayQueue.calculateNewCapacity(oldCapacity);
    assertThat(newCapacity).isEqualTo(oldCapacity + oldCapacity / 2);
  }

  @ParameterizedTest
  @ValueSource(
      ints = {1_431_655_760, 1_431_655_761, ArrayStack.MAX_SIZE - 2, ArrayStack.MAX_SIZE - 1})
  void newCapacityShouldBeMaxSizeIfCapacityWouldExceedIt(int oldCapacity) {
    int newCapacity = ArrayQueue.calculateNewCapacity(oldCapacity);
    assertThat(newCapacity).isEqualTo(ArrayStack.MAX_SIZE);
  }

  @Test
  void shouldThrowIllegalStateExceptionIfHasReachedSizeLimit() {
    assertThatThrownBy(() -> ArrayQueue.calculateNewCapacity(ArrayStack.MAX_SIZE))
        .isInstanceOf(IllegalStateException.class);
  }
}
