package eu.happycoders.demos.stack;

import java.util.Stack;

/**
 * Demo for Java's {@link Stack} class.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class JavaStackDemo {
  public static void main(String[] args) {
    Stack<String> stack = new Stack<>();

    stack.push("apple");
    stack.push("orange");
    stack.push("pear");

    System.out.println("stack = " + stack);

    System.out.println("stack.peek() = " + stack.peek());
    System.out.println("stack.empty() = " + stack.empty());
    System.out.println("stack.search(\"apple\") = " + stack.search("apple"));

    System.out.println("stack.pop() = " + stack.pop());
    System.out.println("stack.pop() = " + stack.pop());
    System.out.println("stack.pop() = " + stack.pop());

    System.out.println("stack.pop() = " + stack.pop());
  }
}
