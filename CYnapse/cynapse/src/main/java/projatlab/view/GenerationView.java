package projatlab.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import projatlab.controller.GenerationController;

/**
 * GenerationView is the JavaFX user interface class for maze generation configuration.
 * It allows users to input maze dimensions, select algorithms and modes, define randomness seed,
 * and trigger maze generation or loading actions through a linked controller.
 */
public class GenerationView {

    /** Text field for maze width input. */
    private TextField tfWidth;

    /** Text field for maze height input. */
    private TextField tfHeight;

    /** Controller responsible for handling user actions. */
    private final GenerationController controller;

    /**
     * Constructs a GenerationView with a reference to its controller.
     *
     * @param controller The controller managing generation actions.
     */
    public GenerationView(GenerationController controller) {
        this.controller = controller;
    }

    /**
     * Displays the maze generation configuration window.
     *
     * @param genStage The JavaFX stage to render the view.
     */
    public void show(Stage genStage) {
        StackPane mainPane = new StackPane();

        // --- Seed input and perfect maze checkbox ---
        TextField tfSeed = new TextField();
        tfSeed.setPromptText("Seed");
        tfSeed.setPrefWidth(150);

        CheckBox cbPerfect = new CheckBox("Parfait");
        cbPerfect.setSelected(true);

        HBox hbSeedPerfect = new HBox(20, tfSeed, cbPerfect);

        // --- Maze size input fields ---
        Label lSize = new Label("Taille");

        tfWidth = new TextField();
        tfWidth.setPromptText("Largeur");
        tfWidth.setPrefWidth(90);

        Label lX = new Label("X");

        tfHeight = new TextField();
        tfHeight.setPromptText("Hauteur");
        tfHeight.setPrefWidth(90);

        HBox hbSize = new HBox(20, tfWidth, lX, tfHeight);
        VBox vbSize = new VBox(lSize, hbSize);

        VBox vbTopRight = new VBox(15, hbSeedPerfect, vbSize);

        // --- Algorithm selection ---
        Label lAlgo = new Label("Algorithmes : ");

        ComboBox<String> cBAlgo = new ComboBox<>();
        cBAlgo.getItems().addAll("DFS", "Prim", "Kruskal");
        cBAlgo.setValue("DFS");

        VBox vbTopLeft = new VBox(10, lAlgo, cBAlgo);

        // --- Generation mode and speed slider ---
        Label lModeG = new Label("Modes de génération :");

        ToggleGroup generationModeGroup = new ToggleGroup();

        RadioButton rbComplet = new RadioButton("Complet");
        rbComplet.setToggleGroup(generationModeGroup);
        rbComplet.setSelected(true);

        RadioButton rbStep = new RadioButton("Pas à pas");
        rbStep.setToggleGroup(generationModeGroup);

        Slider sSpeed = new Slider(1, 100, 10);
        sSpeed.setPrefWidth(100);
        sSpeed.setShowTickMarks(true);
        sSpeed.setMajorTickUnit(25);
        sSpeed.setBlockIncrement(5);

        Label lSpeed = new Label("10 ms");
        sSpeed.valueProperty().addListener((obs, oldVal, newVal) ->
            lSpeed.setText(newVal.intValue() + " ms")
        );

        HBox hbStep = new HBox(5, rbStep, sSpeed, lSpeed);
        VBox vbBotLeft = new VBox(10, lModeG, rbComplet, hbStep);

        // --- Action buttons: load and generate ---
        Button btnLoad = new Button("Charger un labyrinthe");
        btnLoad.setOnAction(e -> controller.handleLoad(genStage));

        Button btnGenerate = new Button("Générer un labyrinthe");
        btnGenerate.setOnAction(e -> {
            String mode = rbComplet.isSelected() ? "complet" : "step";
            controller.handleGenerateMaze(
                tfWidth.getText(),
                tfHeight.getText(),
                tfSeed.getText(),
                cBAlgo.getValue(),
                mode,
                sSpeed.getValue(),
                cbPerfect.isSelected(),
                genStage
            );
        });

        VBox vbBotRight = new VBox(10, btnLoad, btnGenerate);

        // --- Layout configuration ---
        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setVgap(15);
        root.setHgap(10);
        root.setAlignment(Pos.CENTER);

        root.add(vbTopRight, 0, 0);
        root.add(vbTopLeft, 1, 0);
        root.add(vbBotLeft, 0, 1);
        root.add(vbBotRight, 1, 1);

        mainPane.getChildren().add(root);
        mainPane.setAlignment(Pos.CENTER);

        // --- Final scene setup ---
        genStage.setTitle("Génération du labyrinthe");
        Scene scene = new Scene(mainPane, 400, 200);
        genStage.setResizable(false);
        genStage.setScene(scene);
        genStage.show();
    }
}
