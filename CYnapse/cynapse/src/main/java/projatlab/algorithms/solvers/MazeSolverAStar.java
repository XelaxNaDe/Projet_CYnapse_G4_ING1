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

public class MazeSolverAStar extends MazeSolver {

    private final Maze maze;
    private final Cell start;
    private final Cell end;
    private int visitedCount = 0;

    private final PriorityQueue<Cell> openSet;
    private final Set<Cell> openSetHash;
    private final Map<Cell, Cell> cameFrom = new HashMap<>();
    private final Map<Cell, Double> gScore = new HashMap<>(); //cost of start->cell
    private final Map<Cell, Double> fScore = new HashMap<>(); //cost of gscore + estimation of cell->end

    public MazeSolverAStar(Maze maze) {
        
        this.maze = maze;
        this.start = maze.getStart();
        this.end = maze.getEnd();

        Comparator<Cell> comparator = Comparator.comparingDouble(fScore::get);
        openSet = new PriorityQueue<>(comparator);
        openSetHash = new HashSet<>();

        for (Cell c : maze.getGrid()) {
            gScore.put(c, Double.POSITIVE_INFINITY);
            fScore.put(c, Double.POSITIVE_INFINITY);
        }

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, end));
        openSet.add(start);
        openSetHash.add(start);
    }

    @Override
    public void step() {
        if (openSet.isEmpty()) {
            finished = true;
            return;
        }

        Cell current = openSet.poll();
        openSetHash.remove(current);

        if (!current.visited) {   
            if (current != start && current != end) {
                current.visited = true;
                visitedCount++;
        }
        }

        if (current == end) {
            reconstructPath(cameFrom, current);
            finished = true;
            return;
        }

        for (Cell neighbor : getAccessibleNeighbors(current)) {
            double tentativeG = gScore.get(current) + 1;

            if (tentativeG < gScore.get(neighbor)) {
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeG);
                fScore.put(neighbor, tentativeG + heuristic(neighbor, end));

                if (!openSetHash.contains(neighbor)) {
                    openSet.add(neighbor);
                    openSetHash.add(neighbor);
                }
            }
        }
    }

    private void reconstructPath(Map<Cell, Cell> cameFrom, Cell current) {
        finalPath.clear();
        while (cameFrom.containsKey(current)) {
            finalPath.add(0, current);
            current.isInFinalPath = true;
            current = cameFrom.get(current);
        }
        finalPath.add(0, start);
        start.isInFinalPath = false;
        end.isInFinalPath = false;
    }

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

    private double heuristic(Cell a, Cell b) { //Manhattan distance
        return Math.abs(a.i - b.i) + Math.abs(a.j - b.j);
    }

    @Override
    public int getVisitedCount() {
        return visitedCount;
    }
}
