package eu.happycoders.collections.arraylist;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Ch7Iterating {

  static void main() {
    List<String> names = new ArrayList<>(List.of("Ada", "Grace", "Alan"));

    System.out.println("== for-each");
    for (String name : names) {
      System.out.println(name);
    }

    System.out.println("== Index");
    for (int i = 0; i < names.size(); i++) {
      System.out.println(i + ": " + names.get(i));
    }

    System.out.println("== forEach()");
    names.forEach(System.out::println);

    System.out.println("== ListIterator");
    names = new ArrayList<>(List.of("Ada", "Grace", "Alan"));
    ListIterator<String> iterator = names.listIterator();
    while (iterator.hasNext()) {
      String name = iterator.next();
      if (name.equals("Grace")) {
        iterator.set("Grace Hopper");
        iterator.add("Linus");
      }
    }
    System.out.println(names);

    System.out.println("== Stream");
    names = new ArrayList<>(List.of("Ada", "Grace", "Alan"));
    List<String> shortNames = names.stream().filter(name -> name.length() <= 4).toList();
    System.out.println(shortNames);

    System.out.println("== Backwards");
    names = new ArrayList<>(List.of("Ada", "Grace", "Alan"));
    for (String name : names.reversed()) {
      System.out.println(name);
    }
  }
}
