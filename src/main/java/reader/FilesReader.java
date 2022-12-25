package main.java.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


/**
 * FilesReader -- читает  содержимое файлов, находящихся локально
 */
public class FilesReader implements Reader {


    @Override
    public String ReadAll(String filename) throws IOException {
        return String.join("\n", ReadLines(filename));
    }

    @Override
    public List<String> ReadLines(String filename) throws IOException {
        return Files.readAllLines(Path.of(filename));
    }
}
