package projatlab.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Maze class represents a rectangular grid of cells used in maze generation and solving.
 * It stores the structure of the maze including the start and end cells and provides utility
 * methods for indexing and copying the maze.
 */
public class Maze {
    /** Number of rows in the maze. */
    private final int rows;
    
    /** Number of columns in the maze. */
    private final int cols;
    
    /** List representing the grid of cells */
    private ArrayList<Cell> grid;
    
    /** Starting cell of the maze. */
    private Cell startCell;
    
    /** Ending cell of the maze. */
    private Cell endCell;

    /**
     * Constructs a Maze with the specified number of columns and rows.
     * Initializes the grid with new Cell objects.
     *
     * @param cols Number of columns.
     * @param rows Number of rows.
     */
    public Maze(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.grid = new ArrayList<>();

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                grid.add(new Cell(i, j));
            }
        }
    }

    /**
     * Converts 2D grid coordinates (i, j) into a 1D index for the grid list.
     *
     * @param i Column index.
     * @param j Row index.
     * @return Index in the grid list or -1 if out of bounds.
     */
    public int index(int i, int j) {
        if (i < 0 || j < 0 || i >= cols || j >= rows) {
            return -1;
        }
        return i + j * cols;
    }

    /**
     * Sets the start cell of the maze.
     *
     * @param cell The cell to designate as the starting point.
     */
    public void setStart(Cell cell) {
        this.startCell = cell;
    }

    /**
     * Sets the end cell of the maze.
     *
     * @param cell The cell to designate as the ending point.
     */
    public void setEnd(Cell cell) {
        this.endCell = cell;
    }

    /**
     * Retrieves a cell from the grid based on its 1D index.
     *
     * @param index The index in the grid list.
     * @return The corresponding Cell object or null if the index is -1.
     */
    public Cell getCell(int index){
        if (index == -1) return null;
        return grid.get(index);
    }

    /**
     * Returns the number of columns in the maze.
     *
     * @return Column count.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Returns the number of rows in the maze.
     *
     * @return Row count.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the grid of cells in the maze.
     *
     * @return List of Cell objects.
     */
    public ArrayList<Cell> getGrid() {
        return grid;
    }

    /**
     * Returns the starting cell of the maze.
     *
     * @return Start Cell.
     */
    public Cell getStart() {
        return startCell;
    }

    /**
     * Returns the ending cell of the maze.
     *
     * @return End Cell.
     */
    public Cell getEnd() {
        return endCell;
    }

    /**
     * Creates and returns a deep copy of this Maze object.
     *
     * @return A new Maze instance with copied Cell objects.
     */
    public Maze copy() {
        Maze copyMaze = new Maze(cols, rows);
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                Cell original = getCell(index(i,j));
                Cell copyCell = original.copy();
                copyMaze.getGrid().set(copyMaze.index(i, j), copyCell);
            }
        }
        return copyMaze;
    }

    public Cell getRandomCell(Random rand) {
        List<Cell> cells = getGrid();
        return cells.get(rand.nextInt(cells.size()));
    }

}
