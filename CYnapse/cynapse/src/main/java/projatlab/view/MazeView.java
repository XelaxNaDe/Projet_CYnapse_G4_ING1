package projatlab.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import projatlab.model.Cell;
import projatlab.model.Maze;
import projatlab.algorithms.generation.dfs;

import java.util.ArrayList;


public class MazeView extends Pane {

    private static final int cellSize = Cell.cellSize;
    private final int cols;
    private final int rows;
    private final ArrayList<Cell> grid;
    private final dfs generator;
    private final GraphicsContext gc;

    public MazeView(Maze maze) {
        this.cols = maze.getcols();
        this.rows = maze.getrows();
        this.grid = maze.getGrid();

        Canvas canvas = new Canvas(cols * cellSize, rows * cellSize);
        this.getChildren().add(canvas);
        this.gc = canvas.getGraphicsContext2D();

        generator = new dfs(grid);

        startAnimation();
    }

    private void startAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        };
        timer.start();
    }

    private void draw() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, cols * cellSize, rows * cellSize);

        for (Cell cell : grid) {
            cell.show(gc, cellSize);
        }

        generator.step(cols, rows);
    }
}
