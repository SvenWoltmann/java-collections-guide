package eu.happycoders.collections.hashmap;

import java.util.HashMap;
import java.util.Map;

public class Ch19Mistakes {

  static void main() {
    Map<String, Integer> counts = new HashMap<>(Map.of("to", 2, "not", 1));
    try {
      int count = counts.get("that");
      System.out.println(count);
    } catch (NullPointerException e) {
      System.out.println(e.getMessage());
    }
  }
}
