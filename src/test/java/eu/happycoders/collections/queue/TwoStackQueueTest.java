package eu.happycoders.collections.queue;

class TwoStackQueueTest extends FifoQueueTest {

  @Override
  <E> Queue<E> createQueueForBaseTests() {
    return new TwoStackQueue<>();
  }
}
