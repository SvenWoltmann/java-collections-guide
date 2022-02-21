package eu.happycoders.collections.stack;

class QueueStackTest extends StackTest {

  @Override
  <E> Stack<E> createStackForBaseTests() {
    return new QueueStack<>();
  }
}
