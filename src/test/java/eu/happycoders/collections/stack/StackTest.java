package eu.happycoders.collections.stack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

abstract class StackTest {

  @Test
  void popShouldReturnAllElementsPutIntoTheStackInReverseOrder() {
    Stack<Integer> stack = createStackForBaseTests();
    stack.push(1);
    stack.push(2);
    stack.push(3);

    int[] returnedElements = new int[3];

    returnedElements[0] = stack.pop();
    returnedElements[1] = stack.pop();
    returnedElements[2] = stack.pop();

    assertThat(returnedElements).containsExactly(3, 2, 1);
  }

  @Test
  void peekShouldReturnTopElement() {
    Stack<Integer> stack = createStackForBaseTests();
    stack.push(1);
    stack.push(2);

    Integer element = stack.peek();
    assertThat(element).isEqualTo(2);
  }

  @Test
  void peekShouldNotRemoveTopElement() {
    Stack<Integer> stack = createStackForBaseTests();
    stack.push(1);
    stack.push(2);

    stack.peek();

    Integer element = stack.pop();
    assertThat(element).isEqualTo(2);
  }

  @Test
  void popShouldThrowNoSuchElementExceptionWhenStackIsEmpty() {
    Stack<String> stack = createStackForBaseTests();

    assertThatThrownBy(stack::pop).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void peekShouldThrowNoSuchElementExceptionWhenStackIsEmpty() {
    Stack<String> stack = createStackForBaseTests();

    assertThatThrownBy(stack::peek).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void isEmptyShouldReturnTrueForEmptyStack() {
    Stack<String> stack = createStackForBaseTests();
    boolean empty = stack.isEmpty();
    assertThat(empty).isTrue();
  }

  @Test
  void isEmptyShouldReturnFalseIfStackContainsAnElement() {
    Stack<String> stack = createStackForBaseTests();
    stack.push("foo");
    boolean empty = stack.isEmpty();
    assertThat(empty).isFalse();
  }

  abstract <E> Stack<E> createStackForBaseTests();
}
