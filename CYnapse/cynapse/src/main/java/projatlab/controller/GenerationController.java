package projatlab.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projatlab.algorithms.generation.MazeGenerator;
import projatlab.algorithms.generation.MazeGeneratorDFS;
import projatlab.algorithms.generation.MazeGeneratorKruskal;
import projatlab.algorithms.generation.MazeGeneratorPrim;
import projatlab.model.Cell;
import projatlab.model.Maze;
import projatlab.view.MazeView;
import projatlab.view.ResolverView;

public class GenerationController {

    private Maze maze;

    public void handleGenerateMaze(String widthText, String heightText, String seedText, String algo, String mode, Stage stage) {
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

            System.out.println(mode);
            if ("step".equals(mode)){
                mazeController.startAnimation();
            }
            else{
                mazeController.noAnimation();
            }



        } catch (NumberFormatException e) {
            System.out.println("Dimensions invalides. Entrez des entiers valides.");
        }
    }

    public void handleLoad(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Charger un labyrinthe");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                //dimensions
                String dimensionsLine = reader.readLine();
                String[] dimensions = dimensionsLine.split(" ");
                int cols = Integer.parseInt(dimensions[0]);
                int rows = Integer.parseInt(dimensions[1]);

                Maze maze = new Maze(cols, rows);

                //cells
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts.length == 3) {
                        int i = Integer.parseInt(parts[0]);
                        int j = Integer.parseInt(parts[1]);
                        String wallsStr = parts[2];

                        Cell cell = null;
                        for (Cell c : maze.getGrid()) {
                            if (c.i == i && c.j == j) {
                                cell = c;
                                break;
                            }
                        }

                        if (cell != null && wallsStr.length() == 4) {
                            for (int k = 0; k < 4; k++) {
                                cell.walls[k] = wallsStr.charAt(k) == '1';
                            }
                        }
                    }
                }

                MazeView mazeView = new MazeView(maze);

                MazeGenerator dummyGenerator = new MazeGenerator();
                MazeController mazeController = new MazeController(maze, mazeView, dummyGenerator);
                mazeController.drawAll();

                ResolverView resWindow = new ResolverView(maze, mazeController, mazeView);
                resWindow.show();

                System.out.println("Labyrinthe chargé depuis : " + file.getAbsolutePath());

            } catch (IOException | NumberFormatException e) {
                System.err.println("Erreur lors du chargement : " + e.getMessage());
            }
        }
    }
}
