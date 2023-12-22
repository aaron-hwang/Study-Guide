package studysession;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class representing a bank of questions
 */
public class QuestionBank {

  private final ArrayList<QuestionBlock> bank;

  public QuestionBank(ArrayList<QuestionBlock> bank) {
    this.bank = bank;
  }

  public ArrayList<QuestionBlock> getBank() {
    return this.bank;
  }

  /**
   * Grab all the questions from this question bank that are within the difficulty bounds described,
   * grabbing an amount less than or equal to the given amount
   *
   * @param lowerBound the lower bound of difficulty that we should grab, inclusive
   * @param upper the upper bound of difficulty to accept, inclusive
   * @param amtGen the maximum amount to generate
   *
   * @return The questions matching the difficulty parameters
   */
  public ArrayList<QuestionBlock> getQuestionsByDifficulty(int lowerBound, int upper, int amtGen) {
    ArrayList<QuestionBlock> returnBank = new ArrayList<>();
    int i = 0;
    int x = 0;
    while (i < amtGen && x < this.bank.size()) {
      QuestionBlock qb = this.bank.get(x);
      int difficulty = qb.getDifficulty();
      if (difficulty >= lowerBound && difficulty <= upper) {
        returnBank.add(qb);
        i++;
      }
      x++;
    }

    return returnBank;
  }

  /**
   * Return the size of this bank
   *
   * @return the size of the bank
   */
  public int size() {
    return this.bank.size();
  }


  /**
   * Remove all elements of this bank
   * @param c the given collection to remove elements with
   */
  public void removeAll(Collection<QuestionBlock> c) {
    this.bank.removeAll(c);
  }


  /**
   * Convert this questionBank to a string
   *
   * @return the converted string
   */
  public String toString() {
    StringBuilder build = new StringBuilder();
    for (QuestionBlock qb : this.bank) {
      build.append(qb.toString()).append(System.lineSeparator());
    }

    return build.toString();
  }
}
