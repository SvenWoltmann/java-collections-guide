package eu.happycoders.collections.hashmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ch9Counting {

  private static final String TEXT = "to be or not to be that is the question";

  static void main() {
    String[] words = TEXT.split(" ");

    System.out.println("== containsKey() and put()");
    Map<String, Integer> counts = new HashMap<>();
    for (String word : words) {
      if (counts.containsKey(word)) {
        counts.put(word, counts.get(word) + 1);
      } else {
        counts.put(word, 1);
      }
    }
    System.out.println(counts);

    System.out.println("== getOrDefault()");
    counts = new HashMap<>();
    for (String word : words) {
      counts.put(word, counts.getOrDefault(word, 0) + 1);
    }
    System.out.println(counts);

    System.out.println("== merge()");
    counts = new HashMap<>();
    for (String word : words) {
      counts.merge(word, 1, Integer::sum);
    }
    System.out.println(counts);

    System.out.println("== computeIfAbsent()");
    Map<Character, List<String>> byFirstLetter = new HashMap<>();
    for (String word : words) {
      byFirstLetter.computeIfAbsent(word.charAt(0), letter -> new ArrayList<>()).add(word);
    }
    System.out.println(byFirstLetter);

    System.out.println("== compute() and computeIfPresent()");
    counts = new HashMap<>(Map.of("to", 2, "not", 1, "that", 1));
    counts.compute("to", (word, count) -> count == null ? 1 : count + 1);
    counts.compute("the", (word, count) -> count == null ? 1 : count + 1);
    counts.computeIfPresent("not", (word, count) -> count > 1 ? count - 1 : null);
    System.out.println(counts);
  }
}
