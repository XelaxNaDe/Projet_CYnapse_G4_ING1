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

/**
 * ResolverView is the JavaFX interface for solving mazes.
 * It provides options for selecting solving algorithms and modes,
 * displays real-time statistics, and allows user interaction with the resolution process.
 */
public class ResolverView {

    /** The maze to be solved. */
    private final Maze maze;

    /** The MazeView used to visually display the maze. */
    private final MazeView mazeView;

    /** Controller responsible for managing overall maze state and interactions. */
    public final MazeController mazeController;

    /** Controller specifically for handling resolution logic and actions. */
    public final ResolverController controller;

    /** Label showing the number of visited cells. */
    private Label lVisited;

    /** Label showing the generation time. */
    private Label lTimeGen;

    /** Label showing the solving time. */
    private Label lTimeRes;

    /** Label showing the length of the final path. */
    private Label lPath;

    // Control references for toggling availability
    /** Button to save the current maze state. */
    private Button bSave;

    /** Button to launch the maze-solving algorithm. */
    private Button bSolve;

    /** Button to enter maze modification mode. */
    private Button bModify;

    /** Dropdown menu to select the solving algorithm (DFS, A*, Dijkstra). */
    private ComboBox<String> cBAlgo;

    /** Radio button to select "complet" (run full algorithm in one go) mode. */
    private RadioButton rbComplet;

    /** Radio button to select "step-by-step" mode. */
    private RadioButton rbStep;

    /** Slider to control the delay/speed of step-by-step solving. */
    private Slider sSpeed;

    /** Button to toggle visibility of visited cells on the maze. */
    private Button bToggleVisited;

    /** State flag indicating whether visited cells are currently shown. */
    private boolean showVisited = true;

    /**
     * Constructs the ResolverView.
     *
     * @param maze The maze to solve.
     * @param mazeController The controller managing general maze state.
     * @param mazeView The MazeView displaying the visual maze.
     */
    public ResolverView(Maze maze, MazeController mazeController, MazeView mazeView) {
        this.maze = maze;
        this.mazeView = mazeView;
        this.mazeController = mazeController;
        this.controller = new ResolverController(maze, mazeController);
    }
    
    /**
     * Displays the maze resolution interface with all necessary controls and panels.
     */
    public void show() {
        Stage resStage = new Stage();
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        
        // Center scrollable maze view
        ScrollPane scrollPane = new ScrollPane(mazeView);
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setPrefViewportWidth(1000); // Largeur max visible sans scroll horizontal
        scrollPane.setPrefViewportHeight(500); // Hauteur max visible sans scroll vertical

        root.setCenter(scrollPane);
        
        // Right Panel: Algorithm & Mode Selection
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

        // Legend
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

        // Toggle visited cells
        bToggleVisited = new Button("Masquer les cases visitées");
        bToggleVisited.setPrefWidth(200);
        bToggleVisited.setPrefHeight(30);
        bToggleVisited.setOnAction(e -> {
            showVisited = mazeController.handleToggleVisited(showVisited);
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

        // Bottom Panel: Statistics and buttons
        VBox vbStatsSave = new VBox();

        VBox vbStats = new VBox();
        vbStats.setAlignment(Pos.CENTER_LEFT);

        lVisited = new Label("Nombre de cases visitées par l'algorithme : Aucune (Non résolu)");     
        lTimeGen = new Label("Temps de génération : En cours de génération");
        lTimeRes = new Label("Temps de résolution : Non résolu");
        lPath = new Label("Taille du chemin final : Non résolu");

        vbStats.getChildren().addAll(lTimeGen, lTimeRes, lVisited, lPath);

        bSave = new Button("Sauvegarder");
        bSolve = new Button("Résoudre");
        bModify = new Button("Modifier");

        bSave.setOnAction(e -> controller.handleSave(resStage));
        bModify.setOnAction(e -> controller.handleModify());
        bSolve.setOnAction(e -> {
            bToggleVisited.setText("Masquer les cases visitées");
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
            resStage.setWidth(mazeCols * Cell.cellSize + 400);
            resStage.setHeight(mazeRows * Cell.cellSize + 340);
            resStage.setResizable(false);
        } else {
            // Fenêtre fixe (scrollable)
            resStage.setWidth(1400);  // 1000 (mazeView) + 400 (panel)
            resStage.setHeight(840);  // 500 (mazeView) + 340 (panel)
            resStage.setResizable(true); // pour l'utilisateur, optionnel
        }

        resStage.setTitle("Résolution du labyrinthe");
        resStage.show();
    }
    
    /** Updates the generation time label.
     * @param timeGenMs duration time of the generation.
     */
    public void setGenerationTime(long timeGenMs) {
        lTimeGen.setText("Temps de génération : " + timeGenMs + "ms");
    }

    /** Updates the solving time label.
     * @param timeResMs duration time of the resolution.
     */
    public void setSolvingTime (long timeResMs) {
            lTimeRes.setText("Temps de résolution : " + timeResMs + "ms");
    }

    /** Updates the label with number of visited cells.
     * @param visitedcellsNB number of visited cells.
     */
    public void setCellsVisited (long visitedcellsNB) {
        if (visitedcellsNB == 0) {
            lVisited.setText("Nombre de cases visitées par l'algorithme : Pas résolu");
        } else {
            lVisited.setText("Nombre de cases visitées par l'algorithme : " + visitedcellsNB);
        }
    }

    /** Updates the label with final path length.
     * @param finalcellsNB number of cells in the final path.
     */
    public void setCellsPath (long finalcellsNB) {
        if (finalcellsNB == 0) {
            lPath.setText("Taille du hemin final : Pas résolu");
        } else {
            lPath.setText("Taille du chemin final : " + finalcellsNB);
        }
    }

    /** Enables or disables all interactive controls. 
     * @param enabled true to enable all of the button of a ResolutionView.
    */
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

    /** Displays a "solving in progress" message. 
     * @param inProgress true to change the status of lTimeRes
    */
    public void setSolvingInProgress(boolean inProgress) {
        if (inProgress) {
            lTimeRes.setText("Temps de résolution : En cours...");
        }
    }

    /** Displays a "generation in progress" message. 
     * @param inProgress true to change the status of lTimeRes
    */
    public void setGenerationInProgress(boolean inProgress) {
        if (inProgress) {
            lTimeGen.setText("Temps de génération : En cours...");
        }
    }
}
