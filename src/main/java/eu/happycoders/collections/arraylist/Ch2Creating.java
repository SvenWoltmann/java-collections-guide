package eu.happycoders.collections.arraylist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ch2Creating {

  static void main() {
    System.out.println("== With elements");
    List<String> names = new ArrayList<>(List.of("Ada", "Grace", "Alan"));
    names.add("Linus");
    System.out.println(names);

    System.out.println("== List.of() and Arrays.asList() are not ArrayLists");
    List<String> fixed = List.of("Ada", "Grace", "Alan");
    try {
      fixed.add("Linus");
    } catch (UnsupportedOperationException e) {
      System.out.println("List.of(): " + e);
    }
    List<String> backedByArray = Arrays.asList("Ada", "Grace", "Alan");
    backedByArray.set(0, "Linus");
    System.out.println("Arrays.asList() after set(): " + backedByArray);
    try {
      backedByArray.add("Barbara");
    } catch (UnsupportedOperationException e) {
      System.out.println("Arrays.asList(): " + e);
    }
    System.out.println(backedByArray.getClass().getName());

    System.out.println("== Copy of another collection");
    List<String> copy = new ArrayList<>(names);
    copy.removeFirst();
    System.out.println(names);
    System.out.println(copy);

    System.out.println("== Numbers");
    List<Integer> numbers = new ArrayList<>();
    numbers.add(42);
    numbers.add(7);
    int sum = numbers.get(0) + numbers.get(1);
    System.out.println(sum);

    System.out.println("== A list of lists");
    List<List<Integer>> triangle = new ArrayList<>();
    for (int n = 0; n < 5; n++) {
      List<Integer> row = new ArrayList<>();
      for (int k = 0; k <= n; k++) {
        if (k == 0 || k == n) {
          row.add(1);
        } else {
          List<Integer> previous = triangle.get(n - 1);
          row.add(previous.get(k - 1) + previous.get(k));
        }
      }
      triangle.add(row);
    }
    triangle.forEach(System.out::println);
  }
}
