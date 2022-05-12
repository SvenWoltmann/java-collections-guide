package eu.happycoders.collections.queue;

class LinkedListQueueTest extends FifoQueueTest {

  @Override
  <E> Queue<E> createQueueForBaseTests() {
    return new LinkedListQueue<>();
  }
}
