package eu.happycoders.collections.queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eu.happycoders.collections.stack.ArrayStack;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OptimizedHeapPriorityQueueTest extends QueueTest {

  @Override
  <E> Queue<E> createQueueForBaseTests() {
    @SuppressWarnings("unchecked")
    Queue<E> queue = (Queue<E>) new OptimizedHeapPriorityQueue<>();
    return queue;
  }

  @Test
  void constructorShouldThrowIllegalArgumentExceptionWhenCapacityIs0() {
    assertThatThrownBy(() -> new OptimizedHeapPriorityQueue<>(0))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void dequeueShouldReturnElementsOrderedByPriority() {
    Queue<Integer> queue = new OptimizedHeapPriorityQueue<>();

    queue.enqueue(3);
    queue.enqueue(1);
    queue.enqueue(4);
    queue.enqueue(1);
    queue.enqueue(5);
    queue.enqueue(9);
    queue.enqueue(2);
    queue.enqueue(6);
    queue.enqueue(5);

    int[] returnedElements = new int[9];

    for (int i = 0; i < 9; i++) {
      returnedElements[i] = queue.dequeue();
    }

    assertThat(returnedElements).containsExactly(1, 1, 2, 3, 4, 5, 5, 6, 9);
  }

  @Test
  void peekShouldReturnHeadElement() {
    Queue<Integer> queue = new OptimizedHeapPriorityQueue<>();
    queue.enqueue(3);
    queue.enqueue(1);
    queue.enqueue(4);

    Integer element = queue.peek();
    assertThat(element).isEqualTo(1);
  }

  @Test
  void peekShouldNotRemoveHeadElement() {
    Queue<Integer> queue = new OptimizedHeapPriorityQueue<>();
    queue.enqueue(3);
    queue.enqueue(1);
    queue.enqueue(4);

    queue.peek();

    Integer element = queue.dequeue();
    assertThat(element).isEqualTo(1);
  }

  @Test
  void queueShouldAllowMoreElementsThanTheInitialCapacity() {
    int capacity = 3;
    int numberOfElements = 10;

    Queue<Integer> queue = new OptimizedHeapPriorityQueue<>(capacity);

    for (int i = 0; i < numberOfElements; i++) {
      queue.enqueue(i);
    }

    int[] returnedElements = new int[numberOfElements];

    for (int i = 0; i < numberOfElements; i++) {
      returnedElements[i] = queue.dequeue();
    }

    assertThat(returnedElements).containsExactly(IntStream.range(0, numberOfElements).toArray());
  }

  @ParameterizedTest
  @ValueSource(ints = {64, 65, 54_321, 87_654_321, 1_431_655_759})
  void newCapacityShouldBeOneAndAHalfTimesAsHighIfCapacityIs64OrHigher(int oldCapacity) {
    int newCapacity = OptimizedHeapPriorityQueue.calculateNewCapacity(oldCapacity);
    assertThat(newCapacity).isEqualTo(oldCapacity + oldCapacity / 2);
  }

  @ParameterizedTest
  @ValueSource(
      ints = {1_431_655_760, 1_431_655_761, ArrayStack.MAX_SIZE - 2, ArrayStack.MAX_SIZE - 1})
  void newCapacityShouldBeMaxSizeIfCapacityWouldExceedIt(int oldCapacity) {
    int newCapacity = OptimizedHeapPriorityQueue.calculateNewCapacity(oldCapacity);
    assertThat(newCapacity).isEqualTo(ArrayStack.MAX_SIZE);
  }

  @Test
  void shouldThrowIllegalStateExceptionIfHasReachedSizeLimit() {
    assertThatThrownBy(() -> OptimizedHeapPriorityQueue.calculateNewCapacity(ArrayStack.MAX_SIZE))
        .isInstanceOf(IllegalStateException.class);
  }
}
