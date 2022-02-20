package eu.happycoders.collections.stack;

class ArrayDequeStackTest extends StackTest {

  @Override
  <E> Stack<E> createStackForBaseTests() {
    return new ArrayDequeStack<>();
  }
}
