package graph;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MatrixGraph extends Graph {
    private final Boolean[][] matrix;

    public MatrixGraph(Boolean[][] matrix) {
        super(matrix.length);
        this.matrix = matrix;
    }


    static public MatrixGraph parseGraph(String fileName) throws IOException {
        try (Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(fileName))))) {
            int vertices = scanner.nextInt();

            Boolean[][] matrix = new Boolean[vertices][vertices];

            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    boolean current = scanner.nextInt() == 1;

                    matrix[i][j] = current;
                }
            }

            return new MatrixGraph(matrix);
        }
    }

    @Override
    public void drawGraph() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] || matrix[j][i]) {
                    drawEdge(i, j);
                }
            }
        }
        drawVertices();
    }
}
