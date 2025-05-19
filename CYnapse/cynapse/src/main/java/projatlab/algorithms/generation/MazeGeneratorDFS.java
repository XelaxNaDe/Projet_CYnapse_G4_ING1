package projatlab.algorithms.generation;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import projatlab.model.Cell;

public class MazeGeneratorDFS extends MazeGenerator {

    private final int cols;
    private final int rows;
    private final ArrayList<Cell> grid;

    public Cell current;
    private final Stack<Cell> stack = new Stack<>();
    private final Random rand;

    public MazeGeneratorDFS(ArrayList<Cell> grid, int cols, int rows, long seed) {
        this.cols = cols;
        this.rows = rows;
        this.grid = grid;
        this.rand = new Random(seed);

        if (!grid.isEmpty()) {
            this.current = grid.get(0);
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

        Cell top = getCell(i, j - 1);
        Cell right = getCell(i + 1, j);
        Cell bottom = getCell(i, j + 1);
        Cell left = getCell(i - 1, j);

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

    private Cell getCell(int i, int j) {
        if (i < 0 || j < 0 || i >= cols || j >= rows) return null;
        return grid.get(i + j * cols);
    }
}
