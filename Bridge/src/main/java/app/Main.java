package app;

import graph.EdgesGraph;
import graph.Graph;
import graph.MatrixGraph;

import java.io.IOException;

public class Main {
    static public Graph graph;
    static public App app;

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new RuntimeException("Wrong amount of arguments");
        }

        String api = args[0];
        String graphType = args[1];
        String graphFile = args[2];

        switch (api) {
            case "fx":
                app = new JavaFxApp();
                break;
            case "awt":
                app = new JavaAwtApp();
                break;
            default:
                throw new RuntimeException("Unknown API: " + api);
        }

        try {
            switch (graphType) {
                case "list":
                    graph = EdgesGraph.parseGraph(graphFile);
                    break;
                case "matrix":
                    graph = MatrixGraph.parseGraph(graphFile);
                    break;
                default:
                    throw new RuntimeException("Unknown graph type: " + graphType);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't read graph.", e);
        }

        app.startApp();
    }
}
