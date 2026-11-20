package eu.happycoders.collections.arraylist;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Makes the capacity of an {@link ArrayList} visible. Reads the private field {@code elementData}
 * and therefore needs {@code --add-opens java.base/java.util=ALL-UNNAMED}. For demonstration only.
 */
public class Ch10Capacity {

  private static final Field ELEMENT_DATA;

  static {
    try {
      ELEMENT_DATA = ArrayList.class.getDeclaredField("elementData");
      ELEMENT_DATA.setAccessible(true);
    } catch (NoSuchFieldException e) {
      throw new IllegalStateException(e);
    }
  }

  static int capacity(ArrayList<?> list) {
    try {
      return ((Object[]) ELEMENT_DATA.get(list)).length;
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  static void main() {
    System.out.println("== new ArrayList<>()");
    ArrayList<Integer> list = new ArrayList<>();
    System.out.println("size 0, capacity " + capacity(list));
    int lastCapacity = capacity(list);
    for (int i = 1; i <= 100; i++) {
      list.add(i);
      if (capacity(list) != lastCapacity) {
        lastCapacity = capacity(list);
        System.out.println("size " + list.size() + ", capacity " + lastCapacity);
      }
    }

    System.out.println("== new ArrayList<>(0)");
    ArrayList<Integer> zero = new ArrayList<>(0);
    lastCapacity = capacity(zero);
    System.out.println("size 0, capacity " + lastCapacity);
    for (int i = 1; i <= 20; i++) {
      zero.add(i);
      if (capacity(zero) != lastCapacity) {
        lastCapacity = capacity(zero);
        System.out.println("size " + zero.size() + ", capacity " + lastCapacity);
      }
    }

    System.out.println("== addAll() beyond the growth by half");
    ArrayList<Integer> full = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      full.add(i);
    }
    full.addAll(Collections.nCopies(100, 0));
    System.out.println("size " + full.size() + ", capacity " + capacity(full));

    System.out.println("== trimToSize()");
    trim();

    System.out.println("== ensureCapacity()");
    ensure();

    System.out.println("== new ArrayList<>(1_000)");
    ArrayList<Integer> presized = new ArrayList<>(1_000);
    System.out.println("size " + presized.size() + ", capacity " + capacity(presized));
  }

  private static void trim() {
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 0; i < 11; i++) {
      list.add(i);
    }
    System.out.println("size " + list.size() + ", capacity " + capacity(list));
    for (int i = 0; i < 8; i++) {
      list.removeLast();
    }
    System.out.println("size " + list.size() + ", capacity " + capacity(list));
    list.trimToSize();
    System.out.println("size " + list.size() + ", capacity " + capacity(list));
  }

  private static void ensure() {
    ArrayList<Integer> list = new ArrayList<>(List.of(0, 1, 2));
    System.out.println("size " + list.size() + ", capacity " + capacity(list));
    list.ensureCapacity(1_000);
    System.out.println("size " + list.size() + ", capacity " + capacity(list));
  }
}
