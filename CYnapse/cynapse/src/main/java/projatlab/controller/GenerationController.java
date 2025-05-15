package projatlab.controller;

import javafx.stage.Stage;
import projatlab.algorithms.generation.dfs;
import projatlab.algorithms.generation.kruskal;
import projatlab.algorithms.generation.prim;
import projatlab.model.Maze;
import projatlab.model.MazeGenerator;
import projatlab.view.ResolverView;

public class GenerationController {

    public void handleGenerateMaze(String widthText, String heightText, String algo, Stage stage) {
        try {
            int width = Integer.parseInt(widthText);
            int height = Integer.parseInt(heightText);

            Maze maze = new Maze(width, height);

            MazeGenerator generator;
            switch (algo) {
                case "DFS" -> generator = new dfs(maze.getGrid(),width,height);
                case "Prim" -> generator = new prim(maze.getGrid(),width,height);
                case "Kruskal" -> generator = new kruskal(maze.getGrid(),width,height); 
                default -> throw new AssertionError();
            }
            

            ResolverView resWindow = new ResolverView(maze, generator);
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
