package eu.happycoders.collections.stack;

import java.util.NoSuchElementException;

/**
 * A last-in-first-out (LIFO) stack of objects implemented as a singly linked list.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this stack
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class LinkedListStack<E> implements Stack<E> {

  private Node<E> top;

  @Override
  public void push(E element) {
    top = new Node<>(element, top);
  }

  @Override
  public E pop() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    E element = top.element;
    top = top.next;
    return element;
  }

  @Override
  public E peek() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return top.element;
  }

  @Override
  public boolean isEmpty() {
    return top == null;
  }

  private static class Node<E> {
    final E element;
    final Node<E> next;

    Node(E element, Node<E> next) {
      this.element = element;
      this.next = next;
    }
  }
}
