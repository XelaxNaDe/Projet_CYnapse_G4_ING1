package projatlab.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projatlab.algorithms.solvers.MazeSolver;
import projatlab.algorithms.solvers.MazeSolverAStar;
import projatlab.algorithms.solvers.MazeSolverDFS;
import projatlab.algorithms.solvers.MazeSolverDijkstra;
import projatlab.model.Maze;
import projatlab.view.ErrorView;
import projatlab.view.ModificationView;
import projatlab.view.ResolverView;

/**
 * ResolverController is responsible for handling the resolution
 * 
 */
public class ResolverController {

    /** Maze in question */
    private final Maze maze;

    /** Reference to the main maze controller */
    private final MazeController mazeController;

    /** The view to show the results */
    private ResolverView resWindow;

    /** Whether the maze is in the proccess of being solved */
    private boolean isSolving = false;

    /** Whether the maze is in the proccess of modified. */
    private boolean isEditing = false; 

    /**
     * Constructor of ResolverController with a maze and a reference to the maze controller.
     *
     * @param maze           The maze instance
     * @param mazeController The controller responsible for maze generation and drawing
     */
    public ResolverController(Maze maze, MazeController mazeController) {
        this.maze = maze;
        this.mazeController = mazeController;
    }

    /**
     * Opens a modification window allowing the user to edit the maze
     * Prevents modification during solving or generation
     */
    public void handleEdit() {
        if (isSolving || mazeController.isGenerating() || isEditing) return; // Prevents multiple modification
        
        setEditingState(true); // disables controls
        
        ModificationView modWindow = new ModificationView(maze);
        modWindow.showAndWait();  // wait until modal window closes
        
        mazeController.drawAll();
        
        setEditingState(false); //enables controls
    }

     /**
     * Initiates maze solving using the selected algorithm and mode
     *
     * @param maze     The maze to solve
     * @param algorithm The name of the algorithm 
     * @param mode      Solving mode
     * @param delayMs   Delay between animation steps 
     * @param stage     JavaFX stage used 
     */
    public void handleSolveMaze(Maze maze, String solvAlgo, String mode, double delayMs, Stage stage) {
        if (isSolving || mazeController.isGenerating() || isEditing) return;

        clearMaze();
        setSolvingState(true);

        MazeSolver solver;
        switch (solvAlgo) {
            case "DFS" -> solver = new MazeSolverDFS(maze);
            case "A*" -> solver = new MazeSolverAStar(maze);
            case "Dijkstra" -> solver = new MazeSolverDijkstra(maze);
            default -> {
                ErrorView.showError("Algorithme de résolution inconnu : " + solvAlgo);
                setSolvingState(false);
                return;
            }
        }

        mazeController.setSolvingListener(time -> {
            javafx.application.Platform.runLater(() -> {
                // Check if a solution was found
                if (!solver.isPathFound()) {
                    resWindow.setSolvingTime(time);
                    resWindow.setCellsVisited(solver.getVisitedCount());
                    resWindow.setCellsPath(0);
                    ErrorView.showError("Aucune solution trouvée ! Le labyrinthe n'a pas de chemin entre le point de départ et d'arrivée.");
                } else {
                    resWindow.setSolvingTime(time);
                    resWindow.setCellsVisited(solver.getVisitedCount());
                    resWindow.setCellsPath(solver.finalPath.size());
                }
                setSolvingState(false);
            });
        });

        mazeController.setSolver(solver);

        if ("step".equals(mode)){
            mazeController.startSolvingAnimation(delayMs);
        }
        else{
            mazeController.noSolvingAnimation();
        }
    }

    /**
     * Saves the current state of the maze to a .txt file
     *
     * @param stage The JavaFX stage used
     */
    public void handleSave(Stage stage) {
        if (isSolving || mazeController.isGenerating() || isEditing) return; // Empêche la sauvegarde pendant la résolution, génération ou modification
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder le labyrinthe");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"));

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(maze.getCols() + " " + maze.getRows());
                writer.newLine();
                writer.write(maze.getStart().i + " " + maze.getStart().j + " " + maze.getEnd().i + " " + maze.getEnd().j);
                writer.newLine();
                for (var cell : maze.getGrid()) {
                    writer.write(
                                (cell.walls[0] ? "1" : "0") +
                                (cell.walls[1] ? "1" : "0") +
                                (cell.walls[2] ? "1" : "0") +
                                (cell.walls[3] ? "1" : "0"));
                    writer.newLine();
                }
            } catch (IOException e) {
                ErrorView.showError("Erreur lors de la sauvegarde : " + e.getMessage());
            }
        }
    }

    /**
     * Sets the view that displays maze solving information
     *
     * @param resolverView The ResolverView instance
     */
    public void setResolverView(ResolverView resWindow) {
        this.resWindow = resWindow;
    }

     /**
     * Updates the solving state 
     *
     * @param solving true if maze is being solved
     */
    private void setSolvingState(boolean solving) {
        this.isSolving = solving;
        updateControlsState();
    }

    /**
     * Updates the editing state 
     *
     * @param solving true if maze is being modified
     */
    private void setEditingState(boolean Editing) {
        this.isEditing = Editing;
        updateControlsState();
    }

    /**
     * Updates the enabled/disabled state of UI controls depending on solving, editing, or generating states
     */
    private void updateControlsState() {
        if (resWindow != null) {
            boolean shouldDisable = isSolving || mazeController.isGenerating() || isEditing;
            resWindow.setControlsEnabled(!shouldDisable);
            
            // specific messages depending on state
            if (isSolving) {
                resWindow.setSolvingInProgress(true);
            } else if (isEditing) {
            } else if (mazeController.isGenerating()) {
                resWindow.setGenerationInProgress(true);
            } else {
                resWindow.setSolvingInProgress(false);
                resWindow.setGenerationInProgress(false);
            }
        }
    }

    /**
     * Returns whether the maze is currently being solved
     *
     * @return true if solving is in progress
     */
    public boolean isSolving() {
        return isSolving;
    }

    /**
     * Returns whether the maze is currently being modified
     *
     * @return true if editing is in progress
     */
    public boolean isEditing() {
        return isEditing;
    }

     /**
     * Triggers control state updates when generation state changes
     *
     * @param generating Whether generation is in progress
     */
    public void setGenerationInProgress(boolean generating) {
        updateControlsState();
    }

    /**
     * Clears the maze from previous solves 
     * redraws the maze, and resets solving statistics
     */    private void clearMaze() {
        for (var cell : maze.getGrid()) {
            cell.setVisited(false);
            cell.isInFinalPath = false;
        }
        
        mazeController.drawAll();
        
        if (resWindow != null) {
            resWindow.setCellsVisited(0);
            resWindow.setSolvingTime(0);
            resWindow.setCellsPath(0);
        }
    }
}