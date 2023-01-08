package main.java.application;

import main.java.graph.DirectionalGraph;
import main.java.logger.Logger;
import main.java.reader.FileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Main Business Application.
 */
public class Application {
    private final FileReader fileReader;
    private final Logger logger;

    public Application(Logger logger, FileReader fileReader) {
        this.fileReader = fileReader;
        this.logger = logger;
    }


    /**
     * processed business logic of application.
     *
     * @param rootDirectory — path to root directory.
     */
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

    /**
     * collect files to one graph. builds edge between files and their required files.
     *
     * @param filesPath — list of files to be proceeded.
     * @param rootPath  — path to root directory.
     * @return graph of files.
     */
    private DirectionalGraph<Path> buildGraphOfDependencies(List<Path> filesPath, Path rootPath) {
        var result = new DirectionalGraph<Path>();
        for (var file : filesPath) {
            result.AddVertex(file);
        }

        for (var file : filesPath) {
            String text = fileReader.ReadAll(file);

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

    /**
     * Function which returns paths to files and subfiles of given directory.
     *
     * @param directory — directory, from which we need files.
     * @return list of paths to files and subfiles.
     */
    private List<Path> getSubFiles(Path directory) {

        try (var stream = Files.walk(directory)) {
            return stream.filter(Files::isRegularFile)
                    .filter(Files::isReadable)
                    .toList();
        } catch (IOException exception) {
            logger.Warnf("unable to get subfiles of '%s'. io exception have been caught.", directory);
        } catch (SecurityException exception) {
            logger.Warnf("unable to get subfiles of '%s'. Some permission was not provided", directory);
        }

        return List.of();
    }

    /**
     * Collects content of all files to one String.
     *
     * @param files — list of files to be proceeded.
     * @return a String containing joined contents of all `files`.
     */
    private String collectAllText(List<Path> files) {
        return files.stream()
                .map(fileReader::ReadAll)
                .collect(
                        Collectors.joining(System.lineSeparator())
                );
    }

    /**
     * Write text to local file on path `pathToFile`.
     *
     * @param pathToFile — file to write.
     * @param text       — text to be written.
     */
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
