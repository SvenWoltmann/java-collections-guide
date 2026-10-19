package eu.happycoders.demos.queue;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Element to be used in {@link DelayQueueExample}.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class DelayedElement<E> implements Delayed {
  private final E e;
  private final long initialDelayMillis;
  private final long expirationNanos;

  public DelayedElement(E e, long initialDelayMillis) {
    this.e = e;
    this.initialDelayMillis = initialDelayMillis;
    this.expirationNanos = System.nanoTime() + MILLISECONDS.toNanos(initialDelayMillis);
  }

  @Override
  public long getDelay(TimeUnit unit) {
    long remainingNanos = expirationNanos - System.nanoTime();
    return unit.convert(remainingNanos, NANOSECONDS);
  }

  @Override
  public int compareTo(Delayed o) {
    DelayedElement<?> other = (DelayedElement<?>) o;
    // Compare the difference, not the values: System.nanoTime() may overflow
    return Long.signum(expirationNanos - other.expirationNanos);
  }

  @Override
  public String toString() {
    return "{%s, %dms}".formatted(e, initialDelayMillis);
  }
}
