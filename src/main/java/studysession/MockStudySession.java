package studysession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * A mock study session, for testing
 */
public class MockStudySession implements Session {

  @Override
  public ArrayList<QuestionBlock> generateRandomQuestions(
      QuestionBank bank, int questionCount, Random random) throws IOException {
    this.errorThrower();
    return null;
  }

  @Override
  public void incrementToHardQuestions() {
    this.errorThrower();
  }

  @Override
  public void incrementToEasyQuestions() {
    this.errorThrower();
  }

  @Override
  public void incrementQuestionsAnswered() {
    this.errorThrower();
  }

  @Override
  public String[] getStats(ArrayList<QuestionBlock> bank) {
    this.errorThrower();
    return null;
  }

  public void errorThrower() {
    throw new RuntimeException("This is a test exception");
  }

}
