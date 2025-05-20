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

public class MazeSolverDijkstra extends MazeSolver {

    private Maze maze;
    private ArrayList<Cell> grid;
    private int rows;
    private int cols;

    private PriorityQueue<CellDistance> queue;
    private Map<Cell, Cell> cameFrom;
    private Map<Cell, Integer> distance;
    private Set<Cell> visited;

    private Cell current;

    private int visitedCount = 0;

    public MazeSolverDijkstra(Maze maze) {
        this.maze = maze;
        this.rows = maze.getRows();
        this.cols = maze.getCols();
        this.grid = maze.getGrid();

        this.queue = new PriorityQueue<>(Comparator.comparingInt(cd -> cd.distance));
        this.cameFrom = new HashMap<>();
        this.distance = new HashMap<>();
        this.visited = new HashSet<>();

        for (Cell c : grid) {
            distance.put(c, Integer.MAX_VALUE);
        }

        current = maze.getStart();
        distance.put(current, 0);
        queue.add(new CellDistance(current, 0));
    }

    @Override
    public void step() {
        if (finished || queue.isEmpty()) return;

        CellDistance cd = queue.poll();
        current = cd.cell;

        if (visited.contains(current)) return;
        visited.add(current);
        current.visited = true;
        visitedCount++;

        if (current == maze.getEnd()) {
            reconstructPath();
            finished = true;
            return;
        }

        for (Cell neighbor : getAccessibleNeighbors(current)) {
            int newDist = distance.get(current) + 1;
            if (newDist < distance.get(neighbor)) {
                distance.put(neighbor, newDist);
                cameFrom.put(neighbor, current);
                queue.add(new CellDistance(neighbor, newDist));
            }
        }
    }

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
                    Cell neighbor = grid.get(index);
                    if (!visited.contains(neighbor)) {
                        neighbors.add(neighbor);
                    }
                }
            }
        }

        return neighbors;
    }

    @Override
    public int getVisitedCount() {
        return visitedCount;
    }

    private static class CellDistance {
        Cell cell;
        int distance;

        CellDistance(Cell cell, int distance) {
            this.cell = cell;
            this.distance = distance;
        }
    }
}
