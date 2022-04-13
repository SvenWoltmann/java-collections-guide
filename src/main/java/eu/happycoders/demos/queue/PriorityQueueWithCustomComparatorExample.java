package eu.happycoders.demos.queue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Demo showing the order in which elements are taken from a {@link PriorityQueue} using a custom
 * comparator.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public class PriorityQueueWithCustomComparatorExample {
  public static void main(String[] args) {
    Comparator<Task> comparator = Comparator.comparingInt(Task::priority);
    Queue<Task> queue = new PriorityQueue<>(comparator);

    // Enqueue tasks with random priorities
    for (int i = 0; i < 5; i++) {
      String name = "Task " + (i + 1);
      int priority = ThreadLocalRandom.current().nextInt(10, 100);
      Task task = new Task(name, priority);
      queue.offer(task);
      System.out.printf("queue.offer(%s)    -->  queue = %s%n", task, queue);
    }

    // Dequeue all elements
    while (!queue.isEmpty()) {
      System.out.printf("queue.poll() = %s  -->  queue = %s%n", queue.poll(), queue);
    }
  }

  private record Task(String name, int priority) {
    @Override
    public String toString() {
      return name + " (prio " + priority + ")";
    }
  }
}
