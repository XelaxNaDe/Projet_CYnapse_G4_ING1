package projatlab.algorithms.generation;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import projatlab.model.Cell;
import projatlab.model.Maze;

/**
 * MazeGeneratorDFS is a maze generation algorithm that uses
 * Depth-First Search (DFS) to generate a random maze.
 */

public class MazeGeneratorDFS extends MazeGenerator {

    /** Reference to the maze being generated. */
    private Maze maze;
    
    /** Number of columns in the maze. */
    private final int cols;

    /** Number of rows in the maze. */
    private final int rows;

    /** Grid representing a list of all the cells in the maze. */
    private final ArrayList<Cell> grid;

    /** The current cell being processed in the DFS algorithm. */
    public Cell current;

     /** Stack to backtrack during DFS. */
    private final Stack<Cell> stack = new Stack<>();

    /** Random number generator for choosing neighbors. */
    private final Random rand;

    /**
     * Constructs a MazeGeneratorDFS instance.
     *
     * @param maze The maze to generate.
     * @param seed The seed for random number generation (for reproducibility).
     */
    public MazeGeneratorDFS(Maze maze, long seed) {

        this.maze = maze;
        this.cols = maze.getCols();
        this.rows = maze.getRows();
        this.grid = maze.getGrid();
        this.rand = new Random(seed);

        if (!grid.isEmpty()) {
            this.current = grid.get(0); // Start from the top-left cell
        }
    }
    
    /**
     * Performs a step in the maze generation process with the DFS maze generation algorithm.
     * Marks the current cell as visited and moves to a random unvisited neighbor,
     * if available. Uses a stack to backtrack when no unvisited neighbors remain.
     * And mark the end when all the cells have been visited. 
     */
    @Override
    public void step() {
        if (current == null || finished) return;


        if (!current.visited) {
            current.visited = true; 
        }

        Cell next = getUnvisitedNeighbor(current);

        if (next != null) {
            next.visited = true;
            stack.push(current); // Save current cell for backtracking 
            removeWalls(current, next); // Remove the wall bitween the current cell and next cell
            current = next; // Getting to the next cell
        } else if (!stack.isEmpty()) {
            current = stack.pop(); // Backtrack to the previous one
        } else {
            finished = true; // All the cells have been visited
        }
    }

    /**
     * Returns a random unvisited neighbor of a given cell.
     *
     * @param cell The reference cell.
     * @return A randomly selected unvisited neighbor or null if none exists.
     */
    private Cell getUnvisitedNeighbor(Cell cell) {
        ArrayList<Cell> neighbors = new ArrayList<>();

        int i = cell.i;
        int j = cell.j;

        Cell top = getCell(i, j - 1);
        Cell right = getCell(i + 1, j);
        Cell bottom = getCell(i, j + 1);
        Cell left = getCell(i - 1, j);

        if (top != null && !top.visited) neighbors.add(top); // If the top cell is unvisited and isnt null, add top in the neighbors list 
        if (right != null && !right.visited) neighbors.add(right); // If the right cell is unvisited and isnt null, add right in the neighbors list 
        if (bottom != null && !bottom.visited) neighbors.add(bottom); // If the bottom cell is unvisited and isnt null, add bottom in the neighbors list 
        if (left != null && !left.visited) neighbors.add(left); // If the left cell is unvisited and isnt null, add left in the neighbors list 

        if (!neighbors.isEmpty()) {
            int r = rand.nextInt(neighbors.size()); // Choose a random number
            return neighbors.get(r); // Return the neighbor choosed with the random number
        }
        return null; // If no more unvisited return null
    }

    /**
     * Returns the cell at a specific coordinate.
     *
     * @param i The column index.
     * @param j The row index.
     * @return The corresponding Cell object or null if out of bounds.
     */
    private Cell getCell(int i, int j) {
        if (i < 0 || j < 0 || i >= cols || j >= rows) return null;
        return grid.get(i + j * cols);
    }
}
