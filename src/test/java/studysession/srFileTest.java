package studysession;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class srFileTest {

  srFile sr;

  @BeforeEach
  public void setup() {
    sr = new srFile(Path.of("src/test/resources/more/questionHavers/samples.sr"));
  }


  @Test
  void getRawFile() {
    assertEquals(sr.getRawFile(), Path.of("src/test/resources/more/questionHavers/samples.sr"));
  }

  @Test
  void generateQuestions() {
    try {
      assertFalse(Objects.isNull(sr.generateQuestions()));
    } catch (Exception e) {
      fail();
    }
    try {
      assertEquals(sr.generateQuestions().size(), 27);
    } catch (Exception e) {
      fail();
    }
  }
}