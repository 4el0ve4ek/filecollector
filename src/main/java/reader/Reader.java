package main.java.reader;

import java.io.IOException;
import java.util.List;

public interface Reader {

    /**
     * @param filename - имя файла, который надо прочесть
     * @return Все, что содержиться в файле
     * @throws IOException - могут возникнуть ошибки при работе с файлами
     */
    String ReadAll(String filename) throws IOException;

    /**
     * @param filename - имя файла, который надо прочесть
     * @return Массив строк, которые находяться в файле
     * @throws IOException - могут возникнуть ошибки при работе с файлами
     */
    List<String> ReadLines(String filename) throws IOException;
}
