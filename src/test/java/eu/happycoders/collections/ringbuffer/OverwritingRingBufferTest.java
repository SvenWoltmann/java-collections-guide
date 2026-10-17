package eu.happycoders.collections.ringbuffer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OverwritingRingBufferTest {

  @Test
  void writeToFullBufferShouldDiscardTheOldestElement() {
    RingBuffer<Integer> buffer = new OverwritingRingBuffer<>(3);
    buffer.write(1);
    buffer.write(2);
    buffer.write(3);

    buffer.write(4); // discards 1

    assertThat(buffer.size()).isEqualTo(3);
    assertThat(buffer.isFull()).isTrue();
    assertThat(buffer.read()).isEqualTo(2);
    assertThat(buffer.read()).isEqualTo(3);
    assertThat(buffer.read()).isEqualTo(4);
    assertThat(buffer.isEmpty()).isTrue();
  }

  @Test
  void bufferShouldAlwaysHoldTheMostRecentElements() {
    RingBuffer<Integer> buffer = new OverwritingRingBuffer<>(3);
    for (int i = 1; i <= 10; i++) {
      buffer.write(i);
    }

    assertThat(buffer.read()).isEqualTo(8);
    assertThat(buffer.read()).isEqualTo(9);
    assertThat(buffer.read()).isEqualTo(10);
  }

  @Test
  void writeShouldNotDiscardAnythingWhileTheBufferIsNotFull() {
    RingBuffer<Integer> buffer = new OverwritingRingBuffer<>(3);
    buffer.write(1);
    buffer.write(2);

    assertThat(buffer.size()).isEqualTo(2);
    assertThat(buffer.read()).isEqualTo(1);
  }
}
