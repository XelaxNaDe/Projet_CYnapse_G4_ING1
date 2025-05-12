package projatlab.view;

import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import projatlab.model.Cell;
import projatlab.model.Maze;
import projatlab.model.Wall;

public class MazeView extends Pane {
    private final Maze maze;
    private final double cellSize;

    public MazeView(Maze maze, double cellSize) {
        this.maze = maze;
        this.cellSize = cellSize;
        drawMaze();
    }

    public void drawMaze() {
        getChildren().clear();

        // 1. Fond des cellules visités
        for (Cell[] row : maze.getGrid()) {
            for (Cell cell : row) {
                if (cell.visited) {
                    Rectangle background = new Rectangle(
                        cell.j * cellSize,
                        cell.i * cellSize,
                        cellSize,
                        cellSize
                    );
                    background.setFill(Color.LIGHTBLUE);
                    getChildren().add(background);
                }
                if (cell.isSolution) {
                    Rectangle background = new Rectangle(
                        cell.j * cellSize,
                        cell.i * cellSize,
                        cellSize,
                        cellSize
                    );
                    background.setFill(Color.GREEN);
                    getChildren().add(background);
                }
            }
        }

        // 2. Murs horizontaux
        List<List<Wall>> hWalls = maze.getHWalls();
        for (int i = 0; i < hWalls.size(); i++) {
            for (int j = 0; j < hWalls.get(i).size(); j++) {
                Wall wall = hWalls.get(i).get(j);
                if (wall.isActive()) {
                    Rectangle rect = new Rectangle(
                        j * cellSize,
                        (i + 1) * cellSize,
                        cellSize,
                        2
                    );
                    rect.setFill(Color.BLACK);
                    getChildren().add(rect);
                }
            }
        }

        // 3. Murs verticaux
        List<List<Wall>> vWalls = maze.getVWalls();
        for (int i = 0; i < vWalls.size(); i++) {
            for (int j = 0; j < vWalls.get(i).size(); j++) {
                Wall wall = vWalls.get(i).get(j);
                if (wall.isActive()) {
                    Rectangle rect = new Rectangle(
                        (j + 1) * cellSize,
                        i * cellSize,
                        2,
                        cellSize
                    );
                    rect.setFill(Color.BLACK);
                    getChildren().add(rect);
                }
            }
        }
    }

}