package controllers;

import static org.junit.jupiter.api.Assertions.*;

import studysession.KeyInput;
import studysession.MockStudySession;
import studysession.QuestionBlock;
import studysession.Session;
import studysession.StudySession;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudySessionControllerTest {

  StudySessionController controller;
  StudySessionController good;
  StudySessionController file;
  StudySessionController file2;
  QuestionBlock qb;
  QuestionBlock qb1;
  String inputs;
  String altInputs;
  Session normal;
  Appendable output;
  @BeforeEach
  public void setup() {
    inputs = "outputFolder/output";
    altInputs = "outputFolder/output.sr 5";
    Readable fileRead = null;
    try {
      fileRead = new FileReader(Path.of("outputFolder/inputs.txt").toFile());
    } catch (Exception e) {
      fail();
    }
    Readable fileRead2 = null;
    try {
      fileRead2 = new FileReader(Path.of("outputFolder/inputs2.txt").toFile());
    } catch (Exception e) {
      fail();
    }
    StringBuilder writeTo = new StringBuilder();
    Readable input = new StringReader(inputs);
    output = writeTo;
    Session mock = new MockStudySession();
    try {
      normal = new StudySession();
    } catch (Exception e) {
      fail();
    }

    controller = new StudySessionController(input, output, mock);
    good = new StudySessionController(input, output, normal);
    file = new StudySessionController(fileRead, output, normal);
    file2 = new StudySessionController(fileRead2, output, normal);
    qb = new QuestionBlock("What is 1 + 1", "2", 1, 0);
    qb1 = new QuestionBlock("What is 1 + 1", "2", 0, 0);
  }

  @Test
  void run() {
    assertThrows(IllegalArgumentException.class,
        () -> controller.run());
    try {
      file.run();
      File f = new File("outputFolder/output.sr");
      assertTrue(f.exists());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      fail();
    }
    try {
      assertThrows(IllegalArgumentException.class,
          () -> file2.run());
    } catch (Exception e) {
      fail();
    }

  }

  @Test
  void inputProcessing() {
    assertEquals(controller.inputProcessing("1"), KeyInput.MARKEASY);
    assertEquals(controller.inputProcessing("2"), KeyInput.MARKHARD);
    assertEquals(controller.inputProcessing("3"), KeyInput.SEEANSWER);
    assertEquals(controller.inputProcessing("0"), KeyInput.EXIT);
    assertEquals(controller.inputProcessing("h"), KeyInput.UNKNOWNINPUT);
  }

  @Test
  void keyInputProcessing() {

    try {
      assertEquals(good.keyInputProcessing(KeyInput.MARKHARD, qb), 1);
    } catch (Exception e) {
      fail();
    }
    try {
      assertThrows(RuntimeException.class,
          () -> controller.keyInputProcessing(KeyInput.MARKHARD, qb1));
    } catch (Exception e) {
      fail();
    }
    try {
      assertEquals(good.keyInputProcessing(KeyInput.EXIT, qb), 1000000);
    } catch (Exception e) {
      fail();
    }
    try {
      assertEquals(good.keyInputProcessing(KeyInput.UNKNOWNINPUT, qb), 0);
    } catch (Exception e) {
      fail();
    }
    try {
      good.keyInputProcessing(KeyInput.SEEANSWER, qb);
      assertTrue(output.toString().contains(qb.getAnswer()));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testMock() {
    try {
      assertThrows(RuntimeException.class,
          () -> controller.keyInputProcessing(KeyInput.MARKEASY, qb));
    } catch (Exception e) {
      fail();
    }
    try {
      assertThrows(RuntimeException.class,
          () -> controller.keyInputProcessing(KeyInput.MARKEASY, qb1));
    } catch (Exception e) {
      fail();
    }
    try {
      assertThrows(RuntimeException.class,
          () -> controller.keyInputProcessing(KeyInput.MARKHARD, qb1));
    } catch (Exception e) {
      fail();
    }
    try {
      assertThrows(RuntimeException.class,
          () -> controller.keyInputProcessing(KeyInput.MARKHARD, qb));
    } catch (Exception e) {
      fail();
    }

  }

}