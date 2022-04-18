package eu.happycoders.demos.queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Element to be used in {@link DelayQueueExample}.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class DelayedElement<E extends Comparable<E>> implements Delayed {
  private final E e;
  private final long initialDelayMillis;
  private final long expiration;

  public DelayedElement(E e, long initialDelayMillis) {
    this.e = e;
    this.initialDelayMillis = initialDelayMillis;
    this.expiration = System.currentTimeMillis() + initialDelayMillis;
  }

  @Override
  public long getDelay(TimeUnit unit) {
    long remainingDelayMillis = expiration - System.currentTimeMillis();
    return unit.convert(remainingDelayMillis, TimeUnit.MILLISECONDS);
  }

  @Override
  public int compareTo(Delayed o) {
    DelayedElement<?> other = (DelayedElement<?>) o;
    return Long.compare(expiration, other.expiration);
  }

  @Override
  public String toString() {
    return "{%s, %dms}".formatted(e, initialDelayMillis);
  }
}
