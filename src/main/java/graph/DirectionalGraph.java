package main.java.graph;

import java.util.*;

public class DirectionalGraph<Vertex> {
    Map<Vertex, List<Vertex>> graph = new HashMap<>();

    public DirectionalGraph() {
    }

    public void AddEdge(Vertex from, Vertex to) {
        var neighbours = graph.get(from);
        if (neighbours == null) {
            neighbours = new ArrayList<>();
        }
        neighbours.add(to);
        graph.put(from, neighbours);
    }

    public boolean HasCycles() {
        if (graph.isEmpty()) {
            return false;
        }
        Set<Vertex> visited = new HashSet<>();
        for (var vertex : getKeys()) {
            if (!visited.contains(vertex) && checkCycles(vertex, null, visited)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCycles(Vertex current, Vertex parent, Set<Vertex> visited) {
        if (visited.contains(current)) {
            return true;
        }
        visited.add(current);
        for (var next : graph.get(current)) {
            if (next.equals(parent)) {
                continue;
            }
            if (checkCycles(next, current, visited)) {
                return true;
            }
        }
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
            if (visited.contains(current)) {
                continue;
            }
            processTopSort(next, visited, result);
        }
        result.add(current);
    }


    private List<Vertex> getKeys() {
        return new ArrayList<>(graph.keySet());
    }
}
