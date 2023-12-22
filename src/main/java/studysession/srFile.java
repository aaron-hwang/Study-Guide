package studysession;

import cs3500.pa01.FileCompiler;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * class represents an SRFile
 */
public class srFile {
  private final Path rawFile;

  /**
   * Default constructor
   *
   * @param rawFile the raw file
   */
  public srFile(Path rawFile) {
    String fileName = rawFile.toString();
    if (!fileName.endsWith(".sr")) {
      throw new IllegalArgumentException("Must be initialized with a .sr file");
    } else {
      this.rawFile = rawFile;
    }
  }

  /**
   * Gets this raw file
   *
   * @return the raw file
   */
  public Path getRawFile() {
    return rawFile;
  }

  /**
   * Generate questions from this srfile
   *
   * @return the questions we want
   *
   * @throws IOException if a file cannot be read
   */
  public QuestionBank generateQuestions() throws IOException {
    ArrayList<QuestionBlock> questions = new ArrayList<>();
    Pattern difficultyMarker = Pattern.compile("(?<=Difficulty:)\s*(\\d+)");
    Pattern idMarker = Pattern.compile("(?<=ID:)\\s*(\\d+)");
    Pattern questionMarker = Pattern.compile("(?<=\\[\\[).*?(?=\\n*:::)");
    Pattern answerMarker = Pattern.compile("(?<=:::)[^\\]]*(?=\\]\\])");

    FileCompiler fc = new FileCompiler();
    ArrayList<String> difficultyData = fc.allInstancesOf(this.rawFile, difficultyMarker);
    ArrayList<String> idData = fc.allInstancesOf(this.rawFile, idMarker);
    ArrayList<String> questionData = fc.allInstancesOf(this.rawFile, questionMarker);
    ArrayList<String> answerData = fc.allInstancesOf(this.rawFile, answerMarker);

    for (int i = 0; i < idData.size(); i++) {
      int difficulty = Integer.parseInt(difficultyData.get(i));
      int id = Integer.parseInt(idData.get(i));
      String answer = answerData.get(i);
      String inquiry = questionData.get(i);
      QuestionBlock question = new QuestionBlock(inquiry, answer, difficulty, id);
      questions.add(question);
    }

    QuestionBank bank = new QuestionBank(questions);
    return bank;
  }


}
