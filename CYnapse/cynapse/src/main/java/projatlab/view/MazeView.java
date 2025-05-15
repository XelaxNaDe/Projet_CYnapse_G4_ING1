package projatlab.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import projatlab.model.Cell;

import java.util.ArrayList;

public class MazeView extends Pane {

    private final int cols;
    private final int rows;
    private final ArrayList<Cell> grid;
    private final GraphicsContext gc;
    private final Canvas canvas;

    public MazeView(projatlab.model.Maze maze) {
        this.cols = maze.getCols();
        this.rows = maze.getRows();
        this.grid = maze.getGrid();

        canvas = new Canvas(cols * Cell.cellSize, rows * Cell.cellSize);
        this.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();

    }

    public void draw() {

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth() -2, canvas.getHeight()-2);

        for (Cell cell : grid) {
            cell.show(gc);
        }
    }
}
