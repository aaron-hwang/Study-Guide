package readers;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemInReaderTest {

  Reader systemReader;
  @BeforeEach
  public void setup() {
    systemReader = new SystemInReader(new InputStreamReader(System.in));
  }
  @Test
  void read() {
    assertThrows(NoSuchElementException.class,
        () -> systemReader.read());
  }
}