package eu.happycoders.collections.stack;

import java.util.NoSuchElementException;

/**
 * A fixed-size last-in-first-out (LIFO) stack of objects implemented with an array.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this stack
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class FixedArrayStack<E> implements Stack<E> {

  private final Object[] elements;

  private int numberOfElements;

  /**
   * Constructs an empty stack with a capacity sufficient to hold the specified number of elements.
   *
   * @param capacity the capacity
   */
  public FixedArrayStack(int capacity) {
    if (capacity < 1) {
      throw new IllegalArgumentException("Capacity must be 1 or higher");
    }

    elements = new Object[capacity];
  }

  @Override
  public void push(E item) {
    if (numberOfElements == elements.length) {
      throw new IllegalStateException("The stack is full");
    }
    elements[numberOfElements] = item;
    numberOfElements++;
  }

  @Override
  public E pop() {
    E element = elementAtTop();
    elements[numberOfElements - 1] = null;
    numberOfElements--;
    return element;
  }

  @Override
  public E peek() {
    return elementAtTop();
  }

  private E elementAtTop() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }

    @SuppressWarnings("unchecked")
    E element = (E) elements[numberOfElements - 1];

    return element;
  }

  @Override
  public boolean isEmpty() {
    return numberOfElements == 0;
  }
}
