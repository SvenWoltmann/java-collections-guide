package eu.happycoders.collections.hashmap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Two threads put 100,000 different keys each into one map. The result for the {@link HashMap}
 * differs from run to run.
 */
public class Ch18Threads {

  private static final int KEYS_PER_THREAD = 100_000;

  static void main() throws InterruptedException {
    for (int run = 1; run <= 5; run++) {
      System.out.println("HashMap:           " + fill(new HashMap<>()));
    }
    System.out.println("ConcurrentHashMap: " + fill(new ConcurrentHashMap<>()));
  }

  private static String fill(Map<Integer, Integer> map) throws InterruptedException {
    Thread first = Thread.ofPlatform().start(() -> putRange(map, 0));
    Thread second = Thread.ofPlatform().start(() -> putRange(map, KEYS_PER_THREAD));
    first.join();
    second.join();
    int found = 0;
    for (int i = 0; i < 2 * KEYS_PER_THREAD; i++) {
      if (map.containsKey(i)) {
        found++;
      }
    }
    return "size " + map.size() + ", keys found " + found;
  }

  private static void putRange(Map<Integer, Integer> map, int start) {
    try {
      for (int i = start; i < start + KEYS_PER_THREAD; i++) {
        map.put(i, i);
      }
    } catch (RuntimeException e) {
      System.out.println(Thread.currentThread() + ": " + e);
    }
  }
}
