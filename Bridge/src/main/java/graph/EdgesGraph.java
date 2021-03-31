package graph;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EdgesGraph extends Graph {

    private final List<Pair<Integer, Integer>> edges;

    public EdgesGraph(int verticesCount, List<Pair<Integer, Integer>> edges) {
        super(verticesCount);
        this.edges = edges;
    }

    static public EdgesGraph parseGraph(String fileName) throws IOException {
        try (Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(fileName))))) {
            int vertices = scanner.nextInt();
            int edgesCnt = scanner.nextInt();

            List<Pair<Integer, Integer>> edges = new ArrayList<>();

            for (int i = 0; i < edgesCnt; i++) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();

                edges.add(new Pair<>(x - 1, y - 1));
            }

            return new EdgesGraph(vertices, edges);
        }
    }

    public void drawGraph() {
        for (Pair<Integer, Integer> edge : edges) {
            drawEdge(edge.first, edge.second);
        }
        drawVertices();
    }
}
