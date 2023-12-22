package cs3500.pa01;

import studysession.Session;
import studysession.StudySession;
import controllers.StudyGuideController;
import controllers.StudySessionController;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;


/**
 * This is the entry point for this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - args[0]: Desired Absolute/Relative path towards directory with desired .md files,
   *               args[1]: Desired ordering of files, one of filename, modified, or created
   *               args[2]: Desired Absolute/relative path of output file
   *             if args.length() == 0, then the program will instead start a new study session.
   * @throws IOException will throw an IOException if something is wrong with the file directory
   */
  public static void main(String[] args) throws IOException {
    System.out.println("Hello from PA01 Template Repo");
    if (args.length == 3) {
      String inputPath = args[0];
      String order = args[1];
      String outputPath = args[2];
      StudyGuideController controller = new StudyGuideController(inputPath, order, outputPath);
      controller.run();
    } else if (args.length == 0) {
      Readable input = new InputStreamReader(System.in);
      Appendable output = new PrintStream(System.out);
      Session session = new StudySession();
      StudySessionController studyController = new StudySessionController(input, output, session);
      studyController.run();
    } else {
      throw new IllegalStateException("You must have 3 or 0 arguments exactly.");
    }
  }

}