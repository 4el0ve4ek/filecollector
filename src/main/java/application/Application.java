package main.java.application;

import main.java.graph.DirectionalGraph;
import main.java.logger.Logger;
import main.java.reader.Reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {
    private final Reader reader;
    private final Logger logger;

    public Application(Logger logger, Reader reader) {
        this.reader = reader;
        this.logger = logger;
    }

    public void Process(String rootDirectory) {
        var rootPath = Path.of(rootDirectory);

        if (!Files.exists(rootPath)) {
            logger.Errorf("file or directory '%s' not exist\n", rootDirectory);
            return;
        }

        var allFiles = getSubFiles(rootPath);

        var dependencyGraph = buildGraphOfDependencies(allFiles, rootPath);

        var cycle = dependencyGraph.GetCycle();
        if (!cycle.isEmpty()) {
            logger.Error("there is a cycle dependency! abort execution!");
            cycle.forEach(logger::Warn);
            return;
        }

        dependencyGraph.GetTopSort().forEach(logger::Info);
    }

    private DirectionalGraph<Path> buildGraphOfDependencies(List<Path> filesPath, Path rootPath) {
        var result = new DirectionalGraph<Path>();

        for (var file : filesPath) {
            result.AddVertexIfNotExist(file);

            String text = reader.ReadAll(file);

            Pattern pattern = Pattern.compile("require '([^']+)'");
            Matcher mathcer = pattern.matcher(text);

            while (mathcer.find()) {
                result.AddEdge(rootPath.resolve(mathcer.group(1)), file);
            }
        }
        return result;
    }

    private List<Path> getSubFiles(Path path) {

        if (Files.isRegularFile(path)) {
            if (!Files.isReadable(path)) {
                return List.of();
            }
            return List.of(path);
        }

        if (!Files.isDirectory(path)) {
            return List.of();
        }

        return getDirectoryFiles(path).stream()
                .map(this::getSubFiles)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<Path> getDirectoryFiles(Path path) {
        List<Path> result = new ArrayList<>();

        try (var list = Files.list(path)) {
            result = list.toList();
        } catch (IOException exception) {
            logger.Warnf("unable to list the contents of folder %s\n", path);
        }

        return result;
    }

}
