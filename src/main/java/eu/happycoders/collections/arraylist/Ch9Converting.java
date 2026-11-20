package eu.happycoders.collections.arraylist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ch9Converting {

  static void main() {
    System.out.println("== ArrayList to array");
    List<String> names = new ArrayList<>(List.of("Ada", "Grace", "Alan"));
    Object[] array1 = names.toArray();
    String[] array2 = names.toArray(new String[0]);
    String[] array3 = names.toArray(String[]::new);
    System.out.println(array1.getClass().getSimpleName() + " " + Arrays.toString(array1));
    System.out.println(array2.getClass().getSimpleName() + " " + Arrays.toString(array2));
    System.out.println(array3.getClass().getSimpleName() + " " + Arrays.toString(array3));

    List<Integer> numbers = new ArrayList<>(List.of(3, 1, 4, 1, 5));
    int[] ints = numbers.stream().mapToInt(Integer::intValue).toArray();
    System.out.println(Arrays.toString(ints));

    System.out.println("== Array to ArrayList");
    String[] languages = {"Java", "Kotlin", "Scala"};
    List<String> languageList = new ArrayList<>(Arrays.asList(languages));
    languageList.add("Groovy");
    System.out.println(languageList);

    int[] primes = {2, 3, 5, 7};
    List<Integer> primeList = new ArrayList<>(Arrays.stream(primes).boxed().toList());
    primeList.add(11);
    System.out.println(primeList);

    System.out.println("== clone() needs a variable of type ArrayList");
    ArrayList<String> pioneers = new ArrayList<>(List.of("Ada", "Grace"));
    @SuppressWarnings("unchecked")
    ArrayList<String> pioneersCopy = (ArrayList<String>) pioneers.clone();
    System.out.println(pioneersCopy);

    System.out.println("== A copy is shallow");
    List<StringBuilder> original = new ArrayList<>(List.of(new StringBuilder("Ada")));
    List<StringBuilder> copy = new ArrayList<>(original);
    copy.getFirst().append(" Lovelace");
    System.out.println("original: " + original);
    System.out.println("copy:     " + copy);

    System.out.println("== subList() is a view");
    List<Integer> digits = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5));
    List<Integer> middle = digits.subList(2, 4);
    middle.set(0, 20);
    System.out.println(digits);

    changeSizeThroughView();
    invalidateView();
  }

  private static void changeSizeThroughView() {
    System.out.println("== Changing the size through the view");
    List<Integer> digits = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5));
    List<Integer> middle = digits.subList(2, 4);
    middle.add(99);
    System.out.println(digits + " " + middle);
    middle.remove(0);
    System.out.println(digits + " " + middle);
    middle.clear();
    System.out.println(digits + " " + middle);
  }

  private static void invalidateView() {
    System.out.println("== Changing the size of the original list invalidates the view");
    List<Integer> digits = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5));
    List<Integer> middle = digits.subList(2, 4);
    digits.add(6);
    try {
      System.out.println(middle);
    } catch (java.util.ConcurrentModificationException e) {
      System.out.println(e);
    }
  }
}
