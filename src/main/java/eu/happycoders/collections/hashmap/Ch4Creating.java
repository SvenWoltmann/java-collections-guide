package eu.happycoders.collections.hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Creating a {@link HashMap}. The maps created from {@code Map.of()} contain no two keys that share
 * a bucket: {@code Map.of()} hands out its entries in an order that can change from one program to
 * the next, and for keys in the same bucket that order would decide the printed order.
 */
public class Ch4Creating {

  static void main() {
    System.out.println("== With entries");
    Map<String, Integer> counts = new HashMap<>(Map.of("to", 2, "not", 1, "that", 1));
    counts.put("the", 1);
    System.out.println(counts);

    System.out.println("== Map.of() alone");
    Map<String, Integer> fixed = Map.of("to", 2, "not", 1, "that", 1);
    try {
      fixed.put("the", 1);
    } catch (UnsupportedOperationException e) {
      System.out.println("put(): " + e);
    }
    try {
      Map.of("to", 2, "not", null);
    } catch (NullPointerException e) {
      System.out.println("null value: " + e);
    }
    try {
      Map.of("to", 2, "to", 1);
    } catch (IllegalArgumentException e) {
      System.out.println("duplicate key: " + e);
    }

    System.out.println("== Map.ofEntries()");
    Map<String, Integer> many =
        new HashMap<>(
            Map.ofEntries(
                Map.entry("to", 2),
                Map.entry("not", 1),
                Map.entry("that", 1),
                Map.entry("the", 1)));
    System.out.println(many);

    System.out.println("== Copy");
    Map<String, Integer> original = new HashMap<>(Map.of("to", 2, "not", 1, "that", 1));
    Map<String, Integer> copy = new HashMap<>(original);
    copy.remove("not");
    System.out.println(original);
    System.out.println(copy);

    System.out.println("== Numbers");
    Map<String, Integer> numbers = new HashMap<>(Map.of("to", 2, "not", 1));
    int sum = numbers.get("to") + numbers.get("not");
    System.out.println(sum);
  }
}
