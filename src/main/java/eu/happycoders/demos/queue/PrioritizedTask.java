package eu.happycoders.demos.queue;

/**
 * A task with a priority for {@link PriorityThreadPoolExample} and {@link
 * PriorityThreadPoolSubmitExample}; the lower the number, the higher the priority.
 *
 * @author <a href="sven@happycoders.eu">Sven Woltmann</a>
 */
public record PrioritizedTask(int priority, String name)
    implements Runnable, Comparable<PrioritizedTask> {

  @Override
  public void run() {
    IO.println("Running task " + name + " (priority " + priority + ")");
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  @Override
  public int compareTo(PrioritizedTask other) {
    return Integer.compare(priority, other.priority);
  }
}
