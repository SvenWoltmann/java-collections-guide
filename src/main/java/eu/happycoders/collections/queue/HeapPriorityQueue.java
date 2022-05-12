package eu.happycoders.collections.queue;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * A priority queue of objects implemented using a binary heap stored in an array.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this stack
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class HeapPriorityQueue<E extends Comparable<? super E>> implements Queue<E> {

  public static final int MAX_SIZE = Integer.MAX_VALUE - 8;

  private static final int DEFAULT_INITIAL_CAPACITY = 10;
  private static final int ROOT_INDEX = 0;

  private Object[] elements;
  private int numberOfElements;

  /** Constructs an empty queue with an initial capacity sufficient to hold 10 elements. */
  public HeapPriorityQueue() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Constructs an empty queue with a capacity sufficient to hold the specified number of elements.
   *
   * @param capacity the capacity
   */
  public HeapPriorityQueue(int capacity) {
    if (capacity < 1) {
      throw new IllegalArgumentException("Capacity must be 1 or higher");
    }

    elements = new Object[capacity];
  }

  @Override
  public void enqueue(E newElement) {
    if (numberOfElements == elements.length) {
      grow();
    }
    siftUp(newElement);
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

  private void siftUp(E newElement) {
    int insertIndex = numberOfElements;

    while (isNotRoot(insertIndex) && isParentGreater(insertIndex, newElement)) {
      copyParentDownTo(insertIndex);
      insertIndex = parentOf(insertIndex);
    }

    elements[insertIndex] = newElement;
  }

  private boolean isNotRoot(int index) {
    return index != ROOT_INDEX;
  }

  private boolean isParentGreater(int insertIndex, E element) {
    int parentIndex = parentOf(insertIndex);
    E parent = elementAt(parentIndex);
    return parent.compareTo(element) > 0;
  }

  private void copyParentDownTo(int insertIndex) {
    int parentIndex = parentOf(insertIndex);
    elements[insertIndex] = elements[parentIndex];
  }

  private int parentOf(int index) {
    return (index - 1) / 2;
  }

  @Override
  public E dequeue() {
    E result = elementAtHead();
    E lastElement = removeLastElement();
    siftDown(lastElement);
    return result;
  }

  private E removeLastElement() {
    numberOfElements--;
    E lastElement = elementAt(numberOfElements);
    elements[numberOfElements] = null;
    return lastElement;
  }

  private void siftDown(E lastElement) {
    int lastElementInsertIndex = ROOT_INDEX;
    while (isGreaterThanAnyChild(lastElement, lastElementInsertIndex)) {
      moveSmallestChildUpTo(lastElementInsertIndex);
      lastElementInsertIndex = smallestChildOf(lastElementInsertIndex);
    }

    elements[lastElementInsertIndex] = lastElement;
  }

  private boolean isGreaterThanAnyChild(E element, int parentIndex) {
    E leftChild = leftChildOf(parentIndex);
    E rightChild = rightChildOf(parentIndex);

    return leftChild != null && element.compareTo(leftChild) > 0
        || rightChild != null && element.compareTo(rightChild) > 0;
  }

  private E leftChildOf(int parentIndex) {
    int leftChildIndex = leftChildIndexOf(parentIndex);
    return exists(leftChildIndex) ? elementAt(leftChildIndex) : null;
  }

  private int leftChildIndexOf(int parentIndex) {
    return 2 * parentIndex + 1;
  }

  private E rightChildOf(int parentIndex) {
    int rightChildIndex = rightChildIndexOf(parentIndex);
    return exists(rightChildIndex) ? elementAt(rightChildIndex) : null;
  }

  private int rightChildIndexOf(int parentIndex) {
    return 2 * parentIndex + 2;
  }

  private boolean exists(int index) {
    return index < numberOfElements;
  }

  private void moveSmallestChildUpTo(int parentIndex) {
    int smallestChildIndex = smallestChildOf(parentIndex);
    elements[parentIndex] = elements[smallestChildIndex];
  }

  private int smallestChildOf(int parentIndex) {
    int leftChildIndex = leftChildIndexOf(parentIndex);
    int rightChildIndex = rightChildIndexOf(parentIndex);

    if (!exists(rightChildIndex)) {
      return leftChildIndex;
    }

    return smallerOf(leftChildIndex, rightChildIndex);
  }

  private int smallerOf(int leftChildIndex, int rightChildIndex) {
    E leftChild = elementAt(leftChildIndex);
    E rightChild = elementAt(rightChildIndex);
    return leftChild.compareTo(rightChild) < 0 ? leftChildIndex : rightChildIndex;
  }

  @Override
  public E peek() {
    return elementAtHead();
  }

  private E elementAtHead() {
    E element = elementAt(0);
    if (element == null) {
      throw new NoSuchElementException();
    }
    return element;
  }

  private E elementAt(int child) {
    @SuppressWarnings("unchecked")
    E element = (E) elements[child];
    return element;
  }

  @Override
  public boolean isEmpty() {
    return numberOfElements == 0;
  }
}
