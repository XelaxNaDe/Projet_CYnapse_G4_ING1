package projatlab.algorithms.generators;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import projatlab.model.Cell;
import projatlab.model.Maze;

public class MazeGeneratorDFS extends MazeGenerator {

    private Maze maze;

    public Cell current;
    private final Stack<Cell> stack = new Stack<>();
    private final Random rand;

    public MazeGeneratorDFS(Maze maze, long seed) {

        this.maze = maze;
        this.rand = new Random(seed);

        if (!maze.getGrid().isEmpty()) {
            this.current = maze.getGrid().get(0);
        }
    }

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
            current = stack.pop(); 
        } else {
            finished = true;
        }
    }

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
            return neighbors.get(r);
        }
        return null;
    }
}
