package eu.happycoders.collections.stack;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BoundedArrayStackTest extends StackTest {

  @Override
  <E> Stack<E> createStackForBaseTests() {
    return new BoundedArrayStack<>(100);
  }

  @Test
  void constructorShouldThrowIllegalArgumentExceptionWhenCapacityIs0() {
    assertThatThrownBy(() -> new BoundedArrayStack<>(0))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void pushShouldThrowIllegalStateExceptionWhenStackIsFull() {
    Stack<Integer> stack = new BoundedArrayStack<>(3);
    stack.push(1);
    stack.push(2);
    stack.push(3);
    assertThatThrownBy(() -> stack.push(4)).isInstanceOf(IllegalStateException.class);
  }
}
