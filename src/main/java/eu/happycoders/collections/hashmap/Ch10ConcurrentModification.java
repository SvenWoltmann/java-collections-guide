package eu.happycoders.collections.hashmap;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Ch10ConcurrentModification {

  static void main() {
    System.out.println("== remove() in a for-each loop");
    Map<String, Integer> counts = createCounts();
    try {
      for (String word : counts.keySet()) {
        if (counts.get(word) == 1) {
          counts.remove(word);
        }
      }
    } catch (ConcurrentModificationException e) {
      System.out.println(e);
    }
    System.out.println(counts);

    System.out.println("== removeIf()");
    counts = createCounts();
    counts.values().removeIf(count -> count == 1);
    System.out.println(counts);

    System.out.println("== Iterator.remove()");
    counts = createCounts();
    Iterator<Integer> iterator = counts.values().iterator();
    while (iterator.hasNext()) {
      if (iterator.next() == 1) {
        iterator.remove();
      }
    }
    System.out.println(counts);

    System.out.println("== setValue() and replaceAll()");
    counts = createCounts();
    for (Map.Entry<String, Integer> entry : counts.entrySet()) {
      entry.setValue(entry.getValue() * 10);
    }
    System.out.println(counts);
    counts.replaceAll((word, count) -> count / 10);
    System.out.println(counts);
  }

  private static Map<String, Integer> createCounts() {
    return new HashMap<>(Map.of("to", 2, "not", 1, "that", 1, "the", 1));
  }
}
