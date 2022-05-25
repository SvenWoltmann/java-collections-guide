package eu.happycoders.collections.deque;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

abstract class DequeTest {

  @Test
  void enqueueDequeueFrontShouldBeRepeatable() {
    Deque<Integer> deque = createDequeForBaseTests();

    deque.enqueueFront(1);
    assertThat(deque.dequeueFront()).isEqualTo(1);

    deque.enqueueFront(2);
    assertThat(deque.dequeueFront()).isEqualTo(2);
  }

  @Test
  void enqueueDequeueBackShouldBeRepeatable() {
    Deque<Integer> deque = createDequeForBaseTests();

    deque.enqueueBack(3);
    assertThat(deque.dequeueBack()).isEqualTo(3);

    deque.enqueueBack(4);
    assertThat(deque.dequeueBack()).isEqualTo(4);
  }

  @Test
  void dequeueFrontShouldThrowNoSuchElementExceptionWhenDequeIsEmpty() {
    Deque<String> deque = createDequeForBaseTests();

    assertThatThrownBy(deque::dequeueFront).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void dequeueBackShouldThrowNoSuchElementExceptionWhenDequeIsEmpty() {
    Deque<String> deque = createDequeForBaseTests();

    assertThatThrownBy(deque::dequeueBack).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void peekFrontShouldThrowNoSuchElementExceptionWhenDequeIsEmpty() {
    Deque<String> deque = createDequeForBaseTests();

    assertThatThrownBy(deque::peekFront).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void peekBackShouldThrowNoSuchElementExceptionWhenDequeIsEmpty() {
    Deque<String> deque = createDequeForBaseTests();

    assertThatThrownBy(deque::peekBack).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void isEmptyShouldReturnTrueIfDequeIsEmpty() {
    Deque<String> deque = createDequeForBaseTests();
    boolean empty = deque.isEmpty();
    assertThat(empty).isTrue();
  }

  @Test
  void isEmptyShouldReturnFalseIfDequeContainsAnElement() {
    Deque<String> deque = createDequeForBaseTests();
    deque.enqueueBack("foo");
    boolean empty = deque.isEmpty();
    assertThat(empty).isFalse();
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 5, 8})
  void dequeueFrontShouldReturnAllElementsPutIntoTheDequeViaEnqueueBackInFifoOrder(int size) {
    Deque<Integer> deque = createDequeForBaseTests();

    for (int i = 0; i < size; i++) {
      deque.enqueueBack(i);
    }

    int[] returnedElements = new int[size];

    for (int i = 0; i < size; i++) {
      returnedElements[i] = deque.dequeueFront();
    }

    assertThat(returnedElements).containsExactly(IntStream.range(0, size).toArray());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 5, 8})
  void dequeueBackShouldReturnAllElementsPutIntoTheDequeViaEnqueueFrontInFifoOrder(int size) {
    Deque<Integer> deque = createDequeForBaseTests();

    for (int i = 0; i < size; i++) {
      deque.enqueueFront(i);
    }

    int[] returnedElements = new int[size];

    for (int i = 0; i < size; i++) {
      returnedElements[i] = deque.dequeueBack();
    }

    assertThat(returnedElements).containsExactly(IntStream.range(0, size).toArray());
  }

  @Test
  void peekFrontShouldReturnHeadElement() {
    Deque<Integer> deque = createDequeForBaseTests();
    deque.enqueueBack(1);
    deque.enqueueBack(2);
    deque.enqueueBack(3);

    Integer element = deque.peekFront();
    assertThat(element).isEqualTo(1);
  }

  @Test
  void peekBackShouldReturnTailElement() {
    Deque<Integer> deque = createDequeForBaseTests();
    deque.enqueueFront(1);
    deque.enqueueFront(2);
    deque.enqueueFront(3);

    Integer element = deque.peekBack();
    assertThat(element).isEqualTo(1);
  }

  @Test
  void peekFrontShouldNotRemoveHeadElement() {
    Deque<Integer> deque = createDequeForBaseTests();
    deque.enqueueBack(1);
    deque.enqueueBack(2);
    deque.enqueueBack(3);

    deque.peekFront();

    Integer element = deque.dequeueFront();
    assertThat(element).isEqualTo(1);
  }

  @Test
  void peekBackShouldNotRemoveTailElement() {
    Deque<Integer> deque = createDequeForBaseTests();
    deque.enqueueFront(1);
    deque.enqueueFront(2);
    deque.enqueueFront(3);

    deque.peekBack();

    Integer element = deque.dequeueBack();
    assertThat(element).isEqualTo(1);
  }

  abstract <E> Deque<E> createDequeForBaseTests();
}
