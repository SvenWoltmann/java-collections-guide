package eu.happycoders.collections.hashmap;

import java.util.HashMap;
import java.util.Map;

public class Ch7Reading {

  static void main() {
    Map<String, Integer> counts = new HashMap<>(Map.of("to", 2, "be", 2));
    counts.put("not", null);

    System.out.println(counts.get("be"));
    System.out.println(counts.get("that"));
    System.out.println(counts.get("not"));
    System.out.println(counts.containsKey("that") + " " + counts.containsKey("not"));

    System.out.println("== getOrDefault()");
    counts = new HashMap<>(Map.of("to", 2, "be", 2));
    counts.put("not", null);

    System.out.println(counts.getOrDefault("that", 0));
    System.out.println(counts.getOrDefault("not", 0));

    System.out.println("== containsKey(), containsValue(), size(), isEmpty()");
    counts = new HashMap<>(Map.of("to", 2, "be", 2, "not", 1));

    System.out.println(counts.containsKey("be"));
    System.out.println(counts.containsValue(2));
    System.out.println(counts.size() + " " + counts.isEmpty());
  }
}
