package readers;

import java.util.Objects;
import java.util.Scanner;

/**
 * Reads inputs for us
 */
public class SystemInReader implements Reader {

  /**
   * Our fields
   */
  private final Readable readable;
  private final Scanner scanner;

  /**
   * Default constructor
   *
   * @param reader the readable we pass in
   */
  public SystemInReader(Readable reader) {
    this.readable = Objects.requireNonNull(reader);
    this.scanner = new Scanner(reader);
  }

  /**
   * Read input, output it as a string
   *
   * @return what we read
   */
  @Override
  public String read() {
    StringBuilder output = new StringBuilder();

    output.append(scanner.nextLine());

    return output.toString();
  }
}
