package eu.happycoders.collections.queue;

class StackQueueTest extends FifoQueueTest {

  @Override
  <E> Queue<E> createQueueForBaseTests() {
    return new StackQueue<>();
  }
}
