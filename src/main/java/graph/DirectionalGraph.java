package main.java.graph;

import java.util.*;

/**
 * DirectionalGraph -- класс содержащий функции для работы с ориентированными графами
 *
 * @param <Vertex> - тип вершин
 */
public class DirectionalGraph<Vertex extends Comparable<Vertex>> {

    private static final int NOT_VISITED = 0;
    private static final int VISITED = 1;
    private static final int FINISHED = 2;

    private final Map<Vertex, List<Vertex>> graph = new HashMap<>();

    public DirectionalGraph() {
    }

    public void AddVertexIfNotExist(Vertex vertex) {
        if (graph.containsKey(vertex)) {
            return;
        }
        graph.put(vertex, new ArrayList<>());
    }

    public void AddEdge(Vertex from, Vertex to) {
        if (!graph.containsKey(to) || !graph.containsKey(from)) {
            return;
        }

        var neighbours = graph.get(from);
        neighbours.add(to);
        graph.put(from, neighbours);
    }

    public List<Vertex> GetCycle() {
        if (graph.isEmpty()) {
            return List.of();
        }
        Map<Vertex, Integer> colors = new HashMap<>();

        for (var vertex : getKeys()) {
            colors.put(vertex, NOT_VISITED);
        }

        for (var vertex : getKeys()) {
            Integer vertexColor = colors.get(vertex);
            if (vertexColor == NOT_VISITED && checkCycles(vertex, colors)) {

                return colors.entrySet()
                        .stream()
                        .filter(x -> x.getValue() == VISITED)
                        .map(Map.Entry::getKey)
                        .toList();
            }
        }
        return List.of();
    }

    private boolean checkCycles(Vertex current, Map<Vertex, Integer> colors) {
        colors.put(current, VISITED);
        for (var next : graph.get(current)) {
            int nextVertexColor = colors.get(next);
            if (nextVertexColor == FINISHED) {
                continue;
            } else if (nextVertexColor == VISITED) {
                return true;
            }
            if (checkCycles(next, colors)) {
                return true;
            }
        }
        colors.put(current, FINISHED);
        return false;
    }

    public List<Vertex> GetTopSort() {
        List<Vertex> result = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();
        for (var vertex : getKeys()) {
            processTopSort(vertex, visited, result);
        }
        Collections.reverse(result);
        return result;
    }

    private void processTopSort(Vertex current, Set<Vertex> visited, List<Vertex> result) {
        if (visited.contains(current)) {
            return;
        }
        visited.add(current);
        for (var next : graph.get(current)) {
            if (visited.contains(next)) {
                continue;
            }
            processTopSort(next, visited, result);
        }
        result.add(current);
    }


    private List<Vertex> getKeys() {
        return graph.keySet().stream().sorted(Comparator.reverseOrder()).toList();
    }
}
