package projatlab.algorithms.generation;

import projatlab.model.Cell;
import java.util.ArrayList;
import java.util.Stack;

public class dfs {

    private Boolean finished = false;
    private int cols;
    private int rows;
    private final ArrayList<Cell> grid;
    public Cell current;
    private final Stack<Cell> stack = new Stack<>();

    public dfs(ArrayList<Cell> grid) {
        this.grid = grid;
        if (!grid.isEmpty()) {
            this.current = grid.get(0);
        }
    }

    public void step(int cols, int rows) {
        System.out.println("DFS step: current = " + current);

        this.cols = cols;
        this.rows = rows;

        if (current == null) return;

        current.visited = true;

        Cell next = getUnvisitedNeighbor(current, cols, rows);

        if (next != null) {
            next.visited = true;
            stack.push(current);
            removeWalls(current, next);
            current = next;
        } else if (!stack.isEmpty()) {
            current = stack.pop();
        } else finished = true;
    }

    private Cell getUnvisitedNeighbor(Cell cell, int cols, int rows) {
        ArrayList<Cell> neighbors = new ArrayList<>();

        int i = cell.i;
        int j = cell.j;

        Cell top = getCell(i, j - 1, cols, rows);
        Cell right = getCell(i + 1, j, cols, rows);
        Cell bottom = getCell(i, j + 1, cols, rows);
        Cell left = getCell(i - 1, j, cols, rows);

        if (top != null && !top.visited) neighbors.add(top);
        if (right != null && !right.visited) neighbors.add(right);
        if (bottom != null && !bottom.visited) neighbors.add(bottom);
        if (left != null && !left.visited) neighbors.add(left);

        if (!neighbors.isEmpty()) {
            int r = (int) (Math.random() * neighbors.size());
            return neighbors.get(r);
        }
        return null;
    }

    private Cell getCell(int i, int j, int cols, int rows) {
        if (i < 0 || j < 0 || i >= cols || j >= rows) return null;
        return grid.get(i + j * cols);
    }

    private void removeWalls(Cell a, Cell b) {
        int x = a.i - b.i;
        int y = a.j - b.j;

        if (x == 1) {
            a.walls[3] = false;
            b.walls[1] = false;
        } else if (x == -1) {
            a.walls[1] = false;
            b.walls[3] = false;
        }

        if (y == 1) {
            a.walls[0] = false;
            b.walls[2] = false;
        } else if (y == -1) {
            a.walls[2] = false;
            b.walls[0] = false;
        }
    }
    
    public boolean isFinished() {
        return finished;
    }
}
