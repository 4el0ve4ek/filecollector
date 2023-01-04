package main.java.reader;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachedReader implements Reader {

    private final Map<Path, String> cache = new ConcurrentHashMap<>();
    private final Reader originalReader;

    public CachedReader(Reader reader) {
        originalReader = reader;
    }

    @Override
    public String ReadAll(Path filePath) {
        if (!cache.containsKey(filePath)) {
            cache.put(filePath, originalReader.ReadAll(filePath));
        }

        return cache.get(filePath);
    }
}
