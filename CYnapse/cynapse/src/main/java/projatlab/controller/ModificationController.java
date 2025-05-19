package projatlab.controller;

import projatlab.model.Cell;
import projatlab.model.Maze;
import projatlab.view.MazeView;

public class ModificationController {

    public enum Mode { MUR, ENTREE, SORTIE }

    private Mode currentMode = Mode.MUR;
    private final Maze maze;
    private final MazeView view;

    public ModificationController(Maze maze, MazeView view) {
        this.maze = maze;
        this.view = view;
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
    }

    public void handleClick(double x, double y) {
        int col = (int) (x / Cell.cellSize);
        int row = (int) (y / Cell.cellSize);

        Cell cell = maze.getCell(col, row);
        if (cell == null) return;

        switch (currentMode) {
            case MUR:
    int colClicked = cell.i;
    int rowClicked = cell.j;
    double cellX = colClicked * Cell.cellSize;
    double cellY = rowClicked * Cell.cellSize;

    double offsetX = x - cellX;
    double offsetY = y - cellY;
    double threshold = 5;

    if (offsetY < threshold && rowClicked > 0) {
        // Haut
        toggleWall(cell, 0);
        Cell top = maze.getCell(colClicked, rowClicked - 1);
        if (top != null) toggleWall(top, 2);
        
    } else if (offsetX > Cell.cellSize - threshold && colClicked < maze.getCols() - 1) {
        // Droite
        toggleWall(cell, 1);
        Cell right = maze.getCell(colClicked + 1, rowClicked);
        if (right != null) toggleWall(right, 3);

    } else if (offsetY > Cell.cellSize - threshold && rowClicked < maze.getRows() - 1) {
        // Bas
        toggleWall(cell, 2);
        Cell bottom = maze.getCell(colClicked, rowClicked + 1);
        if (bottom != null) toggleWall(bottom, 0);

    } else if (offsetX < threshold && colClicked > 0) {
        // Gauche
        toggleWall(cell, 3);
        Cell left = maze.getCell(colClicked - 1, rowClicked);
        if (left != null) toggleWall(left, 1);
    }
    break;

            case ENTREE:
                //maze.getGrid().forEach(c -> c.setStart(false));
                //cell.setStart(true);
                break;
            case SORTIE:
                //maze.getGrid().forEach(c -> c.setEnd(false));
                //cell.setEnd(true);
                break;
        }

        view.draw();
    }

    private void toggleWall(Cell cell, int wallIndex) {
        cell.walls[wallIndex] = !cell.walls[wallIndex];
    }
}
