package main.java.reader;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CachedFileReader — caching wrapper above some FileReader.
 * Used for decreasing reads from local storage and consistent file content.
 */
public class CachedFileReader implements FileReader {

    private final Map<Path, String> cache = new ConcurrentHashMap<>();
    private final FileReader originalFileReader;

    public CachedFileReader(FileReader fileReader) {
        originalFileReader = fileReader;
    }

    @Override
    public String ReadAll(Path filePath) {
        if (!cache.containsKey(filePath)) {
            cache.put(filePath, originalFileReader.ReadAll(filePath));
        }

        return cache.get(filePath);
    }
}
