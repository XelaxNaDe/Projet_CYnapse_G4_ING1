package projatlab.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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

public class ResolverView {

    private final Maze maze;
    private final MazeView mazeView;
    public final ResolverController controller;
    private final MazeController mazeController;

    private Label lVisited;
    private Label lTimeGen;
    private Label lTimeRes;

    public ResolverView(Maze maze, MazeController mazeController, MazeView mazeView) {
        this.maze = maze;
        this.mazeView = mazeView;
        this.mazeController = mazeController;
        this.controller = new ResolverController(maze, mazeController);
    }

    public void show() {
        Stage resStage = new Stage();
        BorderPane root = new BorderPane();

        // MazeView and Controller
        root.setCenter(mazeView);

        // Right Panel - Algorithm + Mode
        VBox vbAlgoMode = new VBox();
        vbAlgoMode.setAlignment(Pos.TOP_LEFT);

        Label lAlgo = new Label("Choix de l'algorithme :");
        ComboBox<String> cBAlgo = new ComboBox<>();
        cBAlgo.getItems().addAll("DFS", "BFS", "A*","Dijkstra");
        cBAlgo.setValue("DFS");

        Label lMode = new Label("Mode :");
        CheckBox cbComplet = new CheckBox("Complet");
        CheckBox cbPasComplet = new CheckBox("Pas à pas");

        cbComplet.setOnAction(e -> {
            if (cbComplet.isSelected()) cbPasComplet.setSelected(false);
        });

        cbPasComplet.setOnAction(e -> {
            if (cbPasComplet.isSelected()) cbComplet.setSelected(false);
        });

        vbAlgoMode.getChildren().addAll(lAlgo, cBAlgo, new Separator(), lMode, cbComplet, cbPasComplet);
        VBox.setVgrow(vbAlgoMode, Priority.ALWAYS);
        root.setRight(vbAlgoMode);

        // Bottom Panel - Stats + Buttons
        VBox vbStatsSave = new VBox();

        VBox vbStats = new VBox();
        vbStats.setAlignment(Pos.CENTER_LEFT);

        lVisited = new Label("Cases visitées : Pas résolu");     
        lTimeGen = new Label("Temps de génération : En cours de génération");
        lTimeRes = new Label("Temps de résolution : Pas résolu");

        vbStats.getChildren().addAll(lVisited,lTimeGen,lTimeRes);

        Button bSave = new Button("Sauvegarder");
        Button bSolve = new Button("Résoudre");
        Button bModify = new Button("Modifier");

        bSave.setOnAction(e -> controller.handleSave(resStage));

        bModify.setOnAction(e -> controller.handleModify());

        bSolve.setOnAction(e -> controller.handleSolveMaze(maze, cBAlgo.getValue(), resStage));

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

    public void setGenerationTime(long timeGenMs) {
        lTimeGen.setText("Temps de génération : " + timeGenMs + "ms");
    }

    public void setSolvingTime (long timeResMs) {
        lTimeRes.setText("Temps de résolution : " + timeResMs + "ms");
    }

    public void setCellsVisited (long visitedcellsNB) {
        lVisited.setText("Cases visitées : " + visitedcellsNB);
    }
}
