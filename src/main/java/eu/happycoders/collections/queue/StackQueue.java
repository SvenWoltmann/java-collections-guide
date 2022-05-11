package eu.happycoders.collections.queue;

import eu.happycoders.collections.stack.ArrayStack;
import eu.happycoders.collections.stack.Stack;

/**
 * A last-in-first-out (LIFO) stack of objects implemented using two first-in-first-out (FIFO)
 * queues.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this stack
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class StackQueue<E> implements Queue<E> {

  private final Stack<E> stack = new ArrayStack<>();

  @Override
  public void enqueue(E element) {
    // 1. Move elements from main stack to a temporary stack
    Stack<E> temporaryStack = new ArrayStack<>();
    while (!stack.isEmpty()) {
      temporaryStack.push(stack.pop());
    }

    // 2. Push new element on the main stack
    stack.push(element);

    // 3. Move elements back from temporary stack to main stack
    while (!temporaryStack.isEmpty()) {
      stack.push(temporaryStack.pop());
    }
  }

  @Override
  public E dequeue() {
    return stack.pop();
  }

  @Override
  public E peek() {
    return stack.peek();
  }

  @Override
  public boolean isEmpty() {
    return stack.isEmpty();
  }
}
