package main.java.reader;

import java.nio.file.Path;

/**
 * Encapsulates reads from file.
 */
public interface FileReader {

    /**
     * Read all text from file `filePath` to one string.
     *
     * @param filePath - name of the file to read.
     * @return a String containing the content read from the file.
     */
    String ReadAll(Path filePath);

}
