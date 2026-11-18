package eu.happycoders.collections.arraylist;

import java.util.ArrayList;
import java.util.List;

public class Ch3Adding {

  static void main() {
    List<String> names = new ArrayList<>(List.of("Ada", "Alan"));

    names.add("Linus");
    System.out.println(names);

    names.add(1, "Grace");
    System.out.println(names);

    names.addAll(List.of("James", "Barbara"));
    System.out.println(names);

    names.addAll(2, List.of("Ken", "Dennis"));
    System.out.println(names);

    names.addFirst("Charles");
    System.out.println(names);

    System.out.println("== Index after the last element");
    names = new ArrayList<>(List.of("Ada", "Grace", "Alan"));
    names.add(names.size(), "Linus");
    System.out.println(names);
    try {
      names.add(20, "Guido");
    } catch (IndexOutOfBoundsException e) {
      System.out.println(e);
    }
  }
}
