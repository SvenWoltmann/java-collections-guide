package eu.happycoders.collections.stack;

class SingleQueueStackTest extends StackTest {

  @Override
  <E> Stack<E> createStackForBaseTests() {
    return new SingleQueueStack<>();
  }
}
