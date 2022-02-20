package eu.happycoders.collections.stack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ArrayStackTest extends StackTest {

  @Override
  <E> Stack<E> createStackForBaseTests() {
    return new ArrayStack<>();
  }

  @Test
  void shouldTakeMoreElementsThanInitialCapacity() {
    Stack<Integer> stack = new ArrayStack<>(2);

    stack.push(1);
    stack.push(2);
    stack.push(3);
    stack.push(4);
    stack.push(5);

    int[] returnedElements = new int[5];

    returnedElements[0] = stack.pop();
    returnedElements[1] = stack.pop();
    returnedElements[2] = stack.pop();
    returnedElements[3] = stack.pop();
    returnedElements[4] = stack.pop();

    assertThat(returnedElements).containsExactly(5, 4, 3, 2, 1);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 30, 63})
  void newCapacityShouldBeDoubledIfCapacityIsLessThan64OrLess(int oldCapacity) {
    int newCapacity = ArrayStack.calculateNewCapacity(oldCapacity);
    assertThat(newCapacity).isEqualTo(oldCapacity * 2);
  }

  @ParameterizedTest
  @ValueSource(ints = {64, 65, 54_321, 87_654_321, 1_431_655_759})
  void newCapacityShouldBeOneAndAHalfTimesAsHighIfCapacityIs64OrHigher(int oldCapacity) {
    int newCapacity = ArrayStack.calculateNewCapacity(oldCapacity);
    assertThat(newCapacity).isEqualTo(oldCapacity + oldCapacity / 2);
  }

  @ParameterizedTest
  @ValueSource(
      ints = {1_431_655_760, 1_431_655_761, ArrayStack.MAX_SIZE - 2, ArrayStack.MAX_SIZE - 1})
  void newCapacityShouldBeMaxSizeIfCapacityWouldExceedIt(int oldCapacity) {
    int newCapacity = ArrayStack.calculateNewCapacity(oldCapacity);
    assertThat(newCapacity).isEqualTo(ArrayStack.MAX_SIZE);
  }

  @Test
  void shouldThrowIllegalStateExceptionIfHasReachedSizeLimit() {
    assertThatThrownBy(() -> ArrayStack.calculateNewCapacity(ArrayStack.MAX_SIZE))
        .isInstanceOf(IllegalStateException.class);
  }
}
