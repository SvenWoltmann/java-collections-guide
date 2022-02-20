package eu.happycoders.collections.stack;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * A last-in-first-out (LIFO) stack of objects implemented with an array.
 *
 * <p>The array grows as needed to accommodate additional elements; however, it never shrinks.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this stack
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class ArrayStack<E> implements Stack<E> {

  public static final int MAX_SIZE = Integer.MAX_VALUE - 8;

  private static final int DEFAULT_INITIAL_CAPACITY = 10;

  private Object[] elements;

  private int numberOfElements;

  /** Constructs an empty stack with an initial capacity sufficient to hold 10 elements. */
  public ArrayStack() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Constructs an empty stack with an initial capacity sufficient to hold the specified number of
   * elements.
   *
   * @param initialCapacity the initial capacity
   */
  public ArrayStack(int initialCapacity) {
    elements = new Object[initialCapacity];
  }

  @Override
  public void push(E item) {
    if (elements.length == numberOfElements) {
      grow();
    }
    elements[numberOfElements] = item;
    numberOfElements++;
  }

  private void grow() {
    int newCapacity = calculateNewCapacity(elements.length);
    elements = Arrays.copyOf(elements, newCapacity);
  }

  static int calculateNewCapacity(int currentCapacity) {
    if (currentCapacity == MAX_SIZE) {
      throw new IllegalStateException("Can't grow further");
    }

    int newCapacity = currentCapacity + calculateIncrement(currentCapacity);

    if (newCapacity > MAX_SIZE || newCapacity < 0 /* overflow */) {
      newCapacity = MAX_SIZE;
    }

    return newCapacity;
  }

  private static int calculateIncrement(int currentCapacity) {
    return currentCapacity < 64 ? currentCapacity : currentCapacity / 2;
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
    if (numberOfElements == 0) {
      throw new NoSuchElementException();
    }

    @SuppressWarnings("unchecked")
    E element = (E) elements[numberOfElements - 1];

    return element;
  }
}
