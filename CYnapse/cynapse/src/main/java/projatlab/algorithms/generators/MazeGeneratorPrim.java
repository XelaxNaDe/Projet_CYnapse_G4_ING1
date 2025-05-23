package projatlab.algorithms.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import projatlab.model.Cell;
import projatlab.model.Maze;

/** Implements Prim's algorithm
 * Starts from a random cell, connects it to a visted neighbor
 * and adds its unvisited neigbors to the frontier
 */
public class MazeGeneratorPrim extends MazeGenerator {

    /** List of frontier cells (unvisited cells adjacent to visited ones) */
    private final List<Cell> frontier = new ArrayList<>();

    /** Number of visited cells */
    private int visitedCount = 0;

    /** Constructs a maze with the Prim generator and a seed
     * @param maze the maze to generate
     * @param seed the seed for random number generation
     */
    public MazeGeneratorPrim(Maze maze, long seed, Boolean isPerfect) {

        this.maze = maze;
        this.isPerfect = isPerfect;
        this.rand = new Random(seed);

        // Start from a random cell
        int startIndex = rand.nextInt(maze.getGrid().size());
        Cell start = maze.getGrid().get(startIndex);
        start.visited = true;
        visitedCount++;

        // Add its unvisited neignors to the frontier list
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


    
    /** Return true if the maze generation is complete */

    @Override
    public boolean isFinished() {
        finished = frontier.isEmpty();
        return finished;
    }

}
