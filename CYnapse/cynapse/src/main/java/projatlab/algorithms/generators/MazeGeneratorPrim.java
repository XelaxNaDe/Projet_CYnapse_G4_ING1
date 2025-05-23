package projatlab.algorithms.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import projatlab.model.Cell;
import projatlab.model.Maze;

public class MazeGeneratorPrim extends MazeGenerator {

    private Maze maze;

    private final List<Cell> frontier = new ArrayList<>();
    private final Random rand;
    private int visitedCount = 0;

    public MazeGeneratorPrim(Maze maze, long seed) {

        this.maze = maze;
        this.rand = new Random(seed);

        // Start from a random cell
        int startIndex = rand.nextInt(maze.getGrid().size());
        Cell start = maze.getGrid().get(startIndex);
        start.visited = true;
        visitedCount++;

        frontier.addAll(getUnvisitedNeighbors(start));
    }

    @Override
    public void step() {
        if (visitedCount >= maze.getCols() * maze.getRows() || frontier.isEmpty()) {
            return;
        }

        int ind = rand.nextInt(frontier.size());
        Cell current = frontier.remove(ind);
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
    

    @Override
    public boolean isFinished() {
        return frontier.isEmpty();
    }

}
