package projatlab.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projatlab.model.Maze;

public class ModificationView {
    public void show() {
        Stage modStage = new Stage();
        BorderPane root = new BorderPane();

        //Center
        Maze maze = new Maze(10, 10);
        MazeView mazeView = new MazeView(maze, 30.0);
        root.setCenter(mazeView);

        //Right Button
        VBox vbChange = new VBox(10);
        vbChange.setAlignment(Pos.CENTER_RIGHT);
        vbChange.setPadding(new Insets(20));

        Button btnMur = new Button("Mur");
        Button btnEntree = new Button("Entrée");
        Button btnSortie = new Button("Sortie");
        CheckBox cbNouveau = new CheckBox("Nouveau labyrinthe");
        Button btnAnnuler = new Button("Annuler");
        Button btnResoudre = new Button("Résoudre à nouveau");
        btnResoudre.setOnAction(e -> {
            ResolverView resWindow = new ResolverView(maze);
            resWindow.show();
        });

        btnMur.setMaxWidth(Double.MAX_VALUE);
        btnEntree.setMaxWidth(Double.MAX_VALUE);
        btnSortie.setMaxWidth(Double.MAX_VALUE);
        btnAnnuler.setMaxWidth(Double.MAX_VALUE);
        btnResoudre.setMaxWidth(Double.MAX_VALUE);

        vbChange.getChildren().addAll(
            btnMur,
            btnEntree,
            btnSortie,
            cbNouveau,
            btnAnnuler,
            btnResoudre
        );


        root.setRight(vbChange);
        
        Scene scene = new Scene(root, 500, 300); // largeur et hauteur

        modStage.setTitle("Modification du labyrinthe");
        modStage.setScene(scene);
        modStage.setResizable(false);
        modStage.show();
    }

    public void openResWindow() {
        ResolverView resWindow = new ResolverView(null);
        resWindow.show();
    }
}
