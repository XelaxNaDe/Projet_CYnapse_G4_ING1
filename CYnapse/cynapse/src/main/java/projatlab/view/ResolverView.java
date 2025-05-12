package projatlab.view;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResolverView {
    public void show() {
        Stage resStage = new Stage();
        VBox root = new VBox();

        // Ajout du labyrinthe
        MazeView mazeView = new MazeView();
        root.getChildren().add(mazeView.getMazeNode());

        Scene scene = new Scene(root);
        resStage.setScene(scene);
        resStage.setTitle("Résolution du labyrinthe");
        resStage.show();
    }
}
