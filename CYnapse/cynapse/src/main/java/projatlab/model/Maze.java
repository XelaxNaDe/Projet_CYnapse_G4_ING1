package projatlab.model;

import java.util.ArrayList;

public class Maze {
    private final int rows, cols;
    private final ArrayList<Cell> grid = new ArrayList<>();

    public Maze(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                grid.add(new Cell(i, j));
            }
        }
    }

    public int index(int i, int j) {
        if (i < 0 || j < 0 || i >= cols || j >= rows) {
            return -1;
        }
        return i + j * cols;
    }

    public Cell getCell(int i, int j) {
        int index = index(i, j);
        if (index == -1) return null;
        return grid.get(index);
    }

    public int getcols() {
        return cols;
    }

    public int getrows() {
        return rows;
    }

    public ArrayList<Cell> getGrid() {
        return grid;
    }
}
