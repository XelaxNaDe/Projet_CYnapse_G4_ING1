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

/** Implements maze solver using the A* (A-Star) pathfinding algorithm
 * Combines Dijkstra's algorithm with a heuristic to find the shortest path
 */
public class MazeSolverAStar extends MazeSolver {
    
    /** Priority queue sorted by fScore */
    private final PriorityQueue<Cell> openSet;

    /** Set to check if a cell is already in the queue  */
    private final Set<Cell> openSetHash;

    /**  Map used to reconstruct the shortest path*/
    private final Map<Cell, Cell> cameFrom = new HashMap<>();

    /** Actual cost from start to each cell */
    private final Map<Cell, Double> gScore = new HashMap<>(); 

    /** Estimated cost from start to goal (cost of gscore + estimation of cell->end using heuristic)*/
    private final Map<Cell, Double> fScore = new HashMap<>();

    /** Initializes the solver with the maze
     * Initializes the cost map and inserts the start cell into the priority queue
     * @param maze the maze to solve
     */
    public MazeSolverAStar(Maze maze) {
        
        this.maze = maze;
        this.visitedCount = 2;

        // Priority queue using the fScore for comparison
        Comparator<Cell> comparator = Comparator.comparingDouble(fScore::get);
        openSet = new PriorityQueue<>(comparator);
        openSetHash = new HashSet<>();

        // Initialize scores
        for (Cell c : maze.getGrid()) {
            gScore.put(c, Double.POSITIVE_INFINITY);
            fScore.put(c, Double.POSITIVE_INFINITY);
        }

        gScore.put(maze.getStart(), 0.0);
        fScore.put(maze.getStart(), heuristic(maze.getStart(), maze.getEnd()));
        openSet.add(maze.getStart());
        openSetHash.add(maze.getStart());
    }

    /** Performs one step of the A* algorithm */
    @Override
    public void step() {
        if (openSet.isEmpty()) {
            finished = true;
            pathFound = false;  // No path possible
            return;
        }

        Cell current = openSet.poll();
        openSetHash.remove(current);

        if (!current.visited) {
            if (current != maze.getStart() && current != maze.getEnd()) {
                current.visited = true;
                visitedCount++;
            }
        }

        if (current == maze.getEnd()) {
            reconstructPath(cameFrom, current);
            finished = true;
            pathFound = true;
            return;
        }

        // Explore neighbors
        for (Cell neighbor : getAccessibleNeighbors(current)) {
            double tentativeG = gScore.get(current) + 1;

            if (tentativeG < gScore.get(neighbor)) {
                // Better path found
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeG);
                fScore.put(neighbor, tentativeG + heuristic(neighbor, maze.getEnd()));

                if (!openSetHash.contains(neighbor)) {
                    openSet.add(neighbor);
                    openSetHash.add(neighbor);
                }
            }
        }
    }

    /** Reconstructs the final path from start to end using the cameFrom map
     * @param cameFrom the map tracking the previous cell for each visited cell
     * @param current the destination cell 
     */
    private void reconstructPath(Map<Cell, Cell> cameFrom, Cell current) {
        finalPath.clear();
        while (cameFrom.containsKey(current)) {
            finalPath.add(0, current);
            current.isInFinalPath = true;
            current = cameFrom.get(current);
        }
        finalPath.add(0, maze.getStart());
        maze.getStart().isInFinalPath = false;
        maze.getEnd().isInFinalPath = false;
    }

    /** Return accessible and unvisited neighbors of a cell 
     * @param Cell the current cell
     * @return list of accessible unvisited neighboring cells 
     */
    private List<Cell> getAccessibleNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        for (int d = 0; d < 4; d++) {
            if (!cell.walls[d]) {
                int ni = cell.i + dx[d];
                int nj = cell.j + dy[d];
                int index = maze.index(ni, nj);
                if (index != -1) {
                    Cell neighbor = maze.getGrid().get(index);
                    if (!neighbor.visited) {
                        neighbors.add(neighbor);
                    }
                }
            }
        }
        return neighbors;
    }

    /** Heuristic function : computes Manhattan distance between two cells 
     * @param a the first cell 
     * @param b the second cell
     * @return the Manhattan distance between a and b  
     */
    private double heuristic(Cell a, Cell b) { 
        return Math.abs(a.i - b.i) + Math.abs(a.j - b.j);
    }

    /** Returns the number of visited cells during the solving process 
     * @return number of visited cells 
     */
    @Override
    public int getVisitedCount() {
        return visitedCount;
    }
}