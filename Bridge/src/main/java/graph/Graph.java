package graph;

import drawing.DrawingApi;

public abstract class Graph {
    protected int verticesCount;
    protected DrawingApi drawingApi;

    public void setDrawingApi(DrawingApi drawingApi) {
        this.drawingApi = drawingApi;
    }

    public Graph(int verticesCount) {
        this.verticesCount = verticesCount;
    }

    protected double getRadius() {
        return Math.min(drawingApi.getDrawingAreaHeight(), drawingApi.getDrawingAreaWidth()) * 0.25;
    }

    protected Pair<Double, Double> getVertexCoordinates(int v) {
        return new Pair<>(
                drawingApi.getDrawingAreaWidth() / 2.0 + getRadius() * Math.cos(2 * Math.PI / verticesCount * v),
                drawingApi.getDrawingAreaHeight() / 2.0 + getRadius() * Math.sin(2 * Math.PI / verticesCount * v)
        );
    }

    protected void drawVertices() {
        for (int i = 0; i < verticesCount; i++) {
            drawingApi.drawCircle(
                    drawingApi.getDrawingAreaWidth() / 2.0 + getRadius() * Math.cos(2 * Math.PI / verticesCount * i),
                    drawingApi.getDrawingAreaHeight() / 2.0 + getRadius() * Math.sin(2 * Math.PI / verticesCount * i),
                    5
            );
        }
    }

    protected void drawEdge(int v1, int v2) {
        Pair<Double, Double> v1Coordinates = getVertexCoordinates(v1);
        Pair<Double, Double> v2Coordinates = getVertexCoordinates(v2);

        drawingApi.drawLine(v1Coordinates.first, v1Coordinates.second, v2Coordinates.first, v2Coordinates.second);
    }

    public abstract void drawGraph();
}
