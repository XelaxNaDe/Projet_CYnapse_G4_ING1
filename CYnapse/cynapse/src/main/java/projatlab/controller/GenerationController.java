package projatlab.controller;

import javafx.stage.Stage;
import projatlab.algorithms.generation.MazeGenerator;
import projatlab.algorithms.generation.dfs;
import projatlab.algorithms.generation.kruskal;
import projatlab.algorithms.generation.prim;
import projatlab.model.Maze;
import projatlab.view.MazeView;
import projatlab.view.ResolverView;

public class GenerationController {

    public void handleGenerateMaze(String widthText, String heightText, String seedText, String algo, Stage stage) {
        try {
            int width = Integer.parseInt(widthText);
            int height = Integer.parseInt(heightText);

            long seed;
            if (seedText == null || seedText.isEmpty()){
                seed = System.currentTimeMillis();
            }
            else {
                seed = Long.parseLong(seedText);
            }

            Maze maze = new Maze(width, height);


            MazeGenerator generator;
            switch (algo) {
                case "DFS" -> generator = new dfs(maze.getGrid(),width,height,seed);
                case "Prim" -> generator = new prim(maze.getGrid(),width,height,seed);
                case "Kruskal" -> generator = new kruskal(maze.getGrid(),width,height,seed); 
                default -> throw new AssertionError();
            }
            
            MazeView mazeView = new MazeView(maze);
            MazeController mazeController = new MazeController(maze, mazeView, generator);
            ResolverView resWindow = new ResolverView(maze, mazeController, mazeView);
            resWindow.show();

            mazeController.setGenerationListener(time -> {
                javafx.application.Platform.runLater(() -> {
                    resWindow.setGenerationTime(time);
                });
            });

        } catch (NumberFormatException e) {
            System.out.println("Dimensions invalides. Entrez des entiers valides.");
        }
    }

    public void handleLoadMaze(Stage stage) {
        // À implémenter : chargement d'un labyrinthe depuis un fichier
        System.out.println("Fonction de chargement de labyrinthe à implémenter.");
    }
}
