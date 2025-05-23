package projatlab.algorithms.generators;

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
    private final Maze maze;

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
        this.rand = new Random(seed);

        if (!maze.getGrid().isEmpty()) {
            this.current = maze.getGrid().get(0);
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
            stack.push(current);  
            removeWalls(current, next); 
            current = next; 
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

        Cell top = maze.getCell(maze.index(i, j - 1));
        Cell right = maze.getCell(maze.index(i + 1, j));
        Cell bottom = maze.getCell(maze.index(i, j + 1));
        Cell left = maze.getCell(maze.index(i - 1, j));

        if (top != null && !top.visited) neighbors.add(top); 
        if (right != null && !right.visited) neighbors.add(right);
        if (bottom != null && !bottom.visited) neighbors.add(bottom); 
        if (left != null && !left.visited) neighbors.add(left); 

        if (!neighbors.isEmpty()) {
            int r = rand.nextInt(neighbors.size());
            return neighbors.get(r); // Return the neighbor choosed with the random number
        }
        return null; // If no more unvisited return null
    }
}
