package eu.happycoders.collections.queue;

import eu.happycoders.collections.stack.ArrayStack;
import eu.happycoders.collections.stack.Stack;

/**
 * A first-in-first-out (FIFO) queue of objects implemented using two last-in-first-out (LIFO)
 * stacks: new elements are pushed onto the input stack; elements are taken from the output stack,
 * which is refilled from the input stack only when it is empty. Each element is pushed and popped
 * at most twice, so all operations run in amortized constant time.
 *
 * <p>This implementation is not thread-safe.
 *
 * @param <E> the type of elements held in this queue
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class TwoStackQueue<E> implements Queue<E> {

  private final Stack<E> inputStack = new ArrayStack<>();
  private final Stack<E> outputStack = new ArrayStack<>();

  @Override
  public void enqueue(E element) {
    inputStack.push(element);
  }

  @Override
  public E dequeue() {
    refillOutputStackIfEmpty();
    return outputStack.pop();
  }

  @Override
  public E peek() {
    refillOutputStackIfEmpty();
    return outputStack.peek();
  }

  @Override
  public boolean isEmpty() {
    return inputStack.isEmpty() && outputStack.isEmpty();
  }

  private void refillOutputStackIfEmpty() {
    if (outputStack.isEmpty()) {
      while (!inputStack.isEmpty()) {
        outputStack.push(inputStack.pop());
      }
    }
  }
}
