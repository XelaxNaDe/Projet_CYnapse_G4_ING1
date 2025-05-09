package projatlab;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Créer un GridPane (grille)
        GridPane grid = new GridPane();
        
        // Définir les marges entre les cellules de la grille
        grid.setVgap(10); // Espace vertical
        grid.setHgap(10); // Espace horizontal
        
        // Définir l'alignement des éléments à l'intérieur de la grille
        grid.setAlignment(Pos.CENTER);

        // Créer quelques composants à ajouter à la grille
        Label label1 = new Label("Nom:");
        Label label2 = new Label("Prénom:");
        Button button1 = new Button("Envoyer");
        Button button2 = new Button("Annuler");

        // Placer les composants dans la grille
        grid.add(label1, 0, 0);  // (colonne, ligne)
        grid.add(label2, 0, 1);
        grid.add(button1, 1, 0);
        grid.add(button2, 1, 1);

        // Créer une scène avec le GridPane
        Scene scene = new Scene(grid, 300, 200);

        // Définir le titre de la fenêtre
        primaryStage.setTitle("Labyrinthe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}