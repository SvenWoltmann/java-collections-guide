package eu.happycoders.demos.stack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Demo for using Java's {@link ArrayDeque} as a stack.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class ArrayDequeDemo {
  public static void main(String[] args) {
    Deque<String> stack = new ArrayDeque<>();

    stack.push("apple");
    stack.push("orange");
    stack.push("pear");

    System.out.println("stack = " + stack);

    System.out.println("stack.peek() = " + stack.peek());
    System.out.println("stack.isEmpty() = " + stack.isEmpty());

    System.out.println("stack.pop() = " + stack.pop());
    System.out.println("stack.pop() = " + stack.pop());
    System.out.println("stack.pop() = " + stack.pop());

    System.out.println("stack.pop() = " + stack.pop());
  }
}
