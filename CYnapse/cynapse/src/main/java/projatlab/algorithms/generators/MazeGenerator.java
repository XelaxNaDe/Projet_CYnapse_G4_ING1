package projatlab.algorithms.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import projatlab.model.Cell;
import projatlab.model.Maze;

/** Abstract base class for maze generators */

public class MazeGenerator {

        /** Reference to the maze being generated. */
    public Maze maze;

    
    /** Random number generator*/
    public Random rand;

    /** Indicates true if the maze generation is complete */
    public boolean finished = false;

    public Boolean isPerfect;

    public boolean imperfectionsIntroduced = false; // Pour éviter de refaire plusieurs fois

    /** Removes the wall between two adjacent cells a and b
     * @param a the first cell
     * @param b the second cell 
     */
    public void removeWalls(Cell a, Cell b) {
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

    /** Performs one stop of the maze generation algorithm */
    public void step(){

    }

        /** Return a list of all unvisited neighboring cells of a given cell
     * @param Cell : a cell
     * @return List<Cell> : unvisited neighbors of the Cell cell 
     */
    public List<Cell> getUnvisitedNeighbors(Cell cell) {
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

    /** Return a list of all visited neighboring cells of a given cell
     * @param Cell : a cell
     * @return List<Cell> : visited neighbors of the Cell cell 
     */
    public List<Cell> getVisitedNeighbors(Cell cell) {
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

    /** Return true if the maze generation is finished
     * @return true if generation is complete, false otherwise
     */
    public boolean isFinished() {
        return finished;
    }

    public void introduceImperfections(int extraWallsToRemove, int extraWallsToAdd) {
        // Ajouter des cycles : retirer des murs entre des cellules déjà connectées
        for (int k = 0; k < extraWallsToRemove; k++) {
            Cell cell = maze.getGrid().get(rand.nextInt(maze.getGrid().size()));
            List<Cell> visitedNeighbors = getVisitedNeighbors(cell);
            if (!visitedNeighbors.isEmpty()) {
                Cell neighbor = visitedNeighbors.get(rand.nextInt(visitedNeighbors.size()));
                // Vérifie que le mur existe encore
                if (hasWallBetween(cell, neighbor)) {

                    removeWalls(cell, neighbor);
                }
            }
        }

        // Ajouter des impasses : remettre un mur entre deux cellules connectées
        for (int k = 0; k < extraWallsToAdd; k++) {
            Cell cell = maze.getGrid().get(rand.nextInt(maze.getGrid().size()));
            List<Cell> neighbors = getVisitedNeighbors(cell);
            if (!neighbors.isEmpty()) {
                Cell neighbor = neighbors.get(rand.nextInt(neighbors.size()));
                if (!hasWallBetween(cell, neighbor)) {
                    addWallBetween(cell, neighbor);
                }
            }
        }
    }

    // Méthode utilitaire pour vérifier s’il y a un mur entre deux cellules
    private boolean hasWallBetween(Cell a, Cell b) {
        int x = a.i - b.i;
        int y = a.j - b.j;

        if (x == 1) return a.walls[3] && b.walls[1];
        if (x == -1) return a.walls[1] && b.walls[3];
        if (y == 1) return a.walls[0] && b.walls[2];
        if (y == -1) return a.walls[2] && b.walls[0];
        return true; // Not adjacent
    }

    private void addWallBetween(Cell a, Cell b) {
        int x = a.i - b.i;
        int y = a.j - b.j;

        if (x == 1) {
            a.walls[3] = true;
            b.walls[1] = true;
        } else if (x == -1) {
            a.walls[1] = true;
            b.walls[3] = true;
        }

        if (y == 1) {
            a.walls[0] = true;
            b.walls[2] = true;
        } else if (y == -1) {
            a.walls[2] = true;
            b.walls[0] = true;
        }
    }

}
