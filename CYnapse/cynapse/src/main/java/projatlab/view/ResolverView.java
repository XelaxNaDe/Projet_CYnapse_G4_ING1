package projatlab.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projatlab.controller.MazeController;
import projatlab.controller.ResolverController;
import projatlab.model.Cell;
import projatlab.model.Maze;

public class ResolverView {

    private final Maze maze;
    private final MazeView mazeView;
    public final MazeController mazeController;
    public final ResolverController controller;

    private Label lVisited;
    private Label lTimeGen;
    private Label lTimeRes;

    // Références aux contrôles pour pouvoir les désactiver
    private Button bSave;
    private Button bSolve;
    private Button bModify;
    private ComboBox<String> cBAlgo;
    private RadioButton rbComplet;
    private RadioButton rbStep;
    private Slider sSpeed;
    private Button bToggleVisited;
    private boolean showVisited = true;


    private int cellSize = Cell.cellSize;

    public ResolverView(Maze maze, MazeController mazeController, MazeView mazeView) {
        this.maze = maze;
        this.mazeView = mazeView;
        this.mazeController = mazeController;
        this.controller = new ResolverController(maze, mazeController);
    }

    public void show() {
        Stage resStage = new Stage();
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        

        ScrollPane scrollPane = new ScrollPane(mazeView);
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setPrefViewportWidth(1000); // Largeur max visible sans scroll horizontal
        scrollPane.setPrefViewportHeight(500); // Hauteur max visible sans scroll vertical

        root.setCenter(scrollPane);

        // Right Panel - Algorithm + Mode
        VBox vbAlgoMode = new VBox();
        vbAlgoMode.setAlignment(Pos.TOP_LEFT);

        Label lAlgo = new Label("Choix de l'algorithme :");
        cBAlgo = new ComboBox<>();
        cBAlgo.getItems().addAll("DFS", "A*","Dijkstra");
        cBAlgo.setValue("DFS");

        Label lModeS = new Label("Mode :");

        ToggleGroup solverModeGroup = new ToggleGroup();

        rbComplet = new RadioButton("Complet");
        rbComplet.setToggleGroup(solverModeGroup);
        rbComplet.setSelected(true);

        rbStep = new RadioButton("Pas à pas");
        rbStep.setToggleGroup(solverModeGroup);

        sSpeed = new Slider(1, 100, 10); 
        sSpeed.setPrefWidth(100); 
        sSpeed.setShowTickMarks(true);
        sSpeed.setMajorTickUnit(25);
        sSpeed.setBlockIncrement(5);

        Label lSpeed = new Label("10 ms");
        sSpeed.valueProperty().addListener((obs, oldVal, newVal) -> {
            lSpeed.setText(newVal.intValue() + " ms");
        });

        //Legend
        Label lLegend = new Label("Légende : ");

        Label bGreen = new Label("");
        bGreen.setStyle("-fx-background-color: green; -fx-min-width: 20; -fx-min-height: 20; -fx-border-color: black;");

        Label bRed = new Label("");
        bRed.setStyle("-fx-background-color: red; -fx-min-width: 20; -fx-min-height: 20; -fx-border-color: black;");

        Label bPink = new Label("   ");
        bPink.setStyle("-fx-background-color: #FF77FF; -fx-min-width: 20; -fx-min-height: 20; -fx-border-color: black;");

        Label bBlue = new Label("   ");
        bBlue.setStyle("-fx-background-color: #0099FF; -fx-min-width: 20; -fx-min-height: 20; -fx-border-color: black;");

        Label lGreen = new Label("Entrée");
        Label lRed = new Label("Sortie");
        Label lPink = new Label("Cases visitées");
        Label lBlue = new Label("Chemin trouvé");

        HBox hbGreen = new HBox (bGreen, lGreen);
        HBox hbRed = new HBox (bRed, lRed);
        HBox hbPink = new HBox (bPink, lPink);
        HBox hbBlue = new HBox (bBlue, lBlue);

        VBox vbLegend = new VBox(10, hbGreen, hbRed, hbPink, hbBlue);
        vbLegend.setAlignment(Pos.CENTER_LEFT);
        vbLegend.setPadding(new Insets(10, 0, 0, 0));

        //Show/Hide visited
        bToggleVisited = new Button("Masquer les cases visitées");
        bToggleVisited.setPrefWidth(200);
        bToggleVisited.setPrefHeight(30);
        bToggleVisited.setOnAction(e -> {
            showVisited = !showVisited;
            mazeController.handleToggleVisited();
            bToggleVisited.setText(showVisited ? "Masquer les cases visitées" : "Montrer les cases visitées");
        });

        vbAlgoMode.getChildren().addAll(lAlgo, cBAlgo, 
                                        new Separator(),
                                        lModeS, rbComplet, rbStep, sSpeed, lSpeed, 
                                        new Separator(),
                                        lLegend, vbLegend, 
                                        new Separator(), 
                                        bToggleVisited
                                        );
        vbAlgoMode.setSpacing(5);
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

        bSave = new Button("Sauvegarder");
        bSolve = new Button("Résoudre");
        bModify = new Button("Modifier");

        bSave.setOnAction(e -> controller.handleSave(resStage));

        bModify.setOnAction(e -> controller.handleModify());

        bSolve.setOnAction(e -> {
            String mode = rbComplet.isSelected() ? "complet" : "step";
            controller.handleSolveMaze(maze, cBAlgo.getValue(), mode, sSpeed.getValue(), resStage);
        });

        HBox hbSaveModSolve = new HBox(10, bSave, bModify, bSolve);
        hbSaveModSolve.setAlignment(Pos.BOTTOM_RIGHT);

        vbStatsSave.getChildren().addAll(vbStats, hbSaveModSolve);
        root.setBottom(vbStatsSave);

        // Scene
        Scene scene = new Scene(root);
        resStage.setScene(scene);

        int mazeCols = maze.getCols();
        int mazeRows = maze.getRows();

        if (mazeCols <= 100 && mazeRows <= 50) {
            // Adapter la fenêtre à la taille réelle du labyrinthe
            resStage.setWidth(mazeCols * Cell.cellSize + 300);
            resStage.setHeight(mazeRows * Cell.cellSize + 140);
            resStage.setResizable(false);
        } else {
            // Fenêtre fixe (scrollable)
            resStage.setWidth(1300);  // 1000 (mazeView) + 300 (panel)
            resStage.setHeight(640);  // 500 (mazeView) + 140 (panel)
            resStage.setResizable(true); // pour l'utilisateur, optionnel
        }

        resStage.setTitle("Résolution du labyrinthe");
        resStage.show();
    }

    public void setGenerationTime(long timeGenMs) {
        lTimeGen.setText("Temps de génération : " + timeGenMs + "ms");
    }

    public void setSolvingTime (long timeResMs) {
        if (timeResMs == 0) {
            lTimeRes.setText("Temps de résolution : Pas résolu");
        } else {
            lTimeRes.setText("Temps de résolution : " + timeResMs + "ms");
        }
    }

    public void setCellsVisited (long visitedcellsNB) {
        if (visitedcellsNB == 0) {
            lVisited.setText("Cases visitées : Pas résolu");
        } else {
            lVisited.setText("Cases visitées : " + visitedcellsNB);
        }
    }

    // Méthodes pour contrôler l'état des boutons et contrôles
    public void setControlsEnabled(boolean enabled) {
        if (bSave != null) bSave.setDisable(!enabled);
        if (bSolve != null) bSolve.setDisable(!enabled);
        if (bModify != null) bModify.setDisable(!enabled);
        if (cBAlgo != null) cBAlgo.setDisable(!enabled);
        if (rbComplet != null) rbComplet.setDisable(!enabled);
        if (rbStep != null) rbStep.setDisable(!enabled);
        if (sSpeed != null) sSpeed.setDisable(!enabled);
        if (bToggleVisited !=null) bToggleVisited.setDisable(!enabled);
    }



    public void setSolvingInProgress(boolean inProgress) {
        if (inProgress) {
            lTimeRes.setText("Temps de résolution : En cours...");
        }
    }

    public void setGenerationInProgress(boolean inProgress) {
        if (inProgress) {
            lTimeGen.setText("Temps de génération : En cours...");
        }
    }

    // Nouvelle méthode pour gérer l'état de modification
    public void setModificationInProgress(boolean inProgress) {
        // On peut ajouter un message spécifique ou simplement désactiver les contrôles
        // Les contrôles sont déjà gérés par setControlsEnabled dans updateControlsState
        if (inProgress) {
            // Optionnel : ajouter un message spécifique pour la modification
            System.out.println("Modification en cours - contrôles désactivés");
        }
    }
}