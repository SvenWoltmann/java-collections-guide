package eu.happycoders.collections.deque;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BoundedArrayDequeTest extends DequeTest {

  @Override
  <E> Deque<E> createDequeForBaseTests() {
    return new BoundedArrayDeque<>(100);
  }

  @Test
  void constructorShouldThrowIllegalArgumentExceptionWhenCapacityIs0() {
    assertThatThrownBy(() -> new BoundedArrayDeque<>(0))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void enqueueBackShouldThrowIllegalStateExceptionWhenDequeIsFull() {
    Deque<Integer> deque = new BoundedArrayDeque<>(3);
    deque.enqueueBack(1);
    deque.enqueueBack(2);
    deque.enqueueBack(3);
    assertThatThrownBy(() -> deque.enqueueBack(4)).isInstanceOf(IllegalStateException.class);
  }

  @Test
  void enqueueFrontShouldThrowIllegalStateExceptionWhenDequeIsFull() {
    Deque<Integer> deque = new BoundedArrayDeque<>(3);
    deque.enqueueFront(1);
    deque.enqueueFront(2);
    deque.enqueueFront(3);
    assertThatThrownBy(() -> deque.enqueueFront(4)).isInstanceOf(IllegalStateException.class);
  }

  @Test
  void after2ElementsAreRemovedFromADequeWithACapacityOf3ItShouldAccept2MoreElementsEnqueueBack() {
    Deque<Integer> deque = new BoundedArrayDeque<>(3);

    deque.enqueueBack(1);
    deque.enqueueBack(2);
    deque.enqueueBack(3);

    assertThat(deque.dequeueFront()).isEqualTo(1);
    assertThat(deque.dequeueFront()).isEqualTo(2);

    deque.enqueueBack(4);
    deque.enqueueBack(5);

    assertThat(deque.dequeueFront()).isEqualTo(3);
    assertThat(deque.dequeueFront()).isEqualTo(4);
    assertThat(deque.dequeueFront()).isEqualTo(5);
  }

  @Test
  void after2ElementsAreRemovedFromADequeWithACapacityOf3ItShouldAccept2MoreElementsEnqueueFront() {
    Deque<Integer> deque = new BoundedArrayDeque<>(3);

    deque.enqueueFront(1);
    deque.enqueueFront(2);
    deque.enqueueFront(3);

    assertThat(deque.dequeueBack()).isEqualTo(1);
    assertThat(deque.dequeueBack()).isEqualTo(2);

    deque.enqueueFront(4);
    deque.enqueueFront(5);

    assertThat(deque.dequeueBack()).isEqualTo(3);
    assertThat(deque.dequeueBack()).isEqualTo(4);
    assertThat(deque.dequeueBack()).isEqualTo(5);
  }

  @Test
  void after2ElementsAreRemovedFromADequeWithACapacityOf3ItShouldAccept2MoreElementsEnqueueMixed() {
    Deque<Integer> deque = new BoundedArrayDeque<>(3);

    deque.enqueueBack(1);
    deque.enqueueFront(2);
    deque.enqueueBack(3);

    assertThat(deque.dequeueFront()).isEqualTo(2);
    assertThat(deque.dequeueBack()).isEqualTo(3);

    deque.enqueueFront(4);
    deque.enqueueBack(5);

    assertThat(deque.dequeueFront()).isEqualTo(4);
    assertThat(deque.dequeueBack()).isEqualTo(5);
    assertThat(deque.dequeueFront()).isEqualTo(1);
  }
}
