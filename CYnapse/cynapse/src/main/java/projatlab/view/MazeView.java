package projatlab.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import projatlab.algorithms.generation.dfs;
import projatlab.model.Cell;

import java.util.ArrayList;

public class MazeView extends Pane {

    private final int cols;
    private final int rows;
    private final ArrayList<Cell> grid;
    private final dfs generator;
    private final GraphicsContext gc;
    private final Canvas canvas;

    public MazeView(projatlab.model.Maze maze) {
        this.cols = maze.getcols();
        this.rows = maze.getrows();
        this.grid = maze.getGrid();

        canvas = new Canvas(cols * Cell.cellSize, rows * Cell.cellSize);
        this.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();
        generator = new dfs(grid);

        startAnimation();
    }

    private void startAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                generator.step(cols, rows);
                draw();
            }
        };
        timer.start();
    }

    public void draw() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Cell cell : grid) {
            cell.show(gc);
        }
    }
}
