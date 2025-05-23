package projatlab.algorithms.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import projatlab.model.Cell;
import projatlab.model.Maze;

/** Implements the Prim's algorithm
 * Starts from a random cell, connects it to a visted neighbor
 * and adds its unvisted neigbors to the frontier
 */
public class MazeGeneratorPrim extends MazeGenerator {

    /** The maze to solve. */
    private Maze maze;

    /** List of frontier cells (unvisited cells adjacent to visited ones) */
    private final List<Cell> frontier = new ArrayList<>();

    /** Random number generator used for choosing random cells */
    private final Random rand;

    /** Number of visited cells */
    private int visitedCount = 0;

    /** Constructs a maze with the Prim generator and a seed
     * @param maze the maze to generate
     * @param seed the seed for random number generation
     */
    public MazeGeneratorPrim(Maze maze, long seed) {

        this.maze = maze;
        this.rand = new Random(seed);

        // Start from a random cell
        int startIndex = rand.nextInt(maze.getGrid().size());
        Cell start = maze.getGrid().get(startIndex);
        start.visited = true;
        visitedCount++;

        // Add its unvisted neignors to the frontier list
        frontier.addAll(getUnvisitedNeighbors(start));
    }

    /** Performs one step of the Prim maze generation algorithm */

    @Override
    public void step() {
        // Stop if all the cells have been visited or if no frontier cells remain
        if (visitedCount >= maze.getCols() * maze.getRows() || frontier.isEmpty()) {
            return;
        }

        // Pick a random frontier cell
        int ind = rand.nextInt(frontier.size());
        Cell current = frontier.remove(ind);
        current.visited = true;
        visitedCount++;

        // Connect it to one of its already visited neigbors
        List<Cell> visitedNeighbors = getVisitedNeighbors(current);
        if (!visitedNeighbors.isEmpty()) {
            Cell neighbor = visitedNeighbors.get(rand.nextInt(visitedNeighbors.size()));
            removeWalls(current, neighbor);
        }

        // Add its unvisited neigbors to the frontier 
        for (Cell n : getUnvisitedNeighbors(current)) {
            if (!frontier.contains(n)) {
                frontier.add(n);
            }
        }
    }

    /** Return a list of all unvisited neighboring cells of a given cell
     * @param Cell : a cell
     * @return List<Cell> : unvisited neighbors of the Cell cell 
     */
    private List<Cell> getUnvisitedNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
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

        return neighbors;
    }

    /** Return a list of all visited neighboring cells of a given cell
     * @param Cell : a cell
     * @return List<Cell> : visited neighbors of the Cell cell 
     */
    private List<Cell> getVisitedNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int i = cell.i;
        int j = cell.j;

        Cell top = maze.getCell(maze.index(i, j - 1));
        Cell right = maze.getCell(maze.index(i + 1, j));
        Cell bottom = maze.getCell(maze.index(i, j + 1));
        Cell left = maze.getCell(maze.index(i - 1, j));

        if (top != null && top.visited) neighbors.add(top);
        if (right != null && right.visited) neighbors.add(right);
        if (bottom != null && bottom.visited) neighbors.add(bottom);
        if (left != null && left.visited) neighbors.add(left);

        return neighbors;
    }
    
    /** Return true if the maze generation is complete */

    @Override
    public boolean isFinished() {
        return frontier.isEmpty();
    }

}
