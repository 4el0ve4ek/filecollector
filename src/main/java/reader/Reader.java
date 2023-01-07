package main.java.reader;

import java.nio.file.Path;

public interface Reader {

    /**
     * @param filePath - имя файла, который надо прочесть
     * @return Все, что содержиться в файле
     */
    String ReadAll(Path filePath);

}
