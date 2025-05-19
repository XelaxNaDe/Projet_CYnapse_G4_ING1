package projatlab.algorithms.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import projatlab.model.Cell;

public class prim extends MazeGenerator {
    private final ArrayList<Cell> grid;
    private final List<Cell> frontier = new ArrayList<>();
    private final Random rand;
    private final int cols;
    private final int rows;
    private int visitedCount = 0;

    public prim(ArrayList<Cell> grid, int cols, int rows, long seed) {
        this.grid = grid;
        this.cols = cols;
        this.rows = rows;
        this.rand = new Random(seed);

        // Start from a random cell
        int startIndex = rand.nextInt(grid.size());
        Cell start = grid.get(startIndex);
        start.visited = true;
        visitedCount++;

        frontier.addAll(getUnvisitedNeighbors(start));
    }

    @Override
    public void step() {
        if (visitedCount >= cols * rows || frontier.isEmpty()) {
            return;
        }

        int index = rand.nextInt(frontier.size());
        Cell current = frontier.remove(index);
        current.visited = true;
        visitedCount++;

        List<Cell> visitedNeighbors = getVisitedNeighbors(current);
        if (!visitedNeighbors.isEmpty()) {
            Cell neighbor = visitedNeighbors.get(rand.nextInt(visitedNeighbors.size()));
            removeWalls(current, neighbor);
        }

        for (Cell n : getUnvisitedNeighbors(current)) {
            if (!frontier.contains(n)) {
                frontier.add(n);
            }
        }
    }

    private List<Cell> getUnvisitedNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int i = cell.i;
        int j = cell.j;

        Cell top = getCell(i, j - 1);
        Cell right = getCell(i + 1, j);
        Cell bottom = getCell(i, j + 1);
        Cell left = getCell(i - 1, j);

        if (top != null && !top.visited) neighbors.add(top);
        if (right != null && !right.visited) neighbors.add(right);
        if (bottom != null && !bottom.visited) neighbors.add(bottom);
        if (left != null && !left.visited) neighbors.add(left);

        return neighbors;
    }

    private List<Cell> getVisitedNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int i = cell.i;
        int j = cell.j;

        Cell top = getCell(i, j - 1);
        Cell right = getCell(i + 1, j);
        Cell bottom = getCell(i, j + 1);
        Cell left = getCell(i - 1, j);

        if (top != null && top.visited) neighbors.add(top);
        if (right != null && right.visited) neighbors.add(right);
        if (bottom != null && bottom.visited) neighbors.add(bottom);
        if (left != null && left.visited) neighbors.add(left);

        return neighbors;
    }

    private Cell getCell(int i, int j) {
        if (i < 0 || j < 0 || i >= cols || j >= rows) return null;
        return grid.get(i + j * cols);
    }

    

    @Override
    public boolean isFinished() {
        return frontier.isEmpty();
    }

}
