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

    private final List<int[]> edges = new ArrayList<>();
    private final Unionfind uf;
    private int currentEdgesIndex = 0;

    private Maze maze;

    public MazeGeneratorKruskal(Maze maze, long seed) {

        this.maze = maze;
        this.uf = new Unionfind(maze.getRows() * maze.getCols());

        // Génération des arêtes (edges)
        for (int j = 0; j < maze.getRows(); j++) {
            for (int i = 0; i < maze.getCols(); i++) {
                int currentIndex = maze.index(i, j);  // 👈 Utilisation centralisée

                if (j < maze.getRows() - 1) {
                    edges.add(new int[]{currentIndex, maze.index(i, j + 1)}); // bas
                }
                if (i < maze.getCols() - 1) {
                    edges.add(new int[]{currentIndex, maze.index(i + 1, j)}); // droite
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

        Cell cell1 = maze.getCell(index1);
        Cell cell2 = maze.getCell(index2);

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
}
