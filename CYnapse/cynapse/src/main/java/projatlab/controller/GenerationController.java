package projatlab.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projatlab.algorithms.generators.MazeGenerator;
import projatlab.algorithms.generators.MazeGeneratorDFS;
import projatlab.algorithms.generators.MazeGeneratorKruskal;
import projatlab.algorithms.generators.MazeGeneratorPrim;
import projatlab.model.Cell;
import projatlab.model.Maze;
import projatlab.view.ErrorView;
import projatlab.view.MazeView;
import projatlab.view.ResolverView;

/**
 * GenerationController handles maze generation and loading from files
 * It manages user input, initializes the maze and UI components
 */
public class GenerationController {

    /**
     * Generates a new maze using  parameters and displays it
     *
     * @param widthText       The width of the maze
     * @param heightText      The height of the maze 
     * @param seedText        The seed for maze generation (optional)
     * @param genAlgo         The generation algorithm to use
     * @param mode            The generation mode 
     * @param AnimationSpeed  The speed of animation in step mode
     * @param stage           The JavaFX stage used 
     */
    public void handleGenerateMaze(String widthText, String heightText, String seedText, String genAlgo, String mode, double AnimationSpeed, Boolean isPerfect, Stage stage) {
        try {
            int width = Integer.parseInt(widthText);
            int height = Integer.parseInt(heightText);

            if (width <= 0 || height <= 0) {
                ErrorView.showError("Erreur : la taille du labyrinthe doit être supérieure à 0.");
                return;
            }
            if (width == 1 && height == 1) {
                ErrorView.showError("Avertissement : le labyrinthe 1x1 n'a qu'une seule case.");
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
            ErrorView.showError("Entrez des entiers valides.");
        }
    }

    
    /**
     * Handles loading a maze from a .txt file
     *
     * @param stage The JavaFX stage used 
     */    
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

                if (dimensions.length !=2) throw new IOException("Problème de dimension");

                Maze maze = new Maze(cols, rows);

                //start & end
                String startendLine = reader.readLine();
                String[] startend = startendLine.split(" ");


                if (startend.length != 4) throw new IOException("Problème de départ/arrivé)");

                int startI = Integer.parseInt(startend[0]);
                int startJ = Integer.parseInt(startend[1]);
                int endI = Integer.parseInt(startend[2]);
                int endJ = Integer.parseInt(startend[3]);

                Cell startCell = maze.getCell(maze.index(startI, startJ));
                Cell endCell = maze.getCell(maze.index(endI,endJ));

                if (startI < 0 || startI >= cols || startJ < 0 || startJ >= rows || endI < 0 || endI >= cols || endJ < 0 || endJ >= rows) {
                    throw new IOException("Coordonnées départ/arrivé hors limites");
                }

                maze.setStart(startCell);
                maze.setEnd(endCell);
                startCell.setStart(true);
                endCell.setEnd(true);


                //cells
                List<String> wallLines = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    wallLines.add(line.trim());
                }

                int expectedLines = rows * cols;
                if (wallLines.size() != expectedLines) {
                    throw new IOException(expectedLines + " lignes attendus, " + wallLines.size() + " lignes trouvés");
                }

                    List<Cell> grid = maze.getGrid();

                        for (int idx = 0; idx < expectedLines; idx++) {
                            String wallLine = wallLines.get(idx);

                            if (wallLine.length() != 4 || !wallLine.matches("[01]{4}")) {
                                throw new IOException("Problème de murs à la ligne " + (idx + 3));
                            }

                            Cell cell = grid.get(idx);

                            for (int k = 0; k < 4; k++) {
                                cell.walls[k] = wallLine.charAt(k) == '1';
                            }
                        }
                

                MazeView mazeView = new MazeView(maze);

                MazeController mazeController = new MazeController(maze, mazeView, 0);
                mazeController.drawAll();

                ResolverView resWindow = new ResolverView(maze, mazeController, mazeView);
                resWindow.controller.setResolverView(resWindow);

                resWindow.show();
                resWindow.setGenerationTime(0);

            } catch (IOException e) {
                ErrorView.showError("Erreur lors du chargement : " + e.getMessage());
            } catch (NumberFormatException e) {
                ErrorView.showError("Erreur lors du chargement : Mauvais type de charactère" );
            }
        }
    }
}
