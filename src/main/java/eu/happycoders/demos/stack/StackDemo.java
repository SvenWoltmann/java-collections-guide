package eu.happycoders.demos.stack;

import eu.happycoders.collections.stack.ArrayDequeStack;
import eu.happycoders.collections.stack.ArrayStack;
import eu.happycoders.collections.stack.FixedArrayStack;
import eu.happycoders.collections.stack.LinkedListStack;
import eu.happycoders.collections.stack.Stack;

/**
 * Demo for the HappyCoders {@link Stack} interface and its implementations.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class StackDemo {
  public static void main(String[] args) {
    runDemo(new ArrayDequeStack<>());
    runDemo(new FixedArrayStack<>(10));
    runDemo(new ArrayStack<>());
    runDemo(new LinkedListStack<>());
  }

  private static void runDemo(Stack<Integer> stack) {
    System.out.println("---------- " + stack.getClass().getSimpleName() + " ----------");

    stack.push(1);
    stack.push(2);
    stack.push(3);

    System.out.println("stack.peek() = " + stack.peek());

    System.out.println("stack.pop() = " + stack.pop());
    System.out.println("stack.pop() = " + stack.pop());
    System.out.println("stack.pop() = " + stack.pop());

    try {
      System.out.println("stack.pop() = " + stack.pop());
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
    }
  }
}
