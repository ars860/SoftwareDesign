package app;

import drawing.JavaFxDrawing;
import graph.Graph;
import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFxApp extends Application implements App {
    @Override
    public void start(Stage stage) {
        JavaFxDrawing javaFxDrawing = new JavaFxDrawing(stage, 500, 500);
        Graph graph = Main.graph;
        graph.setDrawingApi(javaFxDrawing);
        graph.drawGraph();
    }

    @Override
    public void startApp() {
        launch();
    }
}
