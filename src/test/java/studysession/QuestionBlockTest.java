package studysession;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionBlockTest {

  QuestionBlock qb;
  @BeforeEach
  public void setup() {
    qb = new QuestionBlock("What is spongebob?", "A sponge", 0, 1);
  }

  @Test
  void getQuestion() {
    assertEquals(qb.getQuestion(), "What is spongebob?");
  }

  @Test
  void changeDifficulty() {
    qb.changeDifficulty(1);
    assertEquals(1, qb.getDifficulty());
  }

  @Test
  void getDifficulty() {
    assertEquals(qb.getDifficulty(), 0);
  }

  @Test
  void getAnswer() {
    assertEquals(qb.getAnswer(), "A sponge");
  }

  @Test
  void testToString() {
    assertEquals(qb.toString(), "[[What is spongebob?:::A sponge]] Difficulty:0 ID:1");
  }
}