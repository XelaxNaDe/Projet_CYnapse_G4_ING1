package projatlab.algo.reso;

import projatlab.model.Cell;
import projatlab.view.MazeView;
import java.util.ArrayList;
import java.util.Stack;

public class dfsres {
    private final ArrayList<Cell> grid;
    private final Stack<Cell> stack = new Stack<>();
    private final ArrayList<Cell> path = new ArrayList<>();
    private final ArrayList<Cell> finalPath = new ArrayList<>();
    public Cell current;
    public boolean finished = false;

    public dfsres(ArrayList<Cell> grid) {
        this.grid = grid;
        if (!grid.isEmpty()) {
            this.current = grid.get(0);
        }
    }

    public void step(int cols, int rows) {
        if (current == null) return;

        current.visited = true;

        // ici on check si on est à l'arrive
        if (current.i == cols - 1 && current.j == rows - 1) {
            finished = true;
            finalPath.clear();
            finalPath.addAll(stack); // stack contient le chemin actuel
            finalPath.add(current);
            return;
        }


        Cell next = getAccessibleNeighbor(current, cols, rows);
        if (next != null) {
            stack.push(current);
            path.add(current);
            current = next;
        } else if (!stack.isEmpty()) {
            current = stack.pop();
        }
    }


    private Cell getAccessibleNeighbor(Cell cell, int cols, int rows) {
        int[] dx = {0, 1, 0, -1}; // direction horizontal
        int[] dy = {-1, 0, 1, 0}; // direction vertical

        for (int d = 0; d < 4; d++) { 
            if (!cell.walls[d]) {
                int ni = cell.i + dx[d]; // nv colonne
                int nj = cell.j + dy[d]; // et nv ligne
                int index = MazeView.index(ni, nj); 
                if (index != -1) {
                    Cell neighbor = grid.get(index);
                    if (!neighbor.visited) {
                        return neighbor;
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<Cell> getPath() {
        return path;
    }

    public ArrayList<Cell> getFinalPath() {
        return finalPath;
    }
    
}
// Dans mon MazeView j'ai mis ca dans draw apres la boucle show  
/* if (solving && solver != null && !solver.finished) {
    solver.step(cols, rows); // << appel à chaque frame
}
if (solver != null) {
    for (Cell c : solver.getPath()) {
        gc.setFill(Color.rgb(150, 5, 100));
        gc.fillRect(c.i * Cell.w + 4, c.j * Cell.w + 4, Cell.w - 6, Cell.w - 6);
    }
    for (Cell c : solver.getFinalPath()) {
        gc.setFill(Color.rgb(0, 200, 0));
        gc.fillRect(c.i * Cell.w + 3, c.j * Cell.w + 3, Cell.w - 6, Cell.w - 6);
    }
}*/
