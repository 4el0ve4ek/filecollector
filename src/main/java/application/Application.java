package main.java.application;

import main.java.graph.DirectionalGraph;
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

    public Application(Reader reader) {
        this.reader = reader;
    }

    public void Process(String rootDirectory) {
        var rootPath = Path.of(rootDirectory);

        if (!Files.exists(rootPath)) {
            System.out.printf("file or directory '%s' not exist\n", rootDirectory);
            return;
        }

        var allFiles = getSubFiles(rootPath);

//        allFiles = allFiles.stream().map(path -> rootPath.relativize(path.normalize())).toList();

        var dependencyGraph = buildGraphOfDependencies(allFiles, rootPath);

        var cycle = dependencyGraph.GetCycle();
        if (!cycle.isEmpty()) {
            System.out.println("there is a cycle dependency! abort execution!");
            cycle.forEach(System.out::println);
            return;
        }

        dependencyGraph.GetTopSort().forEach(System.out::println);
    }

    private DirectionalGraph<Path> buildGraphOfDependencies(List<Path> filesPath, Path rootPath) {
        var result = new DirectionalGraph<Path>();
        for (var file : filesPath) {
            result.AddVertexIfNotExist(file);
            String text = "";

            try {
                text = Files.readString(file);
            } catch (IOException e) {
                System.out.printf("unable to read file '%s'\n", file);
            }

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
            System.out.printf("unable to list the contents of folder %s\n", path);
        }

        return result;
    }

}
