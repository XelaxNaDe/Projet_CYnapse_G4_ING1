package projatlab.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projatlab.algorithms.generators.MazeGenerator;
import projatlab.algorithms.generators.MazeGeneratorDFS;
import projatlab.algorithms.generators.MazeGeneratorKruskal;
import projatlab.algorithms.generators.MazeGeneratorPrim;
import projatlab.model.Cell;
import projatlab.model.Maze;
import projatlab.view.MazeView;
import projatlab.view.ResolverView;

public class GenerationController {

    public void handleGenerateMaze(String widthText, String heightText, String seedText, String genAlgo, String mode, double AnimationSpeed, Boolean isPerfect, Stage stage) {
        try {
            int width = Integer.parseInt(widthText);
            int height = Integer.parseInt(heightText);

            if (width <= 0 || height <= 0) {
                System.out.println("Erreur : la taille du labyrinthe doit être supérieure à 0.");
                return;
            }
            if (width == 1 && height == 1) {
                System.out.println("Avertissement : le labyrinthe 1x1 n'a qu'une seule case.");
                return;
            }

            long seed;
            if (seedText == null || seedText.isEmpty()){
                seed = System.currentTimeMillis();
            }
            else {
                seed = Long.parseLong(seedText);
            }

            Maze maze = new Maze(width, height);

            int maxWidth = 1000;
            int maxHeight = 500;

            int refCellSizeWidth = maxWidth / 100;
            int refCellSizeHeight = maxHeight / 50;
            int refCellSize = Math.min(refCellSizeWidth, refCellSizeHeight);
            refCellSize = Math.min(refCellSize, 40);
            refCellSize = Math.max(refCellSize, 5);

            if (width <= 100 && height <= 50) {
                int cellSizeWidth = maxWidth / width;
                int cellSizeHeight = maxHeight / height;
                Cell.cellSize = Math.min(cellSizeWidth, cellSizeHeight);
                Cell.cellSize = Math.min(Cell.cellSize, 40);
                Cell.cellSize = Math.max(Cell.cellSize, 5);
            } else {
                Cell.cellSize = refCellSize;
            }



            MazeGenerator generator;
            switch (genAlgo) {
                case "DFS" -> generator = new MazeGeneratorDFS(maze, seed, isPerfect);
                case "Prim" -> generator = new MazeGeneratorPrim(maze,seed, isPerfect);
                case "Kruskal" -> generator = new MazeGeneratorKruskal(maze, seed, isPerfect); 
                default -> throw new AssertionError();
            }
            
            MazeView mazeView = new MazeView(maze);
            MazeController mazeController = new MazeController(maze, mazeView, seed);
            ResolverView resWindow = new ResolverView(maze, mazeController, mazeView);
            resWindow.controller.setResolverView(resWindow);
            mazeController.setGenerator(generator);
            resWindow.show();

            mazeController.setGenerationListener(time -> {
                javafx.application.Platform.runLater(() -> {
                    resWindow.setGenerationTime(time);
                });
            });

            System.out.println(mode);
            if ("step".equals(mode)){
                mazeController.startGenerationAnimation(AnimationSpeed);
            }
            else{
                mazeController.noGenerationAnimation();
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

                //start & end
                String startendLine = reader.readLine();
                String[] startend = startendLine.split(" ");
                int startI = Integer.parseInt(startend[0]);
                int startJ = Integer.parseInt(startend[1]);
                int endI = Integer.parseInt(startend[2]);
                int endJ = Integer.parseInt(startend[3]);

                Cell startCell = maze.getCell(maze.index(startI, startJ));
                Cell endCell = maze.getCell(maze.index(endI,endJ));

                maze.setStart(startCell);
                maze.setEnd(endCell);
                startCell.setStart(true);
                endCell.setEnd(true);

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

                MazeController mazeController = new MazeController(maze, mazeView, 0);
                mazeController.drawAll();

                ResolverView resWindow = new ResolverView(maze, mazeController, mazeView);
                resWindow.controller.setResolverView(resWindow);

                resWindow.show();
                resWindow.setGenerationTime(0);

                System.out.println("Labyrinthe chargé depuis : " + file.getAbsolutePath());

            } catch (IOException | NumberFormatException e) {
                System.err.println("Erreur lors du chargement : " + e.getMessage());
            }
        }
    }
}
