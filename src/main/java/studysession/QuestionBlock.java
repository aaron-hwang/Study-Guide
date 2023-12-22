package studysession;

/**
 * Represents a QandA block:
 */
public class QuestionBlock {

  //Constants representing the ending, beginning, and middle of question blocks
  private final String questionBeginning = "[[";
  private final String questionEnd = "]]";
  private final String questionSeparator = ":::";

  //The string representing this question block
  private final String question;
  private final String answer;

  private final int idNumber;

  //In this program, a question is difficult if the difficulty is higher than 0. Currently not
  //designed to have difficulty higher than 1, but can be easily extended for such.
  private int difficulty;

  /**
   * Constructor.
   *
   * @param question Question associated with this question block
   * @param answer the answer to the question
   * @param difficulty the difficulty of this question
   * @param idNumber the id number of this question
   */
  public QuestionBlock(String question, String answer, int difficulty, int idNumber) {
    this.question = question;
    this.answer = answer;
    this.idNumber = idNumber;
    this.difficulty = difficulty;
  }

  public String getQuestion() {
    return this.question;
  }

  public void changeDifficulty(int difficult) {
    difficulty = difficult;
  }

  public int getDifficulty() {
    return this.difficulty;
  }

  public String getAnswer() {
    return this.answer;
  }

  /**
   * Convert this question to a string
   *
   * @return The converted String
   */
  public String toString() {
    String build = this.questionBeginning
        + this.question
        + this.questionSeparator
        + this.answer
        + questionEnd
        + " "
        + "Difficulty:" + this.difficulty
        + " "
        + "ID:" + this.idNumber;

    return build;
  }
}
