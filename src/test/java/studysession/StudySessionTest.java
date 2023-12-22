package studysession;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudySessionTest {
  StudySession study;
  int questionAmount;
  Random rand;

  Path file;
  studysession.srFile srFile;
  QuestionBank bank;

  ArrayList<QuestionBlock> rawBank;
  @BeforeEach
  public void setup() {
    file = Path.of("outputFolder/output.sr");
    srFile = new srFile(file);
    questionAmount = 5;
    rand = new Random(12);
    try {
      bank = srFile.generateQuestions();
    } catch (Exception e) {
      fail();
    }
    rawBank = bank.getBank();
    try {
      study = new StudySession();
    } catch (Exception e) {
      fail();
    }

  }

  @Test
  void getRandomQuestionAmount() {
    StudySession randomSession = null;
    try {
      randomSession = new StudySession();
    } catch (Exception e) {
      fail();
    }
    try {
      assertEquals(randomSession.generateRandomQuestions(bank,
              questionAmount,
              new Random(12)).size(),
          27);
    } catch (Exception e) {
      fail();
    }

    ArrayList<Integer> test0 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    ArrayList<Integer> test1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    Collections.shuffle(test0, new Random(5));
    Collections.shuffle(test1, new Random(5));
    assertEquals(test0, test1);
  }

  @Test
  void incrementToHardQuestions() {
    for (int i = 0; i < 5; i++) {
      study.incrementToHardQuestions();
    }
    assertEquals(study.getStats(rawBank)[0], "Questions changed from Easy to Hard: 5");

  }

  @Test
  void incrementToEasyQuestions() {
    for (int i = 0; i < 5; i++) {
      study.incrementToEasyQuestions();
    }
    assertEquals(study.getStats(rawBank)[1], "Questions changed from Hard to Easy: 5");
  }

  @Test
  void incrementQuestionsAnswered() {
    for (int i = 0; i < 5; i++) {
      study.incrementQuestionsAnswered();
    }
    assertEquals(study.getStats(rawBank)[2], "Total amount of questions answered: 5");
  }

  @Test
  void getStats() {
    for (int i = 0; i < 5; i++) {
      study.incrementQuestionsAnswered();
      study.incrementQuestionsAnswered();
      study.incrementToHardQuestions();
      study.incrementToEasyQuestions();
    }

    assertEquals(study.getStats(rawBank)[0], "Questions changed from Easy to Hard: 5");
    assertEquals(study.getStats(rawBank)[1], "Questions changed from Hard to Easy: 5");
  }

  @Test
  void testExceptions() {
    try {
      assertThrows(IllegalArgumentException.class,
          () -> study.generateRandomQuestions(srFile.generateQuestions(), -1, rand));
    } catch (Exception e) {
      fail();
    }
  }

}