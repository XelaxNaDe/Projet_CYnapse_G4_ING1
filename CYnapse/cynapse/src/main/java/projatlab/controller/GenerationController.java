package projatlab.controller;

import javafx.stage.Stage;
import projatlab.model.Maze;
import projatlab.view.ResolverView;

public class GenerationController {

    public void handleGenerateMaze(String widthText, String heightText, Stage stage) {
        try {
            int width = Integer.parseInt(widthText);
            int height = Integer.parseInt(heightText);

            Maze maze = new Maze(width, height);
            ResolverView resWindow = new ResolverView(maze);
            resWindow.show();

        } catch (NumberFormatException e) {
            System.out.println("Dimensions invalides. Entrez des entiers valides.");
        }
    }

    public void handleLoadMaze(Stage stage) {
        // À implémenter : chargement d'un labyrinthe depuis un fichier
        System.out.println("Fonction de chargement de labyrinthe à implémenter.");
    }
}
