package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Objects;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileCompilerTest {

  FileCompiler mc = new FileCompiler();
  FileTraverser ft = new FileTraverser();
  FileCollection fc = new FileCollection();
  FileCollection fc1;
  TraversedFile sampleInvalid;
  TraversedFile sampleValid;
  Path among;

  Pattern importantAndHeader;
  Pattern questionBlock;

  /**
   * Setup for each test
   */
  @BeforeEach
  public void setup() {
    among =
        Path.of(
            "src/test/resources/more/among.md");
    try {
      Files.walkFileTree(Path.of("src/test/resources"), ft);
    } catch (IOException e) {
      fail();
    }
    FileTime knownCreationTime = FileTime.from(Instant.parse("2023-05-14T12:00:00Z"));
    FileTime knownModifiedTime = FileTime.from(Instant.parse("2023-05-15T12:00:00Z"));
    Path burger = Path.of("src/test/resources/burger.md");
    fc = ft.getVisitedFiles();
    fc1 = new FileCollection();
    sampleInvalid = new TraversedFile(knownCreationTime, knownModifiedTime, burger);
    sampleValid = new TraversedFile(knownCreationTime, knownModifiedTime, among);

    fc1.add(sampleInvalid);

    importantAndHeader = Pattern.compile(
        "\\[\\[(?:(?!\\:\\:)[^\\]])*?\\]\\]|#{1,6}\\s*(.*)|^\\s*(?=\\n*#)",
        Pattern.MULTILINE);
    questionBlock = Pattern.compile(
        "\\[\\[(?=.*:::)[^\\]]*?\\n?[^\\]]*?\\]\\]|^\\s*$",
        Pattern.MULTILINE);

  }

  /**
   * Test compilation of a file collection
   */
  @Test
  void compileCollection() {
    assertThrows(IOException.class,
        () -> mc.compileCollection(fc1, importantAndHeader));

    assertFalse(Objects.isNull(ft.getVisitedFiles()));
    assertEquals(fc, ft.getVisitedFiles());
    try {
      assertEquals(mc.compileCollection(fc, importantAndHeader).substring(0, 1), "#");
    } catch (Exception e) {
      fail();
    }
  }

  /**
   * Test compiling a singular given TraversedFile
   */
  @Test
  void compileFile() {
    try {
      assertThrows(IOException.class,
          () -> mc.compileFile(sampleInvalid.getRawFile(), importantAndHeader));
    } catch (Exception e) {
      fail();
    }

   try {
     assertEquals(mc.compileFile(among, importantAndHeader).substring(0, 1),
         "#");
   } catch (Exception e) {
     System.out.println(e.getMessage());
     fail(e.getMessage());
   }
    try {
      assertEquals(
          mc.compileFile(sampleValid.getRawFile(),
              importantAndHeader).substring(0, 1),
          "#");
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testAppendCollection() {
    try {
      assertTrue(
          mc.appendCompilation(
              fc, questionBlock, "Difficulty:1 ID:").contains("Difficulty:1 ID:25"));
    } catch (Exception e) {
      fail();
    }

    try {
      assertThrows(IOException.class,
          () ->mc.appendCompilation(fc1, questionBlock, "hi"));
    } catch (Exception e) {
      fail();
    }

  }

  @Test
  public void testDataAppend() {
    assertThrows(IOException.class,
        () -> mc.dataAppend(sampleInvalid.getRawFile(),importantAndHeader, "data"));
    try {
      assertTrue(mc.dataAppend(
          sampleValid.getRawFile(),
          questionBlock,
          "Difficulty:1 ID:").contains("Difficulty:1 ID:2"));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testAllInstancesOf() {
    assertThrows(IOException.class,
        () -> mc.allInstancesOf(sampleInvalid.getRawFile(), importantAndHeader));
    try {
      assertTrue(
          mc.allInstancesOf(
              sampleValid.getRawFile(),
              importantAndHeader).contains("# Vectors"));
    } catch (Exception e) {
      fail();
    }
  }
}