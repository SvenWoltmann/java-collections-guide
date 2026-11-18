package eu.happycoders.collections.arraylist;

import java.util.ArrayList;
import java.util.List;

public class Intro {

  static void main() {
    List<String> names = new ArrayList<>();
    names.add("Ada");
    names.add("Grace");
    names.add("Alan");

    System.out.println(names.get(1));
    System.out.println(names.size());
    System.out.println(names);
  }
}
