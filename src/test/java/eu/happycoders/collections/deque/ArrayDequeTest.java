package eu.happycoders.collections.deque;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eu.happycoders.collections.stack.ArrayStack;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ArrayDequeTest extends DequeTest {

  @Override
  <E> Deque<E> createDequeForBaseTests() {
    return new ArrayDeque<>();
  }

  @Test
  void constructorShouldThrowIllegalArgumentExceptionWhenCapacityIs0() {
    assertThatThrownBy(() -> new ArrayDeque<>(0)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void dequeShouldAllowMoreElementsThanTheInitialCapacityEnqueueBack() {
    int capacity = 3;
    int numberOfElements = 10;

    Deque<Integer> deque = new ArrayDeque<>(capacity);

    for (int i = 0; i < numberOfElements; i++) {
      deque.enqueueBack(i);
    }

    int[] returnedElements = new int[numberOfElements];

    for (int i = 0; i < numberOfElements; i++) {
      returnedElements[i] = deque.dequeueFront();
    }

    assertThat(returnedElements).containsExactly(IntStream.range(0, numberOfElements).toArray());
  }

  @Test
  void dequeShouldAllowMoreElementsThanTheInitialCapacityEnqueueFront() {
    int capacity = 3;
    int numberOfElements = 10;

    Deque<Integer> deque = new ArrayDeque<>(capacity);

    for (int i = 0; i < numberOfElements; i++) {
      deque.enqueueFront(i);
    }

    int[] returnedElements = new int[numberOfElements];

    for (int i = 0; i < numberOfElements; i++) {
      returnedElements[i] = deque.dequeueBack();
    }

    assertThat(returnedElements).containsExactly(IntStream.range(0, numberOfElements).toArray());
  }

  @Test
  void dequeShouldGrowCorrectlyWhenElementsAreWrapped() {
    Deque<Integer> deque = new ArrayDeque<>(3);

    deque.enqueueBack(1);
    deque.enqueueBack(2);

    assertThat(deque.dequeueFront()).isEqualTo(1);
    assertThat(deque.dequeueFront()).isEqualTo(2);

    deque.enqueueBack(3);
    deque.enqueueBack(4); // wrap around --> will be written at index 0
    deque.enqueueBack(5);
    deque.enqueueBack(6); // will make the deque grow

    assertThat(deque.dequeueFront()).isEqualTo(3);
    assertThat(deque.dequeueFront()).isEqualTo(4);
    assertThat(deque.dequeueFront()).isEqualTo(5);
    assertThat(deque.dequeueFront()).isEqualTo(6);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 30, 63})
  void newCapacityShouldBeDoubledIfCapacityIsLessThan64OrLess(int oldCapacity) {
    int newCapacity = ArrayDeque.calculateNewCapacity(oldCapacity);
    assertThat(newCapacity).isEqualTo(oldCapacity * 2);
  }

  @ParameterizedTest
  @ValueSource(ints = {64, 65, 54_321, 87_654_321, 1_431_655_759})
  void newCapacityShouldBeOneAndAHalfTimesAsHighIfCapacityIs64OrHigher(int oldCapacity) {
    int newCapacity = ArrayDeque.calculateNewCapacity(oldCapacity);
    assertThat(newCapacity).isEqualTo(oldCapacity + oldCapacity / 2);
  }

  @ParameterizedTest
  @ValueSource(
      ints = {1_431_655_760, 1_431_655_761, ArrayStack.MAX_SIZE - 2, ArrayStack.MAX_SIZE - 1})
  void newCapacityShouldBeMaxSizeIfCapacityWouldExceedIt(int oldCapacity) {
    int newCapacity = ArrayDeque.calculateNewCapacity(oldCapacity);
    assertThat(newCapacity).isEqualTo(ArrayStack.MAX_SIZE);
  }

  @Test
  void shouldThrowIllegalStateExceptionIfHasReachedSizeLimit() {
    assertThatThrownBy(() -> ArrayDeque.calculateNewCapacity(ArrayStack.MAX_SIZE))
        .isInstanceOf(IllegalStateException.class);
  }
}
