package projatlab.view;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class MazeView {

    public Pane getMazeNode() {
        int rows = 10;
        int cols = 10;
        int cellSize = 25;

        GridPane grid = new GridPane();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.BLACK); 
                rect.setStrokeWidth(1);

                grid.add(rect, x, y);
            }
        }

        return grid;
    }
}
