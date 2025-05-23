package projatlab.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projatlab.controller.ModificationController;
import projatlab.model.Cell;
import projatlab.model.Maze;

public class ModificationView {
    
    private Maze originalMaze;
    private Maze copiedMaze;
    private Stage modStage;

    public ModificationView(Maze maze) {
        this.originalMaze = maze;
        this.copiedMaze = maze.copy();
    }

    public void showAndWait() {
        modStage = new Stage();
        BorderPane root = new BorderPane();

        MazeView mazeView = new MazeView(copiedMaze);
        ModificationController controller = new ModificationController(copiedMaze, mazeView, originalMaze, modStage);
        mazeView.setController(controller);
        mazeView.draw();

        // ScrollPane pour mazeView comme dans ResolverView
        ScrollPane scrollPane = new ScrollPane(mazeView);
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setPrefViewportWidth(1000); 
        scrollPane.setPrefViewportHeight(500);
        root.setCenter(scrollPane);

        // Right Button Panel
        VBox vbChange = new VBox(10);
        vbChange.setAlignment(Pos.CENTER_RIGHT);
        vbChange.setPadding(new Insets(20));

        Button btnMur = new Button("Mur");
        Button btnEntree = new Button("Entrée");
        Button btnSortie = new Button("Sortie");
        Button btnAnnuler = new Button("Annuler");
        Button btnSave = new Button("Sauvgarder les changements");

        btnMur.setMaxWidth(Double.MAX_VALUE);
        btnEntree.setMaxWidth(Double.MAX_VALUE);
        btnSortie.setMaxWidth(Double.MAX_VALUE);
        btnAnnuler.setMaxWidth(Double.MAX_VALUE);
        btnSave.setMaxWidth(Double.MAX_VALUE);

        btnMur.setOnAction(e -> controller.setMode(ModificationController.Mode.MUR));
        btnEntree.setOnAction(e -> controller.setMode(ModificationController.Mode.ENTREE));
        btnSortie.setOnAction(e -> controller.setMode(ModificationController.Mode.SORTIE));
        btnAnnuler.setOnAction(e -> modStage.close());
        btnSave.setOnAction(e -> controller.handleSave());

        vbChange.getChildren().addAll(btnMur, btnEntree, btnSortie, btnAnnuler, btnSave);
        root.setRight(vbChange);

        // Scene
        Scene scene = new Scene(root);
        modStage.setScene(scene);

        int mazeCols = copiedMaze.getCols();
        int mazeRows = copiedMaze.getRows();

        if (mazeCols <= 100 && mazeRows <= 50) {
            modStage.setWidth(mazeCols * Cell.cellSize + 300);
            modStage.setHeight(mazeRows * Cell.cellSize + 140);
            modStage.setResizable(false);
        } else {
            modStage.setWidth(1300);
            modStage.setHeight(640);
            modStage.setResizable(true);
        }

        modStage.setTitle("Modification du labyrinthe");
        modStage.showAndWait();
    }
}
