package drawing;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JavaFxDrawing implements DrawingApi {
    private final Stage stage;
    private final int height;
    private final int width;
    private final Canvas canvas;
    private final GraphicsContext gc;

    public JavaFxDrawing(Stage stage, int height, int width) {
        this.stage = stage;
        this.height = height;
        this.width = width;

        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        Group group = new Group();
        group.getChildren().add(canvas);
        stage.setScene(new Scene(group, Color.WHITE));
        stage.show();
    }

    @Override
    public long getDrawingAreaWidth() {
        return width;
    }

    @Override
    public long getDrawingAreaHeight() {
        return height;
    }

    @Override
    public void drawCircle(double x, double y, double radius) {
        gc.setFill(Color.GREEN);
        gc.fillOval(
                x - radius,
                y - radius,
                2 * radius,
                2 * radius
        );
    }

    @Override
    public void drawLine(double x1, double y1, double x2, double y2) {
        gc.strokeLine(x1, y1, x2, y2);
    }
}
