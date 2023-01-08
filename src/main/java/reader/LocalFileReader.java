package main.java.reader;

import main.java.logger.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * LocalFileReader — reads files from local storage.
 */
public class LocalFileReader implements FileReader {

    private final Logger logger;

    public LocalFileReader(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String ReadAll(Path filePath) {

        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            logger.Warnf("unable to read file '%s'", filePath);
        } catch (SecurityException e) {
            logger.Errorf("thread not allowed to read file '%s'", filePath);
        }

        return "";
    }
}
