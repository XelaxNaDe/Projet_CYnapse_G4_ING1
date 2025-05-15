package projatlab.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projatlab.controller.GenerationController;

public class GenerationView {

    private TextField tfWidth;
    private TextField tfHeight;

    private final GenerationController controller;

    public GenerationView(GenerationController controller) {
        this.controller = controller;
    }

    public void show(Stage genStage) {

        StackPane mainPane = new StackPane();

        // --- Seed Field & Perfect checkbox ---
        TextField tfSeed = new TextField();
        tfSeed.setPromptText("Seed");
        tfSeed.setPrefWidth(150);

        CheckBox cbPerfect = new CheckBox("Parfait");

        cbPerfect.setSelected(true);

        HBox hbSeedPerfect = new HBox(20, tfSeed, cbPerfect);

        // --- Size input fields ---
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

        // --- Algorithms checkboxes ---
        Label lAlgo = new Label("Algorithmes : ");

        ComboBox<String> algoComboBox = new ComboBox<>();
            algoComboBox.getItems().addAll("DFS", "Prim", "Kruskal");
            algoComboBox.setValue("DFS");

        
        VBox vbTopLeft = new VBox (10, lAlgo, algoComboBox);

        // --- Generation mode checkboxes ---
        Label lModeG = new Label("Modes de génération :");

        CheckBox cbComplet = new CheckBox("Complet");
        CheckBox cbStep = new CheckBox("Pas à pas");

        cbComplet.setSelected(true);


        cbStep.setOnAction(e -> {
            if (cbStep.isSelected()) cbComplet.setSelected(false);
        });

        cbComplet.setOnAction(e -> {
            if (cbComplet.isSelected()) cbStep.setSelected(false);
        });

        VBox vbBotLeft = new VBox(10, lModeG, cbComplet, cbStep);

        // --- Buttons ---
        Button btnLoad = new Button("Charger un labyrinthe");
        btnLoad.setOnAction(e -> controller.handleLoadMaze(genStage));

        Button btnGenerate = new Button("Générer un labyrinthe");
        btnGenerate.setOnAction(e -> controller.handleGenerateMaze(
                tfWidth.getText(),
                tfHeight.getText(),
                tfSeed.getText(),
                algoComboBox.getValue(),
                genStage
        ));

        VBox vbBotRight = new VBox(10, btnLoad, btnGenerate);

        // --- Layout GridPane ---
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

        // --- Scene setup ---
        genStage.setTitle("Génération");
        Scene scene = new Scene(mainPane, 400, 200);
        genStage.setScene(scene);
        genStage.show();
    }
}
