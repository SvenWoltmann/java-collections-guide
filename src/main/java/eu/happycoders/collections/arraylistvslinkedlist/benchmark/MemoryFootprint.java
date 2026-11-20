package eu.happycoders.collections.arraylistvslinkedlist.benchmark;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.management.ObjectName;

/**
 * Not JMH: measures how much heap an {@link ArrayList} and a {@link LinkedList} with 1,000,000
 * {@code Integer} elements occupy, by class. Takes a class histogram - the same as {@code jcmd
 * <pid> GC.class_histogram}, which runs a full garbage collection first - before and after creating
 * each list and prints the difference.
 *
 * <p>The values start at 1,000, outside the cache of {@code Integer.valueOf()}, so every element is
 * an object of its own. The {@code ArrayList} grows from the default capacity, as it does without a
 * constructor argument.
 *
 * <p>Run it once with the default settings and once with {@code -XX:-UseCompactObjectHeaders}:
 *
 * <pre>
 * java -cp target/benchmarks.jar \
 *     eu.happycoders.collections.arraylistvslinkedlist.benchmark.MemoryFootprint
 * </pre>
 */
public class MemoryFootprint {

  private static final int SIZE = 1_000_000;

  /**
   * The classes whose objects make up the lists. The histograms themselves allocate strings, maps,
   * and filler objects; those are left out.
   */
  private static final List<String> LIST_CLASSES =
      List.of(
          "java.util.ArrayList",
          "[Ljava.lang.Object;",
          "java.util.LinkedList",
          "java.util.LinkedList$Node",
          "java.lang.Integer");

  private record Entry(long instances, long bytes) {}

  private static final Entry NONE = new Entry(0, 0);

  // Kept in fields so that the lists stay reachable during the histograms.
  private static List<Integer> arrayList;
  private static List<Integer> linkedList;

  static void main() throws Exception {
    Map<String, Entry> before = histogram();
    arrayList = new ArrayList<>();
    fill(arrayList);
    Map<String, Entry> afterArrayList = histogram();
    linkedList = new LinkedList<>();
    fill(linkedList);
    Map<String, Entry> afterLinkedList = histogram();

    System.out.println(
        "JDK "
            + Runtime.version()
            + ", "
            + String.join(" ", ManagementFactory.getRuntimeMXBean().getInputArguments()));
    printDifference("ArrayList", before, afterArrayList);
    printDifference("LinkedList", afterArrayList, afterLinkedList);
    System.out.println("(sizes: " + arrayList.size() + ", " + linkedList.size() + ")");
  }

  private static void fill(List<Integer> list) {
    for (int i = 0; i < SIZE; i++) {
      list.add(1_000 + i);
    }
  }

  private static Map<String, Entry> histogram() throws Exception {
    String output =
        (String)
            ManagementFactory.getPlatformMBeanServer()
                .invoke(
                    new ObjectName("com.sun.management:type=DiagnosticCommand"),
                    "gcClassHistogram",
                    new Object[] {new String[0]},
                    new String[] {String[].class.getName()});

    Map<String, Entry> entries = new HashMap<>();
    for (String line : output.lines().toList()) {
      String[] fields = line.trim().split("\\s+");
      if (fields.length >= 4 && fields[0].endsWith(":")) {
        entries.put(fields[3], new Entry(Long.parseLong(fields[1]), Long.parseLong(fields[2])));
      }
    }
    return entries;
  }

  private static void printDifference(
      String label, Map<String, Entry> before, Map<String, Entry> after) {
    System.out.println("== " + label);
    long totalBytes = 0;
    for (String className : LIST_CLASSES) {
      Entry old = before.getOrDefault(className, NONE);
      Entry now = after.getOrDefault(className, NONE);
      long instances = now.instances() - old.instances();
      long bytes = now.bytes() - old.bytes();
      if (instances != 0) {
        System.out.printf(
            Locale.ROOT, "%-25s %,10d instances %,12d bytes%n", className, instances, bytes);
        totalBytes += bytes;
      }
    }
    System.out.printf(
        Locale.ROOT,
        "%-25s %,33d bytes = %.1f bytes per element%n",
        "total",
        totalBytes,
        (double) totalBytes / SIZE);
  }
}
