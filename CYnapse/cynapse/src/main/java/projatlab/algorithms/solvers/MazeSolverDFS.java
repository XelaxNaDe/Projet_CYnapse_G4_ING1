package projatlab.algorithms.solvers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import projatlab.model.Cell;
import projatlab.model.Maze;

/**
 * MazeSolverDFS implements the Depth-First Search (DFS) algorithm
 * to solve a maze by exploring paths and backtracking when necessary.
 */
public class MazeSolverDFS extends MazeSolver{

    /** The maze to solve. */
    private Maze maze;

    /** Random number generator for selecting neighbors randomly. */
    private Random rand = new Random(System.currentTimeMillis());

    /** Stack used for DFS backtracking. */
    private Stack<Cell> stack = new Stack<>();

    /** List of visited path cells. */
    private ArrayList<Cell> path = new ArrayList<>();

    /** The current cell being visited during the algorithm. */
    public Cell current;

    /** Counter to keep track of the number of visited cells. */
    private int visitedCount = 0;

    /**
     * Constructs a MazeSolverDFS object with the given maze.
     *
     * @param maze The Maze object to solve.
     */
    public MazeSolverDFS(Maze maze) {

        this.maze = maze;

        if (!maze.getGrid().isEmpty()) {
            this.current = maze.getStart(); // Begin at the starting point choosed randomly 
        }
    }

    /**
     * Executes one step of the DFS algorithm to attempt solving the maze.
     * Visits neighbors if accessible and continues until the end cell is reached.
     */
    public void step() {
        if (current == null) return;

        if (!current.visited) {
            current.visited = true;
            visitedCount++;
        }

        // Check if the end cell is reached
        if (current == maze.getEnd()) {
            finished = true;
            finalPath.clear();
            finalPath.addAll(stack); 
            finalPath.add(current); 

            for (Cell c : finalPath) {
                if (c != maze.getStart() && c != maze.getEnd()) {
                    c.isInFinalPath = true;
                }
            }

            return;
        }

        // Move to an accessible and unvisited neighbor
        Cell next = getAccessibleNeighbor(current, rand);
        if (next != null) {
            stack.push(current); 
            path.add(current);   
            current = next;      
        } else if (!stack.isEmpty()) {
            current = stack.pop(); // Backtrack if no options
        }
    }
    
    /**
     * Gets an accessible (not wall between and unvisited) neighbor of a cell.
     *
     * @param cell The current cell.
     * @param rand Random instance for selection.
     * @return A random unvisited accessible neighbor or null if none exist.
     */
    private Cell getAccessibleNeighbor(Cell cell, Random rand) {
        int[] dx = {0, 1, 0, -1}; // Horizontal directions (left/right) 
        int[] dy = {-1, 0, 1, 0}; // Vertical directions (top/bottom)
        List<Cell> accessibleNeighbors = new ArrayList<>();

        for (int d = 0; d < 4; d++) {
            if (!cell.walls[d]) { // Check if there's no wall in this direction 
                int ni = cell.i + dx[d]; 
                int nj = cell.j + dy[d];
                int index = maze.index(ni, nj); 
                if (index != -1) {
                    Cell neighbor = maze.getGrid().get(index);
                    if (!neighbor.visited) {
                        accessibleNeighbors.add(neighbor);
                    }
                }
            }
        }

        if (!accessibleNeighbors.isEmpty()) {
            return accessibleNeighbors.get(rand.nextInt(accessibleNeighbors.size()));
        }

        return null;
    } 

    /**
     * Returns the number of visited cells during the solving process.
     *
     * @return Number of visited cells.
     */
    @Override
    public int getVisitedCount() {
        return visitedCount;
    }
}
