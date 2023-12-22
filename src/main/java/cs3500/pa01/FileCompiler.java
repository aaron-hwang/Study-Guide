package cs3500.pa01;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Compiles markdown files
 */
public class FileCompiler {

  /**
   * Compile a collection of files, given a pattern of strings to look for
   *
   * @param collection files to compile
   * @param lookFor pattern to look for
   *
   * @return the compiled files
   * @throws IOException when we can't read a file for some reason
   */
  public String compileCollection(FileCollection collection, Pattern lookFor) throws IOException {
    ArrayList<TraversedFile> files;
    StringBuilder result = new StringBuilder();
    files = collection.getFileList();
    for (TraversedFile f : files) {
      Path file = f.getRawFile();
      try {
        String current = this.compileFile(file, lookFor);
        String seperator = "";
        if (!current.isEmpty()) {
          seperator = System.lineSeparator();
        }
        result.append(this.compileFile(file, lookFor)).append(seperator);
      } catch (IOException e) {
        throw new IOException("Something went wrong while writing");
      }
    }

    return result.toString();
  }

  /**
   * Compile a collection of files, appending a given string to every single instance of
   * a given pattern
   *
   * @param collection The collection to compile
   * @param lookFor The pattern we are looking for
   * @param toAppend The string to append to every instance of lookFor
   *
   * @return A string representing the compiled contents of every file given
   * @throws IOException When the file system is corrupted or somehow inaccessible
   */
  public String appendCompilation(FileCollection collection, Pattern lookFor, String toAppend)
      throws IOException {
    ArrayList<TraversedFile> files;
    StringBuilder result = new StringBuilder();
    files = collection.getFileList();

    for (TraversedFile f : files) {
      Path file = f.getRawFile();
      try {
        String current = this.dataAppend(file, lookFor, toAppend);
        String seperator = "";
        if (!current.isEmpty()) {
          seperator = System.lineSeparator();
        }
        result.append(current).append(seperator);
      } catch (IOException e) {
        throw new IOException("One or more files not found");
      }
    }

    return result.toString();
  }

  /**
   * Compile a singular file
   *
   * @param file lookingFor The file we'd like to compile
   * @return The important contents and headers of the file, as a String
   * @throws FileNotFoundException when a given file path is not found
   */
  public String compileFile(Path file, Pattern lookingFor) throws IOException {
    StringBuilder build = new StringBuilder();
    ArrayList<String> instances = this.allInstancesOf(file, lookingFor);
    for (String s : instances) {
      String seperator = System.lineSeparator();
      if (s.strip().equals("")) {
        seperator = "";
      }
      build.append(s).append(seperator);
    }

    return build.toString();
  }

  /**
   * Appends metadata strings to each instance of a seen string pattern
   *
   * @param file The file to search through
   * @param lookingFor the pattern we are looking for
   * @param data The data to append
   *
   * @return A string representing the finished compiled file
   * @throws IOException when we cannot compile a file
   */
  public String dataAppend(Path file, Pattern lookingFor, String data) throws IOException {
    StringBuilder build = new StringBuilder();

    ArrayList<String> instances;
    try {
      instances = this.allInstancesOf(file, lookingFor);
    } catch (IOException e) {
      throw new IOException("Something went wrong while trying to compile");
    }

    for (int i = 0; i < instances.size(); i++) {
      String instance = instances.get(i);
      build.append(instance).append(" ").append(data).append(i).append(System.lineSeparator());
    }

    return build.toString();

  }

  /**
   * Capture all instances of a pattern of text from a file
   *
   * @param file The file to search through
   * @param lookFor The pattern of text we are looking for
   *
   * @return An arraylist containing every instance of the given pattern
   */
  public ArrayList<String> allInstancesOf(Path file, Pattern lookFor) throws IOException {
    Scanner scanner;
    try {
      scanner = new Scanner(file);
    } catch (IOException e) {
      throw new IOException(e.getMessage());
    }

    //Take the stream, map the group function to each MatchResult in said stream,
    //Collect the results into a list
    List<String>
        result = scanner.findAll(lookFor)
        .map(MatchResult::group)
        .toList();

    return new ArrayList<>(result);
  }
}
