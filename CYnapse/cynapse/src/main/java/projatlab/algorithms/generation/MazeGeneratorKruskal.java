package projatlab.algorithms.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import projatlab.algorithms.tools.Unionfind;
import projatlab.model.Cell;
import projatlab.model.Maze;

/** Maze generator using Kruskal's algorithm using Disjoint-set data structure
*/
public class MazeGeneratorKruskal extends MazeGenerator {

    private final List<int[]> edges = new ArrayList<>();
    private final Unionfind uf;
    private int currentEdgesIndex = 0;

    private final Maze maze;
    private final ArrayList<Cell> grid;
    private final int cols;
    private final int rows;

    /**  Constructs a new maze with a seed 
     *   @param maze the maze to generate 
     *   @param seed the seed used for randomizing the edge list
     */
    public MazeGeneratorKruskal(Maze maze, long seed) {
        this.maze = maze;
        this.grid = maze.getGrid();
        this.cols = maze.getCols();
        this.rows = maze.getRows();
        this.uf = new Unionfind(rows * cols);

        /** Generate all possible edges between adjacent cells
         */ 
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

        /** Shuffle the edges using the seed
         */
        Random rand = new Random(seed);
        Collections.shuffle(edges, rand);
    }

    /**  Perform one step of Kruskal's algorithm
     * Pick the next edge and removes it if the cells are not connected
     */ 
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

    /** Checks if the maze generation is complete
     */
    @Override
    public boolean isFinished() {
        return currentEdgesIndex >= edges.size();
    }

    /**  Return the cell linked to its index 
    * @param index the index of the cell
    * @return Cell object, or null if index is -1 
    */
    private Cell getCell(int index) {
        if (index == -1) return null;
        return grid.get(index);
    }
}
