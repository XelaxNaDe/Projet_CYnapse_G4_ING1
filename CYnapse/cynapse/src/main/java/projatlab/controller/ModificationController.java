package projatlab.controller;

import javafx.stage.Stage;
import projatlab.model.Cell;
import projatlab.model.Maze;
import projatlab.view.MazeView;

public class ModificationController {

    public enum Mode { MUR, ENTREE, SORTIE }

    private Mode currentMode = Mode.MUR;
    private final Maze maze; // la copie modifiée
    private final MazeView view;

    private final Maze originalMaze; // le labyrinthe original à modifier
    private final Stage modStage; // pour fermer la fenêtre

    public ModificationController(Maze maze, MazeView view, Maze originalMaze, Stage modStage) {
        this.maze = maze;
        this.view = view;
        this.originalMaze = originalMaze;
        this.modStage = modStage;
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
    }

    public void handleClick(double x, double y) {
        int col = (int) (x / Cell.cellSize);
        int row = (int) (y / Cell.cellSize);

        Cell cell = maze.getCell(maze.index(col, row));
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
                    Cell top = maze.getCell(maze.index(colClicked, rowClicked - 1));
                    if (top != null) toggleWall(top, 2);
                    
                } else if (offsetX > Cell.cellSize - threshold && colClicked < maze.getCols() - 1) {
                    // Droite
                    toggleWall(cell, 1);
                    Cell right = maze.getCell(maze.index(colClicked + 1, rowClicked));
                    if (right != null) toggleWall(right, 3);

                } else if (offsetY > Cell.cellSize - threshold && rowClicked < maze.getRows() - 1) {
                    // Bas
                    toggleWall(cell, 2);
                    Cell bottom = maze.getCell(maze.index(colClicked, rowClicked + 1));
                    if (bottom != null) toggleWall(bottom, 0);

                } else if (offsetX < threshold && colClicked > 0) {
                    // Gauche
                    toggleWall(cell, 3);
                    Cell left = maze.getCell(maze.index(colClicked - 1, rowClicked));
                    if (left != null) toggleWall(left, 1);
                }
                break;

            case ENTREE:
                maze.getGrid().forEach(c -> c.setStart(false));
                cell.setStart(true);
                break;
            case SORTIE:
                maze.getGrid().forEach(c -> c.setEnd(false));
                cell.setEnd(true);
                break;
        }

        view.draw();
    }

    private void toggleWall(Cell cell, int wallIndex) {
        cell.walls[wallIndex] = !cell.walls[wallIndex];
    }

    public void handleSave() {
        for (int j = 0; j < maze.getRows(); j++) {
            for (int i = 0; i < maze.getCols(); i++) {
                Cell modifiedCell = maze.getCell(maze.index(i, j));
                Cell originalCell = originalMaze.getCell(originalMaze.index(i, j));

                // Copier murs
                System.arraycopy(modifiedCell.walls, 0, originalCell.walls, 0, modifiedCell.walls.length);

                // Copier les flags start et end
                originalCell.setStart(modifiedCell.isStart);
                originalCell.setEnd(modifiedCell.isEnd);

                // Si tu veux, tu peux aussi copier visited et isInFinalPath, mais souvent pas nécessaire
                originalCell.visited = modifiedCell.visited;
                originalCell.isInFinalPath = modifiedCell.isInFinalPath;
            }
        }

        // Mettre à jour la cellule de départ et d'arrivée dans le labyrinthe original
        for (Cell c : originalMaze.getGrid()) {
            if (c.isStart) {
                originalMaze.setStart(c);
            }
            if (c.isEnd) {
                originalMaze.setEnd(c);
            }
        }

        // Fermer la fenêtre de modification
        modStage.close();
    }

}
