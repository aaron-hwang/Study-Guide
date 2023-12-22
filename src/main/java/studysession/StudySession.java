package studysession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * A class to represent a study session
 */
public class StudySession implements Session {

  private int questionsAnswered;
  private int questionsEasyToHard;
  private int questionsHardToEasy;

  /**
   * Default constructor
   */
  public StudySession() throws IOException {
    this.questionsAnswered = 0;
    this.questionsEasyToHard = 0;
    this.questionsHardToEasy = 0;
  }

  /**
   * Generate a random set of questions, the amount equal to a given number.
   * @param questionCount The amount of questions to generate. If the amount exceeds that of a given
   *                      file's question count, then we take from every question.
   * @return an arraylist of all the questions we generated
   */
  public ArrayList<QuestionBlock> generateRandomQuestions(QuestionBank bank,
                                                          int questionCount, Random random) {
    if (questionCount < 1) {
      throw new IllegalArgumentException("Question count cannot be less than 1");
    }
    ArrayList<QuestionBlock> hardQuestions
        = bank.getQuestionsByDifficulty(1, 1, questionCount);
    int hardSize = hardQuestions.size();
    ArrayList<QuestionBlock> easyQuestions
        = bank.getQuestionsByDifficulty(0, 0, questionCount - hardSize);
    bank.removeAll(hardQuestions);
    bank.removeAll(easyQuestions);
    Collections.shuffle(hardQuestions, random);
    Collections.shuffle(easyQuestions, random);
    ArrayList<QuestionBlock> returnBlock = new ArrayList<>();
    returnBlock.addAll(hardQuestions);
    returnBlock.addAll(easyQuestions);
    returnBlock.addAll(bank.getBank());

    return returnBlock;
  }

  public void incrementToHardQuestions() {
    this.questionsEasyToHard++;
  }

  public void incrementToEasyQuestions() {
    this.questionsHardToEasy++;
  }

  public void incrementQuestionsAnswered() {
    this.questionsAnswered++;
  }

  /**
   * Get the statistics about this data session
   *
   * @return A string[] representing the data of the statistics of this study session
   */
  public String[] getStats(ArrayList<QuestionBlock> bank) {
    String[] data = new String[5];
    int newQuestionsHard = 0;
    int newQuestionsEasy;
    for (QuestionBlock qb : bank) {
      if (qb.getDifficulty() > 0) {
        newQuestionsHard++;
      }
    }
    newQuestionsEasy = bank.size() - newQuestionsHard;
    data[0] = "Questions changed from Easy to Hard: " + this.questionsEasyToHard;
    data[1] = "Questions changed from Hard to Easy: " + this.questionsHardToEasy;
    data[2] = "Total amount of questions answered: " + this.questionsAnswered;
    data[3] = "Total new amount of hard questions is: " + newQuestionsHard;
    data[4] = "Total new amount of easy questions is: " + newQuestionsEasy;

    return data;
  }

}
