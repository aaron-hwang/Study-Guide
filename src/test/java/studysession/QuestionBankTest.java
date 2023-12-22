package studysession;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionBankTest {

  QuestionBank bank;
  @BeforeEach
  public void setup() {
    srFile srFile = new srFile(Path.of("src/test/resources/more/questionHavers/samples.sr"));
    try {
      bank = srFile.generateQuestions();
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testRemoveAll() {
    bank.removeAll(bank.getBank());
    assertEquals(0, bank.size());
  }


  @Test
  void getBank() {
    ArrayList<QuestionBlock> qBank = bank.getBank();
    assertEquals(qBank.get(1), bank.getBank().get(1));
    assertEquals(qBank.get(2), bank.getBank().get(2));
    assertFalse(Objects.isNull(bank.getBank()));
  }

  @Test
  void getQuestionsByDifficulty() {
    assertEquals(5, bank.getQuestionsByDifficulty(0, 1, 5).size());
    assertEquals(5, bank.getQuestionsByDifficulty(0, 0, 5).size());
    assertEquals(6, bank.getQuestionsByDifficulty(0, 0, 100).size());
  }

  @Test
  void size() {
    assertEquals(27, bank.size());
    assertEquals(bank.size(), bank.getBank().size());
  }

  @Test
  void testToString() {
    assertEquals(bank.toString().substring(0, 2), "[[");
    assertFalse(bank.toString().isEmpty());
  }
}