package viewers;

import java.io.IOException;

/**
 * A class to represent the view seen by a client when executing a study session
 */
public class StudySessionViewer implements Viewer {

  private final Appendable output;

  public StudySessionViewer(Appendable output) {
   this.output = output;
  }

  /**
   * Display a message to the user
   * @param message Message to display
   */
  public void messageDisplay(String message) throws IOException {
    try {
      this.output.append(message + System.lineSeparator());
    } catch (IOException e) {
      throw new IOException("IO error occurred");
    }
  }


}
