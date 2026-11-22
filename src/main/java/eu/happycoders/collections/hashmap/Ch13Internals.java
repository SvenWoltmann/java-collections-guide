package eu.happycoders.collections.hashmap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;

/**
 * Makes the table of a {@link HashMap} visible. Reads the private field {@code table} and therefore
 * needs {@code --add-opens java.base/java.util=ALL-UNNAMED}. For demonstration only.
 */
public class Ch13Internals {

  private static final Field TABLE;

  static {
    try {
      TABLE = HashMap.class.getDeclaredField("table");
      TABLE.setAccessible(true);
    } catch (NoSuchFieldException e) {
      throw new IllegalStateException(e);
    }
  }

  static Object[] table(HashMap<?, ?> map) {
    try {
      return (Object[]) TABLE.get(map);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  static int tableLength(HashMap<?, ?> map) {
    Object[] table = table(map);
    return table == null ? 0 : table.length;
  }

  /** A key that shares its hash code with all other keys of its class. */
  record CollidingKey(int id) {
    @Override
    public int hashCode() {
      return 42;
    }
  }

  static void main() {
    System.out.println("== From the key to the bucket");
    for (String key : new String[] {"be", "or", "question"}) {
      int h = key.hashCode();
      int hash = h ^ (h >>> 16);
      System.out.printf(
          Locale.ROOT,
          "%-8s hashCode %32s%n         hash     %32s%n         index    %d (without the fold %d)%n",
          key,
          Integer.toBinaryString(h),
          Integer.toBinaryString(hash),
          hash & 15,
          h & 15);
    }

    System.out.println("== Buckets of the first line");
    HashMap<String, Integer> counts = new HashMap<>();
    for (String word : "to be or not to be that is the question".split(" ")) {
      counts.put(word, counts.getOrDefault(word, 0) + 1);
    }
    printBuckets(counts);

    System.out.println("== Growth");
    HashMap<Integer, Integer> map = new HashMap<>();
    System.out.println("size 0, table " + tableLength(map));
    int lastLength = tableLength(map);
    for (int i = 1; i <= 100; i++) {
      map.put(i, i);
      if (tableLength(map) != lastLength) {
        lastLength = tableLength(map);
        System.out.println("size " + map.size() + ", table " + lastLength);
      }
    }

    System.out.println("== new HashMap<>(100) and HashMap.newHashMap(100)");
    fillAndTrack("new HashMap<>(100)", new HashMap<>(100));
    fillAndTrack("HashMap.newHashMap(100)", HashMap.newHashMap(100));

    System.out.println("== The table does not shrink");
    map.clear();
    System.out.println("size " + map.size() + ", table " + tableLength(map));

    System.out.println("== Treeification");
    HashMap<CollidingKey, Integer> colliding = new HashMap<>();
    for (int i = 1; i <= 11; i++) {
      colliding.put(new CollidingKey(i), i);
      Object[] table = table(colliding);
      Object bin = table[42 & (table.length - 1)];
      System.out.println(
          "size " + colliding.size() + ", table " + table.length + ", " + bin.getClass().getName());
    }
  }

  private static void fillAndTrack(String label, HashMap<Integer, Integer> map) {
    System.out.println(label + ": size 0, table " + tableLength(map));
    int lastLength = tableLength(map);
    for (int i = 1; i <= 100; i++) {
      map.put(i, i);
      if (tableLength(map) != lastLength) {
        lastLength = tableLength(map);
        System.out.println(label + ": size " + map.size() + ", table " + lastLength);
      }
    }
  }

  private static void printBuckets(HashMap<String, Integer> map) {
    Object[] table = table(map);
    for (int i = 0; i < table.length; i++) {
      if (table[i] != null) {
        System.out.println(i + ": " + table[i] + nextNodes(table[i]));
      }
    }
  }

  private static String nextNodes(Object node) {
    try {
      Field next = node.getClass().getDeclaredField("next");
      next.setAccessible(true);
      StringBuilder result = new StringBuilder();
      for (Object n = next.get(node); n != null; n = next.get(n)) {
        result.append(" -> ").append(n);
      }
      return result.toString();
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
  }
}
