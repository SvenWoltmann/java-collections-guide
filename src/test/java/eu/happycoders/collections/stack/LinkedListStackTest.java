package eu.happycoders.collections.stack;

class LinkedListStackTest extends StackTest {

  @Override
  <E> Stack<E> createStackForBaseTests() {
    return new LinkedListStack<>();
  }
}
