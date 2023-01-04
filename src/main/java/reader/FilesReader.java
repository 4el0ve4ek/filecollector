package main.java.reader;

import main.java.logger.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * FilesReader -- читает  содержимое файлов, находящихся локально
 */
public class FilesReader implements Reader {

    private final Logger logger;

    public FilesReader(Logger logger) {
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
