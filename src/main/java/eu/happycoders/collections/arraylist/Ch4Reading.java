package eu.happycoders.collections.arraylist;

import java.util.ArrayList;
import java.util.List;

public class Ch4Reading {

  static void main() {
    List<String> names = new ArrayList<>(List.of("Ada", "Grace", "Alan", "Grace"));

    System.out.println(names.get(2));
    System.out.println(names.getFirst() + " " + names.getLast());
    System.out.println(names.size() + " " + names.isEmpty());

    System.out.println(names.contains("Alan"));
    System.out.println(names.indexOf("Grace") + " " + names.lastIndexOf("Grace"));
    System.out.println(names.indexOf("Linus"));

    System.out.println("== Invalid index");
    try {
      names.get(4);
    } catch (IndexOutOfBoundsException e) {
      System.out.println(e);
    }

    System.out.println("== Empty list");
    List<String> empty = new ArrayList<>();
    try {
      empty.getFirst();
    } catch (java.util.NoSuchElementException e) {
      System.out.println(e);
    }
    try {
      empty.get(0);
    } catch (IndexOutOfBoundsException e) {
      System.out.println(e);
    }
  }
}
