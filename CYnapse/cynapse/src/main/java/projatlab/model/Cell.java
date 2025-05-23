package projatlab.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * A Cell is a part of a grid-based maze.
 * Each cell has four walls,
 * and flags to indicate if it has been visited, if it's the start or the end a the maze,
 * or part of the path that is solution of the maze.
 * 
 * 
 */

public class Cell {

    /**Grid position in a maze */
    public int i, j;

    /** Walls of the cell: [top, right, bottom, left] */
    public boolean[] walls = {true, true, true, true};

    /** True if the cell has been visited */
    public boolean visited = false;

    /** True if this cell is the start of the maze */
    public boolean isStart = false;

    /** True if this cell is the end of the maze */
    public boolean isEnd = false;

    /** True if this cell is part of the final solution path */
    public boolean isInFinalPath = false;

    /** Width of the wall */
    public static int wallWidth = 2;

    /** Size of the cell in pixels */
    public static int cellSize = 20;


    /**
     * Creates a new cell at the given grid position.
     *
     * @param i column index
     * @param j row index
     */
    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
    }

    /**
     * Draws the cell on the screen using JavaFX.
     *
     * @param gc the graphics context to draw with
     */
    public void show(GraphicsContext gc) {
        double x = i * cellSize;
        double y = j * cellSize;


        if (isStart) {
            gc.setFill(Color.GREEN);
            gc.fillRect(x ,y , cellSize, cellSize);
        } else if (isEnd) {
            gc.setFill(Color.RED);
            gc.fillRect(x ,y ,cellSize, cellSize);
        } else if (visited) {
            gc.setFill(Color.web("FF77FF"));
            gc.fillRect(x , y,cellSize, cellSize);
        }
        if (isInFinalPath) {
            gc.setFill(Color.web("#0099FF"));
            gc.fillRect(x , y,cellSize, cellSize);
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(wallWidth);
        if (walls[0]) gc.strokeLine(x, y, x + cellSize, y);
        if (walls[1]) gc.strokeLine(x + cellSize, y, x + cellSize, y + cellSize);
        if (walls[2]) gc.strokeLine(x + cellSize, y + cellSize, x, y + cellSize);
        if (walls[3]) gc.strokeLine(x, y + cellSize, x, y);
    }

    /**
     * Creates a copy of this cell with the same position and wall states.
     *
     * @return a new Cell object
     */
    public Cell copy() {
        Cell copy = new Cell(i, j);
        copy.isStart = this.isStart;
        copy.isEnd = this.isEnd;
        System.arraycopy(this.walls, 0, copy.walls, 0, this.walls.length);
        return copy;
    }
   

    // Setters

    /** Set the cell as the start or not. */
    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }

    /** Set the cell as the end or not. */
    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    /** Set the cell as visited or not. */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}