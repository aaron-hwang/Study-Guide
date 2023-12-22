package cs3500.pa01;

import java.util.Comparator;

/**
 * A comparator class to order files by their name
 */
public class FilesByName implements Comparator<TraversedFile> {

  /**
   * Compare two traversed files
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   *
   * @return the int determining the ordering of these files
   */
  @Override
  public int compare(TraversedFile o1, TraversedFile o2) {
    return o1.compareName(o2);
  }
}
