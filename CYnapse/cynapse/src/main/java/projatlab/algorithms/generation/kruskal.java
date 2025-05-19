package projatlab.algorithms.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import projatlab.algorithms.tools.Unionfind;
import projatlab.model.Cell;

public class kruskal extends MazeGenerator {

    private final List<int[]> edges = new ArrayList<>();
    private final Unionfind uf;
    private final int cols;
    private final int rows;
    private final ArrayList<Cell> grid;
    private int currentEdgesIndex = 0;

    public kruskal(ArrayList<Cell> grid, int cols, int rows, long seed) {
        this.grid = grid;
        this.rows = rows;
        this.cols = cols;
        this.uf = new Unionfind(rows * cols);

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                int currentIndex = index(i, j);

                // Bottom wall
                if (j < rows - 1) {
                    edges.add(new int[]{currentIndex, currentIndex + cols});
                }

                // Right wall
                if (i < cols - 1) {
                    edges.add(new int[]{currentIndex, currentIndex + 1});
                }
            }
        }

        Random rand = new Random(seed);
        Collections.shuffle(edges, rand);
    }

    @Override
    public void step() {
        if (currentEdgesIndex >= edges.size()) return;

        int[] edge = edges.get(currentEdgesIndex++);
        int index1 = edge[0];
        int index2 = edge[1];

        Cell cell1 = getCell(index1);
        Cell cell2 = getCell(index2);

        if (!uf.connected(index1, index2)) {
            uf.union(index1, index2);

            if (!cell1.visited) {
                cell1.setVisited(true);
            }
            if (!cell2.visited) {
                cell2.setVisited(true);
            }

            removeWalls(cell1, cell2);
        }
    }


    @Override
    public boolean isFinished() {
        return currentEdgesIndex >= edges.size();
    }

    private Cell getCell(int index) {
        if (index == -1) return null;
        return grid.get(index);
    }


    public int index(int i, int j) {
        if (i < 0 || j < 0 || i >= cols || j >= rows) {
            return -1;
        }
        return i + j * cols;
    }
   
}
