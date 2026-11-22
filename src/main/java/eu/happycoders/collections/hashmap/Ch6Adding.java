package eu.happycoders.collections.hashmap;

import java.util.HashMap;
import java.util.Map;

public class Ch6Adding {

  static void main() {
    Map<String, Integer> counts = new HashMap<>();
    System.out.println(counts.put("to", 1));
    System.out.println(counts.put("to", 2));
    System.out.println(counts);

    System.out.println(counts.putIfAbsent("to", 5));
    System.out.println(counts.putIfAbsent("be", 2));
    System.out.println(counts);

    counts.putAll(Map.of("or", 1, "not", 1));
    System.out.println(counts);

    System.out.println(counts.replace("or", 3));
    System.out.println(counts.replace("that", 1));
    System.out.println(counts);
  }
}
