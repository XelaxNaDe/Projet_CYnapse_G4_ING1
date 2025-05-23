package projatlab.algorithms.solvers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import projatlab.model.Cell;
import projatlab.model.Maze;

/** Implements the Dijkstra algorithm to find the shortest path
 *  in a maze from the start cell to the end cell using a priority queue (to keep the distance)
 */
public class MazeSolverDijkstra extends MazeSolver {

    /** The maze to solve. */
    private Maze maze;

    /** The grid of all cells in the maze. */
    private ArrayList<Cell> grid;
 
    /** Priority queue to process the cells with the smallest distance first */
    private PriorityQueue<CellDistance> queue;

    /** Map to reconstruct the path */
    private Map<Cell, Cell> cameFrom;

    /** Stores the shortest known distance to each cell */
    private Map<Cell, Integer> distance;

    /** Keeps track of already visited cells */
    private Set<Cell> visited;

    /** The current cell being visited during the algorithm. */
    private Cell current;

    /** The number of visited cells */
    private int visitedCount = 0;

    /** Initializes the solver with the maze and prepares the data structures */
    public MazeSolverDijkstra(Maze maze) {
        this.maze = maze;
        this.grid = maze.getGrid();

        // Initialize the priority queue with custom comparator based on distance 

        this.queue = new PriorityQueue<>(Comparator.comparingInt(cd -> cd.distance));
        this.cameFrom = new HashMap<>();
        this.distance = new HashMap<>();
        this.visited = new HashSet<>();

        // Set initial distances to infinity 
        for (Cell c : grid) {
            distance.put(c, Integer.MAX_VALUE);
        }

        // Set the start cell distance to 0 and add it to the queue 
        current = maze.getStart();
        distance.put(current, 0);
        queue.add(new CellDistance(current, 0));
    }

    /** Executes one step of the Dijkstra algorithm */

    @Override
    public void step() {
        if (finished || queue.isEmpty()) return;

        // Take the cell with the smallest distance 
        CellDistance cd = queue.poll();
        current = cd.cell;

        // Skip if already visited 
        if (visited.contains(current)) return;

        visited.add(current);
        current.visited = true;
        visitedCount++;

        // If the end cell is reached, stop and reconstruct the path 
        if (current == maze.getEnd()) {
            reconstructPath();
            finished = true;
            return;
        }

        // Check neigbors 
        for (Cell neighbor : getAccessibleNeighbors(current)) {
            int newDist = distance.get(current) + 1;
            if (newDist < distance.get(neighbor)) {
                distance.put(neighbor, newDist);
                cameFrom.put(neighbor, current);
                queue.add(new CellDistance(neighbor, newDist));
            }
        }
    }

    /** Reconstruct the shortest path from end to start using the cameFrom map 
     *  Marks each cell on the final path
     */
    private void reconstructPath() {
        finalPath.clear();
        Cell step = maze.getEnd();
        while (step != null) {
            finalPath.add(0, step);
            step = cameFrom.get(step);
        }

        for (Cell c : finalPath) {
            if (c != maze.getStart() && c != maze.getEnd()) {
                c.isInFinalPath = true;
            }
        }
    }

    /** Returns a list of accesible and unvisted adjacents cells 
     *  @param cell the current cell
     *  @return List<Cell> of valid adjacents cells 
     */

    private List<Cell> getAccessibleNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int[] dx = {0, 1, 0, -1}; 
        // Direction vectors for x 
        int[] dy = {-1, 0, 1, 0};
        // Direction vectors for y 

        for (int d = 0; d < 4; d++) {
            // No wall in this direction 
            if (!cell.walls[d]) { 
                int ni = cell.i + dx[d];
                int nj = cell.j + dy[d];
                int index = maze.index(ni, nj);
                // Checks if the cell exist 
                if (index != -1) {
                    Cell neighbor = grid.get(index);
                    if (!visited.contains(neighbor)) {
                        neighbors.add(neighbor);
                    }
                }
            }
        }

        return neighbors;
    }

    /** Returns the number of cells visited so far 
     * @return number of visited cells
     */

    @Override
    public int getVisitedCount() {
        return visitedCount;
    }

    /** 
     * Class to store a cell and its associated distance
     * Used for ordering in the priority queue (Comparator)
     */
    private static class CellDistance {
        Cell cell;
        int distance;

        CellDistance(Cell cell, int distance) {
            this.cell = cell;
            this.distance = distance;
        }
    }
}
