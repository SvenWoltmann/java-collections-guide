package eu.happycoders.collections.stack;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StacksTest {

  @Test
  void reversingAnEmptyStackShouldLeaveStackEmtpy() {
    Stack<Integer> stack = new ArrayStack<>();

    Stacks.reverse(stack);

    assertThat(stack.isEmpty()).isTrue();
  }

  @Test
  void reversingAStackWithASingleElementDoesntChangeTheStack() {
    Stack<String> stack = new ArrayStack<>();
    stack.push("foo");

    Stacks.reverse(stack);

    String element = stack.pop();
    assertThat(element).isEqualTo("foo");
  }

  @Test
  void reversingAStackWithThreeElementsShouldReverseTheElementsOrder() {
    Stack<Integer> stack = new ArrayStack<>();
    stack.push(1);
    stack.push(2);
    stack.push(3);

    Stacks.reverse(stack);

    int[] returnedElements = new int[3];
    returnedElements[0] = stack.pop();
    returnedElements[1] = stack.pop();
    returnedElements[2] = stack.pop();
    assertThat(returnedElements).containsExactly(1, 2, 3);
  }
}
