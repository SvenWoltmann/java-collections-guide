package eu.happycoders.collections.arraylist;

import java.util.ArrayList;
import java.util.List;

public class Ch5Changing {

  static void main() {
    List<String> names = new ArrayList<>(List.of("Ada", "Grace", "Alan"));

    String replaced = names.set(1, "Linus");
    System.out.println(replaced + " -> " + names);

    names.replaceAll(String::toUpperCase);
    System.out.println(names);
  }
}
