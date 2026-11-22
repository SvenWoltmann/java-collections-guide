package eu.happycoders.collections.hashmap.benchmark;

import eu.happycoders.collections.hashmap.benchmark.TreeificationBenchmark.KeyType;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.management.ObjectName;

/**
 * Not JMH: measures how much heap a {@link HashMap} with 1,000,000 entries occupies, by class -
 * once with keys that each have a bucket to themselves, so that every entry is a {@code
 * HashMap.Node}, and once with keys that all share one hash code, so that every entry is a {@code
 * HashMap.TreeNode}. Takes a class histogram - the same as {@code jcmd <pid> GC.class_histogram},
 * which runs a full garbage collection first - before and after creating each map and prints the
 * difference.
 *
 * <p>The keys and values are left out of the sum: they would occupy the same space in any other
 * collection. The values start at 1,000, outside the cache of {@code Integer.valueOf()}.
 *
 * <p>Run it once with the default settings and once with {@code -XX:-UseCompactObjectHeaders}:
 *
 * <pre>
 * java -cp target/benchmarks.jar eu.happycoders.collections.hashmap.benchmark.MemoryFootprint
 * </pre>
 */
public class MemoryFootprint {

  private static final int SIZE = 1_000_000;

  /**
   * The classes whose objects make up the maps, without keys and values. The histograms themselves
   * allocate strings, maps, and filler objects; a {@code HashMap} of their own shows up as one
   * additional instance at most.
   */
  private static final List<String> MAP_CLASSES =
      List.of(
          "java.util.HashMap",
          "[Ljava.util.HashMap$Node;",
          "java.util.HashMap$Node",
          "java.util.HashMap$TreeNode");

  private record Entry(long instances, long bytes) {}

  private static final Entry NONE = new Entry(0, 0);

  // Kept in fields so that the maps stay reachable during the histograms.
  private static Map<Object, Integer> nodeMap;
  private static Map<Object, Integer> treeNodeMap;

  static void main() throws Exception {
    Map<String, Entry> before = histogram();
    nodeMap = new HashMap<>();
    fill(nodeMap, KeyType.DISTINCT);
    Map<String, Entry> afterNodeMap = histogram();
    treeNodeMap = new HashMap<>();
    fill(treeNodeMap, KeyType.COLLIDING_COMPARABLE);
    Map<String, Entry> afterTreeNodeMap = histogram();

    System.out.println(
        "JDK "
            + Runtime.version()
            + ", "
            + String.join(" ", ManagementFactory.getRuntimeMXBean().getInputArguments()));
    printDifference("HashMap, one bucket per key", before, afterNodeMap);
    printDifference("HashMap, all keys in one bucket", afterNodeMap, afterTreeNodeMap);
    System.out.println("(sizes: " + nodeMap.size() + ", " + treeNodeMap.size() + ")");
  }

  private static void fill(Map<Object, Integer> map, KeyType keyType) {
    for (int i = 0; i < SIZE; i++) {
      map.put(keyType.createKey(i), 1_000 + i);
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
    for (String className : MAP_CLASSES) {
      Entry old = before.getOrDefault(className, NONE);
      Entry now = after.getOrDefault(className, NONE);
      long instances = now.instances() - old.instances();
      long bytes = now.bytes() - old.bytes();
      if (instances != 0) {
        System.out.printf(
            Locale.ROOT, "%-27s %,10d instances %,12d bytes%n", className, instances, bytes);
        totalBytes += bytes;
      }
    }
    System.out.printf(
        Locale.ROOT,
        "%-27s %,33d bytes = %.1f bytes per entry%n",
        "total",
        totalBytes,
        (double) totalBytes / SIZE);
  }
}
