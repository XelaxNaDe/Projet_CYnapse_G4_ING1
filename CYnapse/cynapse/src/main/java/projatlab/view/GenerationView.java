package projatlab.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class GenerationView {
    public void show(Stage genStage) {

        StackPane mainPane = new StackPane();

        

        

        // Seed Field
        TextField tfSeed = new TextField();
        tfSeed.setPromptText("Seed");
        tfSeed.setPrefWidth(150);

        //Perfect Check
        CheckBox cbPerfect = new CheckBox("Parfait");

        
        HBox hbSeedPerfect = new HBox(20);
        hbSeedPerfect.getChildren().addAll(tfSeed, cbPerfect);


        //Size Choice
        Label lSize = new Label("Taille");

        TextField tfWidth = new TextField();
        tfWidth.setPromptText("Hauteur");
        tfWidth.setPrefWidth(90);

        Label lX = new Label("X");

        TextField tfHeight = new TextField();
        tfHeight.setPromptText("Largeur");
        tfHeight.setPrefWidth(90);

        
        HBox hbSize = new HBox(20);
        hbSize.getChildren().addAll(tfWidth, lX, tfHeight);

        VBox vbSize = new VBox();
        vbSize.getChildren().addAll(lSize, hbSize);


        VBox vbTopRight = new VBox();
        vbTopRight.getChildren().addAll(hbSeedPerfect, vbSize);


        //Algorithms
        Label lAlgo = new Label("Algorithmes : ");

        CheckBox cbAlgo1 = new CheckBox(".....");
        CheckBox cbAlgo2 = new CheckBox(".....");

        cbAlgo1.setOnAction(e -> {
            if (cbAlgo1.isSelected()) {
                cbAlgo2.setSelected(false);
            }
        });

        cbAlgo2.setOnAction(e -> {
            if (cbAlgo2.isSelected()) {
                cbAlgo1.setSelected(false);
            }
        });


        VBox vbTopLeft = new VBox();
        vbTopLeft.getChildren().addAll(lAlgo, cbAlgo1, cbAlgo2);


        Label lModeG = new Label("Modes de génération :");

        CheckBox cbStep = new CheckBox("Pas à pas");
        CheckBox cbComplet = new CheckBox("Complet");

        cbStep.setOnAction(e -> {
            if (cbStep.isSelected()) {
                cbComplet.setSelected(false);
            }
        });

        cbComplet.setOnAction(e -> {
            if (cbComplet.isSelected()) {
                cbStep.setSelected(false);
            }
        });

        VBox vbBotLeft = new VBox();
        vbBotLeft.getChildren().addAll(lModeG, cbStep, cbComplet);


        // Button Load
        Button btnLoad = new Button("Charger un labyrinthe");
        btnLoad.setPrefSize(150, 40);
        btnLoad.setOnAction(e -> openResWindow());

        // Button Generate
        Button btnGenerate = new Button("Générer un labyrinthe");
        btnGenerate.setPrefSize(150, 40);
        btnGenerate.setOnAction(e -> openResWindow());

        VBox vbBotRight = new VBox();
        vbBotRight.getChildren().addAll(btnLoad, btnGenerate);



        // GridPane
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

        // Scene creation
        genStage.setTitle("Génération");
        Scene scene = new Scene(mainPane, 400, 200);
        genStage.setScene(scene);
        genStage.show();
    }

    public void openResWindow() {
        ResolverView resWindow = new ResolverView();
        resWindow.show();
    }
}
