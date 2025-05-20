package projatlab.algorithms.solvers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import projatlab.model.Cell;
import projatlab.model.Maze;

public class MazeSolverDFS extends MazeSolver{

    private Maze maze;
    private ArrayList<Cell> grid;
    private int rows;
    private int cols;

    private Random rand = new Random(System.currentTimeMillis());
    private Stack<Cell> stack = new Stack<>();
    private ArrayList<Cell> path = new ArrayList<>();

    public Cell current;


    public MazeSolverDFS(Maze maze) {

        this.maze = maze;
        this.cols = maze.getCols();
        this.rows = maze.getRows();
        this.grid = maze.getGrid();

        if (!grid.isEmpty()) {
            this.current = maze.getStart();
        }
    }

    private int visitedCount =0;

    public void step() {
        if (current == null) return;

        if (!current.visited) {
            current.visited = true;
            visitedCount++;
        }

        // ici on check si on est à l'arrive
        if (current == maze.getEnd()) {
            finished = true;
            finalPath.clear();
            finalPath.addAll(stack); // stack contient le chemin actuel
            finalPath.add(current);

            for (Cell c : finalPath) {
                if (c != maze.getStart() && c != maze.getEnd()) {
                    c.isInFinalPath = true;
                }
            }

            return;
        }


        Cell next = getAccessibleNeighbor(current, cols, rows, rand);
        if (next != null) {
            stack.push(current);
            path.add(current);
            current = next;
        } else if (!stack.isEmpty()) {
            current = stack.pop();
        }
    }


    private Cell getAccessibleNeighbor(Cell cell, int cols, int rows, Random rand) {
        int[] dx = {0, 1, 0, -1}; // direction horizontale (gauche/droite)
        int[] dy = {-1, 0, 1, 0}; // direction verticale (haut/bas)
        List<Cell> accessibleNeighbors = new ArrayList<>();

        for (int d = 0; d < 4; d++) {
            if (!cell.walls[d]) { // Pas de mur dans cette direction
                int ni = cell.i + dx[d];
                int nj = cell.j + dy[d];
                int index = maze.index(ni, nj); // ou utilise ta propre méthode index(i, j)
                if (index != -1) {
                    Cell neighbor = grid.get(index);
                    if (!neighbor.visited) {
                        accessibleNeighbors.add(neighbor);
                    }
                }
            }
        }

        if (!accessibleNeighbors.isEmpty()) {
            return accessibleNeighbors.get(rand.nextInt(accessibleNeighbors.size()));
        }

        return null;
    } 

    @Override
    public int getVisitedCount() {
        return visitedCount;
    }
}
