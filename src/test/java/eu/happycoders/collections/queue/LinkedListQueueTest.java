package eu.happycoders.collections.queue;

class LinkedListQueueTest extends QueueTest {

  @Override
  <E> Queue<E> createQueueForBaseTests() {
    return new LinkedListQueue<>();
  }
}
