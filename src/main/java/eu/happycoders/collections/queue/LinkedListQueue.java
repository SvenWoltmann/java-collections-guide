package eu.happycoders.collections.queue;

import java.util.NoSuchElementException;

/**
 * A last-in-first-out (LIFO) queue of objects implemented as a singly linked list.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this stack
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class LinkedListQueue<E> implements Queue<E> {

  private Node<E> head;
  private Node<E> tail;

  @Override
  public void enqueue(E element) {
    Node<E> newNode = new Node<>(element);
    if (isEmpty()) {
      head = tail = newNode;
    } else {
      tail.next = newNode;
      tail = newNode;
    }
  }

  @Override
  public E dequeue() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    E element = head.element;
    head = head.next;
    if (head == null) {
      tail = null;
    }
    return element;
  }

  @Override
  public E peek() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return head.element;
  }

  @Override
  public boolean isEmpty() {
    return head == null;
  }

  private static class Node<E> {
    final E element;
    Node<E> next;

    Node(E element) {
      this.element = element;
    }
  }
}
