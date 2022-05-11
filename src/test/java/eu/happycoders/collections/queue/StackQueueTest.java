package eu.happycoders.collections.queue;

class StackQueueTest extends QueueTest {

  @Override
  <E> Queue<E> createQueueForBaseTests() {
    return new StackQueue<>();
  }
}
