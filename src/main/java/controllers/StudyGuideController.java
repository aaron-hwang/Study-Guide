package controllers;

import cs3500.pa01.FileCollection;
import cs3500.pa01.FileCompiler;
import cs3500.pa01.FileOrdering;
import cs3500.pa01.FileTraverser;
import cs3500.pa01.FilesByCreatedDate;
import cs3500.pa01.FilesByModifiedTime;
import cs3500.pa01.FilesByName;
import cs3500.pa01.TraversedFile;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * This class represents the controller for the generation of a study guide and corresponding
 * .sr file
 */
public class StudyGuideController implements Controller {

  private final Path inputPath;
  private final Path outputPath;
  private final Path outputsrpath;
  private final Comparator<TraversedFile> order;


  /**
   * our constructor
   * @param inputPath our input path
   * @param order our ordering
   * @param outputPath our output path
   */
  public StudyGuideController(String inputPath, String order, String outputPath) {
    try {
      this.inputPath = Path.of(inputPath);
    } catch (InvalidPathException e) {
      throw new IllegalArgumentException("Could not instantiate the path");
    }
    try {
      this.outputPath = Path.of(outputPath);
    } catch (InvalidPathException e) {
      throw new IllegalArgumentException("Could not instantiate output path");
    }
    this.outputsrpath = Path.of(this.generatecorrespondingsrPath(outputPath));
    this.order = this.chooseOrdering(order);
  }


  /**
   * Run the controller
   */
  @Override
  public void run() throws IOException {

    FileCollection fc = this.grabFiles();
    fc.sort(this.order);
    String guideContents = this.markdownCompilation(fc);
    File output = new File(outputPath.toUri());
    File srOutput = new File(outputsrpath.toUri());
    output.createNewFile();
    srOutput.createNewFile();
    FileWriter writetooutput = new FileWriter(output);
    FileWriter writetosr = new FileWriter(srOutput);
    writetooutput.write(guideContents);
    String srFileContents = this.questionCompilation(fc);
    writetosr.write(srFileContents);
    writetooutput.close();
    writetosr.close();
  }

  /**
   * Given a string, returns the appropriate comparator for TraversedFiles
   *
   * @param input The string to determine input from
   * @return The appropriate Comparator for TraversedFiles
   *
   * @throws IllegalArgumentException if the input is not one of the valid orderings
   */
  public Comparator<TraversedFile> chooseOrdering(String input) {
    Comparator<TraversedFile> ordering = null;
    FileOrdering inputInterpretation = FileOrdering.valueOf(input.toUpperCase());
    switch (inputInterpretation) {
      case FILENAME:
        ordering = new FilesByName();
        break;
      case CREATED:
        ordering = new FilesByCreatedDate();
        break;
      case MODIFIED:
        ordering = new FilesByModifiedTime();
        break;
      default:
        break;

    }

    return ordering;
  }

  /**
   * Handles general IOExceptions for the controller
   *
   * @param message The message we want to be associated with our exception
   * @return nothing
   * @throws IOException Every time the method is run
   */
  public String generalioexceptionHandler(String message) throws IOException {
    throw new IOException(message);
  }

  /**
   * Grabs markdown files from the input path of this StudyGuideController for usage
   *
   * @return A FileCollection of files from this controller's given input path
   * @throws IOException if something goes wrong
   */
  public FileCollection grabFiles() throws IOException {
    FileTraverser ft = new FileTraverser();
    Files.walkFileTree(this.inputPath, ft);
    return ft.getVisitedFiles();
  }

  /**
   * Compile a given collection and extract all the markdown information
   * @param collection The collection to compile
   * @return The compiled compilation of files
   * @throws IOException ioexception when a file cannot be read
   */
  public String markdownCompilation(FileCollection collection) throws IOException {
    Pattern importantAndHeader =
        Pattern.compile(
            "(?<=\\[\\[)(?:(?!\\[\\[|\\]\\]|:::)[^\\]])*?(?=\\]\\])"
                + "|#{1,6}\\s*(.*)|\\n(?=\\#[^\\r\\n]*$)",
        Pattern.MULTILINE);
    return this.compilation(collection, importantAndHeader);
  }

  /**
   * Compile a given collection, extracting only the question blocks from it and appending the
   * relevant metadata to each instance
   * @param collection The collection to compile
   * @return The string representing the compiled contents of all files in the given collection
   * @throws IOException If unable to read a given file
   */
  public String questionCompilation(FileCollection collection) throws IOException {
    Pattern questionBlock =
        Pattern.compile(
            "\\[\\[(?=.*:::)[^\\]]*?\\n?[^\\]]*?\\]\\]",
            Pattern.MULTILINE);
    FileCompiler fc = new FileCompiler();
    String difficultyMetaData = "Difficulty:1 ID:";

    return fc.appendCompilation(collection, questionBlock, difficultyMetaData);
  }

  /**
   * Compile a collection of files, grabbing only whatever matches the given pattern
   * @param collection The collection to compile
   * @param lookFor The pattern we are looking for
   * @return The string representing the compiled collection
   * @throws IOException When unable to read a file
   */
  public String compilation(FileCollection collection, Pattern lookFor) throws IOException {
    FileCompiler compiler = new FileCompiler();
    String result;
    result = compiler.compileCollection(collection, lookFor);
    return result;
  }

  /**
   * Generates the correct corresponding SR path for a given other path
   * @param path The file we wish to generate off of
   * @return The string representing the path of the correct sr file
   */
  public String generatecorrespondingsrPath(String path) {
    String toAppendTo = path.substring(0, path.length() - 2);
    return toAppendTo.concat("sr");
  }

}
