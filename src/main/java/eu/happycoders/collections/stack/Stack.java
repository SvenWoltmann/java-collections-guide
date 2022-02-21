package eu.happycoders.collections.stack;

import java.util.NoSuchElementException;

/**
 * A last-in-first-out (LIFO) stack of objects.
 *
 * @param <E> the type of elements held in this stack
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public interface Stack<E> {

  /**
   * Pushes an element onto the top of the stack.
   *
   * @param element the element to push on the stack
   */
  void push(E element);

  /**
   * Removes the element from the top of the stack.
   *
   * @return the element from the top of the stack
   * @throws NoSuchElementException if the stack is empty
   */
  E pop();

  /**
   * Returns the element from the top of the stack without removing it.
   *
   * @return the element from the top of the stack
   * @throws NoSuchElementException if the stack is empty
   */
  E peek();

  /**
   * Returns {@code true} if this stack contains no elements.
   *
   * @return {@code true} if this stack contains no elements
   */
  boolean isEmpty();
}
