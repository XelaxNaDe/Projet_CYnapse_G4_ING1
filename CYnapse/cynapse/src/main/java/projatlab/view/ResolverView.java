package projatlab.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projatlab.model.Maze;

public class ResolverView {

    private Maze maze;

    public ResolverView(Maze maze) {
        this.maze = maze;
    }

    public void show() {
        Stage resStage = new Stage();
        BorderPane root = new BorderPane();

        MazeView mazeView = new MazeView(maze, 25); // cellSize arbitraire
        root.setCenter(mazeView);        

        //Right
        VBox vbAlgoMode = new VBox();
        vbAlgoMode.setAlignment(Pos.TOP_LEFT);

        Label lAlgo = new Label("Choix de l'algorithme :");
        CheckBox cb1 = new CheckBox();
        CheckBox cb2 = new CheckBox();
        CheckBox cb3 = new CheckBox();

        CheckBox[] algoBoxes = {cb1, cb2, cb3};

        for (CheckBox cb : algoBoxes) {
            cb.setOnAction(e -> {
                if (cb.isSelected()) {
                    for (CheckBox other : algoBoxes) {
                        if (other != cb) {
                            other.setSelected(false);
                        }
                    }
                }
            });
        }


        Label lMode = new Label("Mode :");
        CheckBox cbComplet = new CheckBox("Complet");
        CheckBox cbPasComplet = new CheckBox("Pas Complet");

        cbComplet.setOnAction(e -> {
            if (cbComplet.isSelected()) {
                cbPasComplet.setSelected(false);
            }
        });

        cbPasComplet.setOnAction(e -> {
            if (cbPasComplet.isSelected()) {
                cbComplet.setSelected(false);
            }
        });

        vbAlgoMode.getChildren().addAll(lAlgo, cb1, cb2, cb3, new Separator(), lMode, cbComplet, cbPasComplet);
        VBox.setVgrow(vbAlgoMode, Priority.ALWAYS);
        root.setRight(vbAlgoMode);

        //Bottom

        VBox vbStatsSave = new VBox();

        VBox vbStats = new VBox();
        vbStats.setAlignment(Pos.CENTER_LEFT);
        Label lVisited = new Label("Cases visitées :");
        Label lTime = new Label("Temps de résolution :");

        vbStats.getChildren().addAll(lVisited, lTime);

        Button bSave = new Button("Sauvegarder");
        Button bSolve = new Button("Résoudre");
        Button bModify = new Button("Modifier");
        
        bModify.setOnAction(e -> {
            ModificationView modWindow = new ModificationView(maze);
            modWindow.show();
        });

        HBox hbSaveModSolve = new HBox(bSave,bModify,bSolve);

        hbSaveModSolve.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(hbSaveModSolve, Priority.NEVER);
        hbSaveModSolve.setAlignment(Pos.BOTTOM_RIGHT);

        
        vbStatsSave.getChildren().addAll(vbStats, hbSaveModSolve);
        root.setBottom(vbStatsSave);

        Scene scene = new Scene(root);


        resStage.setScene(scene);
        resStage.setResizable(false);
        resStage.setTitle("Résolution du labyrinthe");
        resStage.show();
    }
}
