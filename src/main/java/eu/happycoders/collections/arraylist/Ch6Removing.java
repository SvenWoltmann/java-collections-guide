package eu.happycoders.collections.arraylist;

import java.util.ArrayList;
import java.util.List;

public class Ch6Removing {

  static void main() {
    List<String> names =
        new ArrayList<>(List.of("Ada", "Grace", "Alan", "Linus", "James", "Barbara"));

    String removed = names.remove(1);
    System.out.println(removed + " -> " + names);

    boolean found = names.remove("Linus");
    System.out.println(found + " -> " + names);

    names.removeFirst();
    System.out.println(names);

    System.out.println("== remove(int) or remove(Object)?");
    List<Integer> numbers = new ArrayList<>(List.of(10, 20, 30, 1));
    numbers.remove(1);
    System.out.println(numbers);
    numbers.remove(Integer.valueOf(1));
    System.out.println(numbers);

    System.out.println("== removeIf(), removeAll(), retainAll()");
    List<String> pioneers =
        new ArrayList<>(List.of("Ada", "Grace", "Alan", "Linus", "James", "Barbara"));
    pioneers.removeIf(name -> name.startsWith("A"));
    System.out.println(pioneers);
    pioneers.removeAll(List.of("Grace", "James"));
    System.out.println(pioneers);
    pioneers.retainAll(List.of("Barbara", "Ken"));
    System.out.println(pioneers);

    System.out.println("== A range and everything");
    List<Integer> digits = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    digits.subList(2, 5).clear();
    System.out.println(digits);
    digits.clear();
    System.out.println(digits + " " + digits.size());
  }
}
