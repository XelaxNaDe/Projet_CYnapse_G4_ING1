package projatlab.controller;

import javafx.stage.Stage;
import projatlab.model.Cell;
import projatlab.model.Maze;
import projatlab.view.MazeView;

/**
 * ModificationController handles user interactions for modifying the maze
 * like changing walls, the start and the end
 * It manages a copy of the original maze, applies changes via user clicks,
 * and saves the modifications back into the original maze 
 */
public class ModificationController {

    /** Represents the modication modes
     * MUR : for walls
     * ENTREE : for the start
     * SORTIE : for the end
     */
    public enum Mode { MUR, ENTREE, SORTIE }

    /** The currently selected modification mode */
    private Mode currentMode = Mode.MUR;

    /** The temporary maze where modifications are applied */ 
    private final Maze maze; 

    /** The visual representation of the maze to reflect changes */
    private final MazeView view;

    /** The original maze where changes will be saved */
    private final Maze originalMaze; 

    /** Window to make modifications closed upon save */
    private final Stage modStage; 

     /**
     * Constructor for ModificationController
     *
     * @param maze         The temporary maze where modifications are applied
     * @param view         The visual representation of the maze to reflect changes
     * @param originalMaze The original maze where changes will be saved
     * @param modStage     Window to make modifications closed upon save
     */
    public ModificationController(Maze maze, MazeView view, Maze originalMaze, Stage modStage) {
        this.maze = maze;
        this.view = view;
        this.originalMaze = originalMaze;
        this.modStage = modStage;
    }

    /**
     * Sets the current modification mode
     *
     * @param mode the mode to be used 
     */
    public void setMode(Mode mode) {
        this.currentMode = mode;
    }

    /**
     * Handles a user click on the maze, determining the action based on
     * the current mode (modifying walls, start, or end).
     *
     * @param x the X-coordinate of the mouse click
     * @param y the Y-coordinate of the mouse click
     */
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
                    // top
                    toggleWall(cell, 0);
                    Cell top = maze.getCell(maze.index(colClicked, rowClicked - 1));
                    if (top != null) toggleWall(top, 2);
                    
                } else if (offsetX > Cell.cellSize - threshold && colClicked < maze.getCols() - 1) {
                    // right
                    toggleWall(cell, 1);
                    Cell right = maze.getCell(maze.index(colClicked + 1, rowClicked));
                    if (right != null) toggleWall(right, 3);

                } else if (offsetY > Cell.cellSize - threshold && rowClicked < maze.getRows() - 1) {
                    // bot
                    toggleWall(cell, 2);
                    Cell bottom = maze.getCell(maze.index(colClicked, rowClicked + 1));
                    if (bottom != null) toggleWall(bottom, 0);

                } else if (offsetX < threshold && colClicked > 0) {
                    // left
                    toggleWall(cell, 3);
                    Cell left = maze.getCell(maze.index(colClicked - 1, rowClicked));
                    if (left != null) toggleWall(left, 1);
                }
                break;

            case ENTREE:
                maze.getGrid().forEach(c -> c.setStart(false));

                // if the cell is already the end, we switch
                if (cell.isEnd) {
                    cell.setEnd(false);
                    cell.setStart(true);

                    // find another cell to put the end
                    for (Cell other : maze.getGrid()) {
                        if (!other.isStart && !other.equals(cell)) {
                            other.setEnd(true);
                            break;
                        }
                    }
                } else {
                    cell.setStart(true);
                }
                break;

            case SORTIE:
                maze.getGrid().forEach(c -> c.setEnd(false));

                // if the cell is already the start, we switch
                if (cell.isStart) {
                    cell.setStart(false);
                    cell.setEnd(true);

                    // find another cell to put the start
                    for (Cell other : maze.getGrid()) {
                        if (!other.isEnd && !other.equals(cell)) {
                            other.setStart(true);
                            break;
                        }
                    }
                } else {
                    cell.setEnd(true);
                }
                break;
        }

        view.draw();
    }

     /**
     * Toggles the state of a wall for a given cell at the specified wall
     *
     * @param cell      The cell whose wall is to be toggled
     * @param wallIndex The index of the wall to toggle
     */
    private void toggleWall(Cell cell, int wallIndex) {
        cell.walls[wallIndex] = !cell.walls[wallIndex];
    }

    /**
     * Saves all modifications to the original maze
     * Copies wall, then the start and end, then the visual states (visited or not, is in final path..),
     * updates everything in the original maze and then closes
     */
    public void handleSave() {
        for (int j = 0; j < maze.getRows(); j++) {
            for (int i = 0; i < maze.getCols(); i++) {
                Cell modifiedCell = maze.getCell(maze.index(i, j));
                Cell originalCell = originalMaze.getCell(originalMaze.index(i, j));

                // copies walls
                System.arraycopy(modifiedCell.walls, 0, originalCell.walls, 0, modifiedCell.walls.length);

                // copies the start and end
                originalCell.setStart(modifiedCell.isStart);
                originalCell.setEnd(modifiedCell.isEnd);

                originalCell.visited = modifiedCell.visited;
                originalCell.isInFinalPath = modifiedCell.isInFinalPath;
            }
        }

        // change the original maze
        for (Cell c : originalMaze.getGrid()) {
            if (c.isStart) {
                originalMaze.setStart(c);
            }
            if (c.isEnd) {
                originalMaze.setEnd(c);
            }
        }

        // close
        modStage.close();
    }

}
