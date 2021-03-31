package app;

import drawing.JavaAwtDrawing;
import graph.Graph;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JavaAwtApp extends Frame implements App {
    @Override
    public void startApp() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        setSize(500, 500);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        JavaAwtDrawing drawing = new JavaAwtDrawing(500, 500, (Graphics2D) g);
        Graph graph = Main.graph;
        graph.setDrawingApi(drawing);
        graph.drawGraph();
    }
}
