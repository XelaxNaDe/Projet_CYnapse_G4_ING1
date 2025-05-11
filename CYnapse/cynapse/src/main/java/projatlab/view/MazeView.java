package projatlab.view;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MazeView {

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Labyrinthe généré");
        stage.setResizable(false);

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

        int totalWidth = cols * (cellSize + 1) ;
        int totalHeight = rows * (cellSize + 1);

        Scene scene = new Scene(grid, totalWidth, totalHeight);
        stage.setScene(scene);
        stage.show();   
    }
}