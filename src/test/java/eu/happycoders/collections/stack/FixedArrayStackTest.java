package eu.happycoders.collections.stack;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class FixedArrayStackTest extends StackTest {

  @Override
  <E> Stack<E> createStackForBaseTests() {
    return new FixedArrayStack<>(100);
  }

  @Test
  void constructorShouldThrowIllegalArgumentExceptionWhenCapacityIs0() {
    assertThatThrownBy(() -> new FixedArrayStack<>(0)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void pushShouldThrowIllegalStateExceptionWhenStackIsFull() {
    Stack<Integer> stack = new FixedArrayStack<>(3);
    stack.push(1);
    stack.push(2);
    stack.push(3);
    assertThatThrownBy(() -> stack.push(4)).isInstanceOf(IllegalStateException.class);
  }
}
