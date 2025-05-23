package projatlab.view;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import projatlab.controller.ModificationController;
import projatlab.model.Cell;
import projatlab.model.Maze;

public class MazeView extends Pane {

    private final int cols;
    private final int rows;
    private final ArrayList<Cell> grid;
    private final GraphicsContext gc;
    private final Canvas canvas;
    private ModificationController modController;

    public MazeView(Maze maze) {
        this.cols = maze.getCols();
        this.rows = maze.getRows();
        this.grid = maze.getGrid();

        canvas = new Canvas(cols * Cell.cellSize, rows * Cell.cellSize);
        this.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();

        updateCanvasSize();
    }


    public void draw() {

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth()-2, canvas.getHeight()-2);

        for (Cell cell : grid) {
            cell.show(gc);
        }

        gc.setStroke(Color.BLACK); // ou une autre couleur de mur
        gc.setLineWidth(4);

        double w = cols * Cell.cellSize;
        double h = rows * Cell.cellSize;

        // Mur haut
        gc.strokeLine(0, 0, w, 0);
        // Mur droite
        gc.strokeLine(w, 0, w, h);
        // Mur bas
        gc.strokeLine(w, h, 0, h);
        // Mur gauche
        gc.strokeLine(0, h, 0, 0);
    }

    public void setController(ModificationController controller) {
        this.modController = controller;
        canvas.setOnMouseClicked(e -> {
            if (modController != null) {
                modController.handleClick(e.getX(), e.getY());
            }
        });
    }

    public void updateCanvasSize() {
        canvas.setWidth(cols * Cell.cellSize);
        canvas.setHeight(rows * Cell.cellSize);
        draw();
    }
}
