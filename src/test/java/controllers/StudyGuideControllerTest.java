package controllers;

import static org.junit.jupiter.api.Assertions.*;

import cs3500.pa01.FileCollection;
import cs3500.pa01.FileTraverser;
import cs3500.pa01.FilesByCreatedDate;
import cs3500.pa01.FilesByModifiedTime;
import cs3500.pa01.FilesByName;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudyGuideControllerTest {

  StudyGuideController controller;
  FileCollection fc;
  FileTraverser ft;
  @BeforeEach
  public void setup() {
    controller = new StudyGuideController("src/test/resources" ,
        "filename",
        "outputFolder/output.md");

    ft = new FileTraverser();
    try {
      Files.walkFileTree(Path.of("src/test/resources"), ft);
    } catch (Exception e) {
      fail();
    }
    fc = ft.getVisitedFiles();
  }

  @Test
  void run() {
    try {
      controller.run();
      File f = new File("outputFolder/output.md");
      File sr = new File("outputFolder/output.sr");
      assertTrue(f.exists());
      assertTrue(sr.exists());
    } catch (Exception e) {
      fail();
    }

  }

  @Test
  void chooseOrdering() {
    assertTrue(controller.chooseOrdering("filename") instanceof FilesByName);
    assertInstanceOf(FilesByModifiedTime.class, controller.chooseOrdering("modified"));
    assertInstanceOf(FilesByCreatedDate.class, controller.chooseOrdering("created"));
    assertThrows(IllegalArgumentException.class,
        () -> controller.chooseOrdering("booga"));
  }

  @Test
  void generalIOExceptionHandler() {
    assertThrows(IOException.class,
        () -> controller.generalioexceptionHandler("This is an error."));
  }

  @Test
  void grabFiles() {
    try {
      assertFalse(Objects.isNull(controller.grabFiles()));
    } catch (Exception e) {
      fail();
    }
    try {
      assertEquals(controller.grabFiles().getFileList().size(), 5);
    } catch (Exception e) {
      fail();
    }

  }

  @Test
  void markdownCompilation() {
    try {
      assertEquals(controller.markdownCompilation(fc).substring(0, 1), "#");
    } catch (Exception e) {
      fail();
    }

  }

  @Test
  void questionCompilation() {
    try {
      assertEquals(controller.questionCompilation(fc).substring(0, 1), "[");
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void compilation() {
    Pattern importantAndHeader =
        Pattern.compile(
            "(?<=\\[\\[)(?:(?!\\[\\[|\\]\\]|:::)[^\\]])*?(?=\\]\\])" +
                "|#{1,6}\\s*(.*)|\\n(?=\\#[^\\r\\n]*$)",
            Pattern.MULTILINE);
    try {
      assertEquals(controller.compilation(fc, importantAndHeader).substring(0, 1), "#");
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void generateCorrespondingSRPath() {
    assertEquals(controller.generatecorrespondingsrPath("outputFolder/output.md"),
        "outputFolder/output.sr");
  }

  @Test
  void testExceptions() {
    try {
      StudyGuideController badController;
      assertThrows(IllegalArgumentException.class,
          () -> new StudyGuideController(
              "src/test/more/:'ä'jimmbob",
              "created",
              "src/test/resources/more/output"));
    } catch (Exception e) {
      fail();
    }

    try {
      StudyGuideController badController;
      assertThrows(IllegalArgumentException.class,
          () -> new StudyGuideController(
              "src/test/resources",
              "modified",
              "src/test/resourcesä:::"));
    } catch (Exception e) {
      fail();
    }

    try {
      StudyGuideController badController;
      assertThrows(IllegalArgumentException.class,
          () -> new StudyGuideController(
              "src/test/resources", "thisisbad", "outputFolder/output.md"));
    } catch (Exception e) {
      fail();
    }
  }

}