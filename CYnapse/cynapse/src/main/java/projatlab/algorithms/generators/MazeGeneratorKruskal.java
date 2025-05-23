package projatlab.algorithms.generators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import projatlab.algorithms.tools.Unionfind;
import projatlab.model.Cell;
import projatlab.model.Maze;

/** Maze generator using Kruskal's algorithm using Union-Find data structure
 * It starts with each cell as an individual set and removes walls between randoms adjacent cells
 * that belong to different sets, forming a minimum spanning tree
*/
public class MazeGeneratorKruskal extends MazeGenerator {

    /** List of edges representing possible connections between adjacent cells */
    private final List<int[]> edges = new ArrayList<>();

    /** Union-Find (Disjoint Set) data structure  */
    private final Unionfind uf;

    /** Index to keep track of the current position in the shuffled list of edges */
    private int currentEdgesIndex = 0;
    
    /** Reference to the maze being generated. */
    private Maze maze;
    
    /** Number of columns in the maze. */
    private final int cols;
 
    /** Number of rows in the maze. */
    private final int rows;
 
    /** Grid representing a list of all the cells in the maze. */
    private final ArrayList<Cell> grid;

    public MazeGeneratorKruskal(Maze maze, long seed) {
        this.maze = maze;
        this.grid = maze.getGrid();
        this.cols = maze.getCols();
        this.rows = maze.getRows();
        this.uf = new Unionfind(rows * cols);

        // Generate all possible edges between adjacent cells
          
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                int currentIndex = maze.index(i, j);

                if (j < rows - 1) {
                    edges.add(new int[]{currentIndex, maze.index(i, j + 1)}); // bottom
                }
                if (i < cols - 1) {
                    edges.add(new int[]{currentIndex, maze.index(i + 1, j)}); // right
                }
            }
        }

        // Shuffle the edges using the seed
         
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
}
