package main.java.application;

import main.java.graph.DirectionalGraph;
import main.java.logger.Logger;
import main.java.reader.Reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

        var resultFiles = dependencyGraph.GetTopSort();

        logger.Info("\nProcess file in following order:");
        resultFiles.forEach(logger::Warn);

        var resultText = collectAllText(resultFiles);
        Path outputPath = rootPath.getParent().resolve("result.txt");

        logger.Infof("\nCollecting result here '%s'", outputPath);

        writeTextToFile(outputPath, resultText);
    }

    private DirectionalGraph<Path> buildGraphOfDependencies(List<Path> filesPath, Path rootPath) {
        var result = new DirectionalGraph<Path>();
        for (var file : filesPath) {
            result.AddVertexIfNotExist(file);
        }

        for (var file : filesPath) {
            String text = reader.ReadAll(file);

            Pattern pattern = Pattern.compile("require '([^']+)'");
            Matcher mathcer = pattern.matcher(text);

            while (mathcer.find()) {
                Path requiredFile;
                try {
                    requiredFile = rootPath.resolve(mathcer.group(1));
                } catch (InvalidPathException exception) {
                    logger.Warnf("unable to resolve path of require '%s'", mathcer.group(1));
                    continue;
                }
                result.AddEdge(requiredFile, file);
            }
        }
        return result;
    }

    private List<Path> getSubFiles(Path path) {

        try (var stream = Files.walk(path)) {
            return stream.filter(Files::isRegularFile)
                    .filter(Files::isReadable)
                    .toList();
        } catch (IOException exception) {
            logger.Warnf("unable to get subfiles of '%s'. io exception have been caught.", path);
        } catch (SecurityException exception) {
            logger.Warnf("unable to get subfiles of '%s'. Some permission was not provided", path);
        }

        return List.of();
    }

    private String collectAllText(List<Path> files) {
        return files.stream()
                .map(reader::ReadAll)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private void writeTextToFile(Path pathToFile, String text) {
        try {
            Files.writeString(pathToFile, text);
        } catch (IOException e) {
            logger.Warnf("unable to write to file '%s'. io exception have been caught.", pathToFile);
        } catch (SecurityException e) {
            logger.Warnf("unable to write to file '%s'. Some permission was not provided", pathToFile);
        }
    }
}
