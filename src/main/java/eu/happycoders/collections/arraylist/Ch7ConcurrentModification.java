package eu.happycoders.collections.arraylist;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class Ch7ConcurrentModification {

  static void main() {
    System.out.println("== remove() inside for-each");
    List<String> names = new ArrayList<>(List.of("Ada", "Grace", "Alan", "Linus"));
    try {
      for (String name : names) {
        if (name.startsWith("A")) {
          names.remove(name);
        }
      }
    } catch (ConcurrentModificationException e) {
      System.out.println(e);
    }
    System.out.println(names);

    System.out.println("== Removing the second-to-last element");
    names = new ArrayList<>(List.of("Ada", "Grace", "Alan", "Linus"));
    for (String name : names) {
      System.out.println("Visiting " + name);
      if (name.equals("Alan")) {
        names.remove(name);
      }
    }
    System.out.println(names);

    System.out.println("== Fix 1: removeIf()");
    names = new ArrayList<>(List.of("Ada", "Grace", "Alan", "Linus"));
    names.removeIf(name -> name.startsWith("A"));
    System.out.println(names);

    System.out.println("== Fix 2: Iterator.remove()");
    names = new ArrayList<>(List.of("Ada", "Grace", "Alan", "Linus"));
    Iterator<String> iterator = names.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().startsWith("A")) {
        iterator.remove();
      }
    }
    System.out.println(names);

    System.out.println("== Fix 3: index loop from the end");
    names = new ArrayList<>(List.of("Ada", "Grace", "Alan", "Linus"));
    for (int i = names.size() - 1; i >= 0; i--) {
      if (names.get(i).startsWith("A")) {
        names.remove(i);
      }
    }
    System.out.println(names);
  }
}
