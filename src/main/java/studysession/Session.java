package studysession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * An interface for a session
 */
public interface Session {

  ArrayList<QuestionBlock> generateRandomQuestions(
      QuestionBank bank, int questionCount, Random random)
      throws IOException;

  void incrementToHardQuestions();

  void incrementToEasyQuestions();

  void incrementQuestionsAnswered();

  String[] getStats(ArrayList<QuestionBlock> bank);
}
