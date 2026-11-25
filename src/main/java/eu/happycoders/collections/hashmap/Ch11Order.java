package eu.happycoders.collections.hashmap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Ch11Order {

  private static final String LINE_1 = "to be or not to be that is the question";
  private static final String LINE_2 = "whether 'tis nobler in the mind to suffer";

  static void main() {
    System.out.println("== HashMap");
    Map<String, Integer> counts = new HashMap<>();
    count(LINE_1, counts);
    System.out.println(counts);
    count(LINE_2, counts);
    System.out.println(counts);

    System.out.println("== LinkedHashMap");
    Map<String, Integer> linked = new LinkedHashMap<>();
    count(LINE_1, linked);
    System.out.println(linked);

    System.out.println("== TreeMap");
    Map<String, Integer> sorted = new TreeMap<>(linked);
    System.out.println(sorted);

    System.out.println("== Sorted by value");
    Map<String, Integer> byCount =
        counts.entrySet().stream()
            .sorted(
                Map.Entry.<String, Integer>comparingByValue()
                    .reversed()
                    .thenComparing(Map.Entry.comparingByKey()))
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (first, second) -> first,
                    LinkedHashMap::new));
    System.out.println(byCount);
  }

  private static void count(String line, Map<String, Integer> counts) {
    for (String word : line.split(" ")) {
      counts.merge(word, 1, Integer::sum);
    }
  }
}
