package eu.happycoders.collections.stack;

/**
 * Static utility methods that operate on {@link Stack}.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public final class Stacks {

  private Stacks() {}

  /**
   * Reverses the order of the elements in the given stack.
   *
   * @param stack the stack
   * @param <E> the type of elements held in the stack
   */
  public static <E> void reverse(Stack<E> stack) {
    if (stack.isEmpty()) {
      return;
    }
    E element = stack.pop();
    reverse(stack);
    insertAtBottom(stack, element);
  }

  private static <E> void insertAtBottom(Stack<E> stack, E element) {
    if (stack.isEmpty()) {
      stack.push(element);
    } else {
      E top = stack.pop();
      insertAtBottom(stack, element);
      stack.push(top);
    }
  }
}
