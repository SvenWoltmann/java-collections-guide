package eu.happycoders.collections.ringbuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class RingBufferTest {

  @Test
  void constructorShouldThrowIllegalArgumentExceptionWhenCapacityIs0() {
    assertThatThrownBy(() -> new RingBuffer<>(0)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void newBufferShouldBeEmptyAndNotFull() {
    RingBuffer<Integer> buffer = new RingBuffer<>(3);

    assertThat(buffer.isEmpty()).isTrue();
    assertThat(buffer.isFull()).isFalse();
    assertThat(buffer.size()).isZero();
    assertThat(buffer.capacity()).isEqualTo(3);
  }

  @Test
  void readShouldReturnElementsInTheOrderTheyWereWritten() {
    RingBuffer<String> buffer = new RingBuffer<>(3);
    buffer.write("a");
    buffer.write("b");
    buffer.write("c");

    assertThat(buffer.read()).isEqualTo("a");
    assertThat(buffer.read()).isEqualTo("b");
    assertThat(buffer.read()).isEqualTo("c");
    assertThat(buffer.isEmpty()).isTrue();
  }

  @Test
  void readShouldThrowNoSuchElementExceptionWhenBufferIsEmpty() {
    RingBuffer<Integer> buffer = new RingBuffer<>(3);
    assertThatThrownBy(buffer::read).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void writeShouldThrowIllegalStateExceptionWhenBufferIsFull() {
    RingBuffer<Integer> buffer = new RingBuffer<>(3);
    buffer.write(1);
    buffer.write(2);
    buffer.write(3);

    assertThat(buffer.isFull()).isTrue();
    assertThatThrownBy(() -> buffer.write(4)).isInstanceOf(IllegalStateException.class);
  }

  @Test
  void indicesShouldWrapAroundToTheBeginningOfTheArray() {
    RingBuffer<Integer> buffer = new RingBuffer<>(3);
    buffer.write(1);
    buffer.write(2);
    buffer.write(3);
    assertThat(buffer.read()).isEqualTo(1);
    assertThat(buffer.read()).isEqualTo(2);

    // The write index is at 0 again, the read index at 2
    buffer.write(4);
    buffer.write(5);

    assertThat(buffer.isFull()).isTrue();
    assertThat(buffer.read()).isEqualTo(3);
    assertThat(buffer.read()).isEqualTo(4);
    assertThat(buffer.read()).isEqualTo(5);
    assertThat(buffer.isEmpty()).isTrue();
  }

  @Test
  void sizeShouldFollowWritesAndReads() {
    RingBuffer<Integer> buffer = new RingBuffer<>(2);
    buffer.write(1);
    assertThat(buffer.size()).isEqualTo(1);
    buffer.write(2);
    assertThat(buffer.size()).isEqualTo(2);
    buffer.read();
    assertThat(buffer.size()).isEqualTo(1);
  }
}
