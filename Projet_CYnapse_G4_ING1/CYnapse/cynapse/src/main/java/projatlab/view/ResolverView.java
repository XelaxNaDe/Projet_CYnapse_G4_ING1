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
import projatlab.controller.MazeController;
import projatlab.controller.ResolverController;
import projatlab.model.Maze;
import projatlab.model.MazeGenerator;

public class ResolverView {

    private final Maze maze;
    private final MazeGenerator generator;
    private final ResolverController controller;

    private Label lVisited;
    private Label lTimeGen;

    public ResolverView(Maze maze, MazeGenerator generator) {
        this.maze = maze;
        this.generator = generator;
        this.controller = new ResolverController(maze);
    }

    public void show() {
        Stage resStage = new Stage();
        BorderPane root = new BorderPane();

        // MazeView and Controller
        MazeView mazeView = new MazeView(maze);
        MazeController mazeController = new MazeController(maze, mazeView, generator);
        root.setCenter(mazeView);

        // Right Panel - Algorithm + Mode
        VBox vbAlgoMode = new VBox();
        vbAlgoMode.setAlignment(Pos.TOP_LEFT);

        Label lAlgo = new Label("Choix de l'algorithme :");
        CheckBox cb1 = new CheckBox("A*");
        CheckBox cb2 = new CheckBox("BFS");
        CheckBox cb3 = new CheckBox("DFS");

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
        CheckBox cbPasComplet = new CheckBox("Pas à pas");

        cbComplet.setOnAction(e -> {
            if (cbComplet.isSelected()) cbPasComplet.setSelected(false);
        });

        cbPasComplet.setOnAction(e -> {
            if (cbPasComplet.isSelected()) cbComplet.setSelected(false);
        });

        vbAlgoMode.getChildren().addAll(lAlgo, cb1, cb2, cb3, new Separator(), lMode, cbComplet, cbPasComplet);
        VBox.setVgrow(vbAlgoMode, Priority.ALWAYS);
        root.setRight(vbAlgoMode);

        // Bottom Panel - Stats + Buttons
        VBox vbStatsSave = new VBox();

        VBox vbStats = new VBox();
        vbStats.setAlignment(Pos.CENTER_LEFT);
        lVisited = new Label("Cases visitées :");        
        lTimeGen = new Label("Temps de génération :");
        Label lTimeRes = new Label("Temps de résolution :");

        vbStats.getChildren().addAll(lVisited,lTimeGen, lTimeRes);

        Button bSave = new Button("Sauvegarder");
        Button bSolve = new Button("Résoudre");
        Button bModify = new Button("Modifier");

        bModify.setOnAction(e -> controller.handleModify());

        bSolve.setOnAction(e -> controller.handleSolve(cb1.isSelected(), cb2.isSelected(), cb3.isSelected(), cbComplet.isSelected()));

        HBox hbSaveModSolve = new HBox(10, bSave, bModify, bSolve);
        hbSaveModSolve.setAlignment(Pos.BOTTOM_RIGHT);

        vbStatsSave.getChildren().addAll(vbStats, hbSaveModSolve);
        root.setBottom(vbStatsSave);

        // Scene
        Scene scene = new Scene(root, maze.getCols() * 20 + 200, maze.getRows() * 20 + 100);
        resStage.setScene(scene);
        resStage.setResizable(false);
        resStage.setTitle("Résolution du labyrinthe");
        resStage.show();
    }

    public void setGenerationStats(int visited, long timeGenMs) {
        lVisited.setText("Cases visitées : " + visited);
        lTimeGen.setText("Temps de génération : " + timeGenMs + " ms");
    }
}
