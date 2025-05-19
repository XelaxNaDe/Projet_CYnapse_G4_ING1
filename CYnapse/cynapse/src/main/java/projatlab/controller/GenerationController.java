package projatlab.controller;

import javafx.stage.Stage;
import projatlab.algorithms.generation.MazeGenerator;
import projatlab.algorithms.generation.MazeGeneratorDFS;
import projatlab.algorithms.generation.MazeGeneratorKruskal;
import projatlab.algorithms.generation.MazeGeneratorPrim;
import projatlab.model.Maze;
import projatlab.view.MazeView;
import projatlab.view.ResolverView;

public class GenerationController {

    public void handleGenerateMaze(String widthText, String heightText, String seedText, String genAlgo, Stage stage) {
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
            switch (genAlgo) {
                case "DFS" -> generator = new MazeGeneratorDFS(maze, seed);
                case "Prim" -> generator = new MazeGeneratorPrim(maze,seed);
                case "Kruskal" -> generator = new MazeGeneratorKruskal(maze, seed); 
                default -> throw new AssertionError();
            }
            
            MazeView mazeView = new MazeView(maze);
            MazeController mazeController = new MazeController(maze, mazeView, seed);
            ResolverView resWindow = new ResolverView(maze, mazeController, mazeView);
            mazeController.generateMaze(generator);
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
