package eu.happycoders.collections.hashmap;

import java.util.HashMap;
import java.util.Map;

public class Ch8Removing {

  static void main() {
    Map<String, Integer> counts =
        new HashMap<>(Map.of("to", 2, "not", 1, "that", 1, "the", 1, "question", 1));

    System.out.println(counts.remove("that"));
    System.out.println(counts.remove("that"));
    System.out.println(counts.remove("to", 1));
    System.out.println(counts.remove("to", 2));
    System.out.println(counts);

    System.out.println("== null as key and as value");
    counts = new HashMap<>(Map.of("to", 2));
    counts.put("be", null);
    counts.put("not", null);
    counts.put(null, 1);

    System.out.println(counts.remove(null));
    System.out.println(counts.remove("be"));
    System.out.println(counts.remove("that"));
    System.out.println(counts.remove("not", null));
    System.out.println(counts.remove("that", null));
    System.out.println(counts);

    System.out.println("== Several at once");
    counts = new HashMap<>(Map.of("to", 2, "not", 1, "that", 1, "the", 1, "question", 1));
    counts.keySet().removeIf(word -> word.length() > 3);
    System.out.println(counts);
    counts.values().removeIf(count -> count == 1);
    System.out.println(counts);

    System.out.println("== By key and value");
    counts = new HashMap<>(Map.of("to", 2, "not", 1, "that", 1, "the", 1, "question", 1));
    counts.entrySet().removeIf(entry -> entry.getKey().length() <= 3 && entry.getValue() == 1);
    System.out.println(counts);

    System.out.println("== clear()");
    counts.clear();
    System.out.println(counts + " " + counts.size());
  }
}
