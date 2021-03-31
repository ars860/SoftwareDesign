package drawing;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class JavaAwtDrawing implements DrawingApi {
    private final int height;
    private final int width;
    private final Graphics2D ga;

    public JavaAwtDrawing(int height, int width, Graphics2D graphics2D) {
        this.height = height;
        this.width = width;
        this.ga = graphics2D;
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
        ga.setPaint(Color.GREEN);
        ga.fill(new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius));
    }

    @Override
    public void drawLine(double x1, double y1, double x2, double y2) {
        ga.setPaint(Color.BLACK);
        ga.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }
}
