package main.java.graph;

import java.util.*;

/**
 * DirectionalGraph -- class which contains some function for work with directed graph.
 *
 * @param <Vertex> - type of vertex.
 */
public class DirectionalGraph<Vertex extends Comparable<Vertex>> {

    private static final int NOT_VISITED = 0;
    private static final int VISITED = 1;
    private static final int FINISHED = 2;

    private final Map<Vertex, List<Vertex>> graph = new HashMap<>();

    public DirectionalGraph() {
    }


    /**
     * Add new vertex to graph (ignore operation if it already exists).
     *
     * @param vertex — new vertex.
     */
    public void AddVertex(Vertex vertex) {
        if (graph.containsKey(vertex)) {
            return;
        }
        graph.put(vertex, new ArrayList<>());
    }


    /**
     * Add oriented edge from vertex `from` to vertex `to`.
     * If some vertex not exist, operation will be ignored.
     *
     * @param from beginning of oriented edge.
     * @param to   end of oriented edge.
     */
    public void AddEdge(Vertex from, Vertex to) {
        if (!graph.containsKey(to) || !graph.containsKey(from)) {
            return;
        }

        var neighbours = graph.get(from);
        neighbours.add(to);
        graph.put(from, neighbours);
    }


    /**
     * Tries to find a cycle. If such exist, it will be returned.
     * Otherwise, will be returned empty list.
     *
     * @return List of vertex, which forms cycle, if such exists. Or Empty, otherwise.
     */
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

    /**
     * Using dfs, tries to find a cycle(to meet VISITED vertex).
     * Also, it changes colors of vertex.
     *
     * @param current — vertex which will be proceeded in current function.
     * @param colors  — maps vertex to its type -- NOT_VISITED, VISITED, FINISHED.
     * @return true — if we meet VISITED vertex. false, otherwise.
     */
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

    /**
     * Does topological sort of current state of graph
     *
     * @return Vertex in topological order.
     */
    public List<Vertex> GetTopSort() {
        List<Vertex> result = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();
        var keys = getKeys();
        keys.sort(Comparator.reverseOrder());
        for (var vertex : keys) {
            processTopSort(vertex, visited, result);
        }
        Collections.reverse(result);
        return result;
    }


    /**
     * Auxiliary function to implement topsort using dfs (in O(n) time)
     *
     * @param current — current proceeded vertex
     * @param visited — already proceeded set of vertex
     * @param result  — topsort result
     */
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


    /**
     * List of vertex.
     *
     * @return List of vertex.
     */
    private List<Vertex> getKeys() {
        return graph.keySet().stream().toList();
    }
}
