package eu.happycoders.collections.arraylist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ch1ArrayVsArrayList {

  static void main() {
    String[] array = new String[3];
    array[0] = "Ada";
    array[1] = "Grace";
    array[2] = "Alan";
    System.out.println(array.length + " " + Arrays.toString(array));

    List<String> list = new ArrayList<>();
    list.add("Ada");
    list.add("Grace");
    list.add("Alan");
    list.add("Linus");
    System.out.println(list.size() + " " + list);
  }
}
