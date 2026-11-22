package eu.happycoders.collections.hashmap;

import java.util.HashMap;
import java.util.Map;

public class Intro {

  static void main() {
    Map<String, Integer> counts = new HashMap<>();
    counts.put("to", 2);
    counts.put("be", 2);
    counts.put("or", 1);

    System.out.println(counts.get("be"));
    System.out.println(counts.size());
    System.out.println(counts);
  }
}
