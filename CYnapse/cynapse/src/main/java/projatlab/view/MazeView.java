package projatlab.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import projatlab.model.Maze;

public class MazeView  extends Pane{
    Maze maze;
    int width = 600;
    int height= 600;
    GraphicsContext gc;
    
    public void show() {
        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
    }
}
