package eu.happycoders.collections.hashmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ch10Iterating {

  static void main() {
    Map<String, Integer> counts = new HashMap<>(Map.of("to", 2, "not", 1, "that", 1));

    for (Map.Entry<String, Integer> entry : counts.entrySet()) {
      System.out.println(entry.getKey() + " = " + entry.getValue());
    }

    for (String word : counts.keySet()) {
      System.out.print(word + " ");
    }
    System.out.println();

    int total = 0;
    for (int count : counts.values()) {
      total += count;
    }
    System.out.println(total);

    counts.forEach((word, count) -> System.out.println(word + ": " + count));

    System.out.println("== Stream");
    List<String> frequent =
        counts.entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .map(Map.Entry::getKey)
            .toList();
    System.out.println(frequent);

    System.out.println("== Views");
    counts.keySet().remove("that");
    System.out.println(counts);
  }
}
