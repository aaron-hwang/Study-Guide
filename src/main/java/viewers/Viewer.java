package viewers;

import java.io.IOException;

/**
 * An interface for a viewer
 */
public interface Viewer {

  void messageDisplay(String message) throws IOException;
}
