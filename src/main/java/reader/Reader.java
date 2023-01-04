package main.java.reader;

import java.nio.file.Path;

public interface Reader {

    /**
     * @param filePath - имя файла, который надо прочесть
     * @return Все, что содержиться в файле, или пустую строку, если возникла ошибка.
     */
    String ReadAll(Path filePath);

}
