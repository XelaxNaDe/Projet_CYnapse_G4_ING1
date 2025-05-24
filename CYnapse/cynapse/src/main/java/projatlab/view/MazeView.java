package projatlab.view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import projatlab.controller.ModificationController;
import projatlab.model.Cell;
import projatlab.model.Maze;

/**
 * MazeView is a JavaFX component that visually represents a maze on a canvas.
 * It is responsible for rendering cells, managing mouse interactions, and 
 * optionally displaying visited cells for visualization purposes.
 */
public class MazeView extends Pane {

    /** Number of columns in the maze. */
    private final int cols;

    /** Number of rows in the maze. */
    private final int rows;

    /** List of all cells composing the maze grid. */
    private final ArrayList<Cell> grid;

    /** Graphics context used to draw on the canvas. */
    private final GraphicsContext gc;

    /** The drawing surface for the maze. */
    private final Canvas canvas;

    /** Controller used for handling mouse click events for maze modification. */
    private ModificationController modController;

    /** Flag indicating whether visited cells are shown or hidden. */
    private boolean showvisited = true;

     /**
     * Constructs a MazeView object from a Maze model.
     *
     * @param maze The Maze instance containing grid and dimension data.
     */
    public MazeView(Maze maze) {
        this.cols = maze.getCols();
        this.rows = maze.getRows();
        this.grid = maze.getGrid();

        canvas = new Canvas(cols * Cell.cellSize, rows * Cell.cellSize);
        this.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();

        updateCanvasSize();
    }

    /**
     * Draws the entire maze including cells and outer walls.
     * Uses individual cells' {@code show()} method for rendering.
     */
    public void draw() {

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth()-2, canvas.getHeight()-2);

        for (Cell cell : grid) {
            cell.show(gc);
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);

        double w = cols * Cell.cellSize;
        double h = rows * Cell.cellSize;

        
        gc.strokeLine(0, 0, w, 0); // Top wall
        gc.strokeLine(w, 0, w, h); // Right wall
        gc.strokeLine(w, h, 0, h); // Bottom wall
        gc.strokeLine(0, h, 0, 0); // Left wall
    }

    /**
     * Binds a controller to handle mouse click events on the maze canvas.
     *
     * @param controller The controller to handle modification events.
     */
    public void setController(ModificationController controller) {
        this.modController = controller;
        canvas.setOnMouseClicked(e -> {
            if (modController != null) {
                modController.handleClick(e.getX(), e.getY());
            }
        });
    }

    /**
     * Updates the canvas size based on the current number of rows and columns,
     * and redraws the maze to fit the new dimensions.
     */
    public void updateCanvasSize() {
        canvas.setWidth(cols * Cell.cellSize);
        canvas.setHeight(rows * Cell.cellSize);
        draw();
    }

    /**
     * Toggles the visibility of visited cells.
     * If currently visible, they are cleared. If hidden, restores them from the provided list.
     *
     * @param visitedCells List of cells previously marked as visited.
     * @return The new state of the {@code showvisited} flag.
     */
    public boolean toggleVisited(List<Cell> visitedCells){
        if (showvisited == true) {
            for (Cell cell:grid) {
                if (cell.visited) {
                    cell.visited = false;
                }
            }
            showvisited = false; 
            draw();
            return showvisited;
        }
        else{
            for (Cell cell:visitedCells) {
                cell.visited = true;
            }
            showvisited = true; 
            draw();
            return showvisited;
        }
    }

    /**
     * Explicitly sets whether visited cells should be shown on the canvas.
     *
     * @param show {@code true} to show visited cells; {@code false} to hide them.
     */
    public void setShowVisited(boolean show) {
        this.showvisited = show;
    }
}
