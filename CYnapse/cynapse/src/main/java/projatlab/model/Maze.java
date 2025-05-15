package projatlab.model;

import java.util.ArrayList;

public class Maze {
    private final int rows;
    private final int cols;
    private final ArrayList<Cell> grid;

    public Maze(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.grid = new ArrayList<>();

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
        int idx = index(i, j);
        if (idx == -1) return null;
        return grid.get(idx);
    }

    public Cell getCell(int index){
        if (index == -1) return null;
        return grid.get(index);
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public ArrayList<Cell> getGrid() {
        return grid;
    }

    public Maze copy() {
        Maze copyMaze = new Maze(cols, rows);
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                Cell original = getCell(i, j);
                Cell copyCell = original.copy();
                copyMaze.getGrid().set(copyMaze.index(i, j), copyCell);
            }
        }
        return copyMaze;
    }
}
