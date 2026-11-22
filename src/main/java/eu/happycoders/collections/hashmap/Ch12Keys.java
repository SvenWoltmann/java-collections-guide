package eu.happycoders.collections.hashmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ch12Keys {

  /** A key class without {@code equals()} and {@code hashCode()}. */
  static class Point {
    final int x;
    final int y;

    Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  /** The same key as a record, which brings {@code equals()} and {@code hashCode()}. */
  record PointRecord(int x, int y) {}

  static void main() {
    System.out.println("== Class without equals() and hashCode()");
    Map<Point, String> labels = new HashMap<>();
    labels.put(new Point(1, 2), "start");
    System.out.println(labels.get(new Point(1, 2)));

    Map<PointRecord, String> recordLabels = new HashMap<>();
    recordLabels.put(new PointRecord(1, 2), "start");
    System.out.println(recordLabels.get(new PointRecord(1, 2)));

    System.out.println("== Key changed after put()");
    List<String> phrase = new ArrayList<>(List.of("to", "be"));
    Map<List<String>, Integer> phrases = new HashMap<>();
    phrases.put(phrase, 1);
    phrase.add("or");
    System.out.println(phrases.get(phrase));
    System.out.println(phrases.get(List.of("to", "be")));
    System.out.println(phrases.containsKey(phrase));
    System.out.println(phrases + " " + phrases.size());
  }
}
