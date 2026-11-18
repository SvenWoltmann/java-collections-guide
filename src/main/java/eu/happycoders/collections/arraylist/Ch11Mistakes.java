package eu.happycoders.collections.arraylist;

import java.util.ArrayList;
import java.util.List;

public class Ch11Mistakes {

  static void main() {
    System.out.println("== Capacity is not size");
    List<String> names = new ArrayList<>(10);
    try {
      names.set(0, "Ada");
    } catch (IndexOutOfBoundsException e) {
      System.out.println(e);
    }

    System.out.println("== Comparing Integer elements with ==");
    List<Integer> small = new ArrayList<>(List.of(127, 127));
    List<Integer> large = new ArrayList<>(List.of(128, 128));
    System.out.println(small.get(0) == small.get(1));
    System.out.println(large.get(0) == large.get(1));
    System.out.println(large.get(0).equals(large.get(1)));

    System.out.println("== contains() without equals()");
    List<Point> points = new ArrayList<>(List.of(new Point(1, 2)));
    System.out.println(points.contains(new Point(1, 2)));
    List<PointRecord> records = new ArrayList<>(List.of(new PointRecord(1, 2)));
    System.out.println(records.contains(new PointRecord(1, 2)));
  }

  static class Point {
    final int x;
    final int y;

    Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  record PointRecord(int x, int y) {}
}
