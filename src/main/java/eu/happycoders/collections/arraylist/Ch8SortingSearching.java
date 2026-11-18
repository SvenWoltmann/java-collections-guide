package eu.happycoders.collections.arraylist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ch8SortingSearching {

  static void main() {
    List<String> names = new ArrayList<>(List.of("Grace", "Ada", "Linus", "Barbara", "Alan"));

    names.sort(null);
    System.out.println(names);

    names.sort(Comparator.comparing(String::length).thenComparing(Comparator.naturalOrder()));
    System.out.println(names);

    names = new ArrayList<>(List.of("Ada", "Alan", "Barbara", "Grace", "Linus"));
    System.out.println(Collections.binarySearch(names, "Grace"));
    System.out.println(Collections.binarySearch(names, "Ken"));
  }
}
