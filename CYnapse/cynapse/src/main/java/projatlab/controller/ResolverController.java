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

public class ResolverController {

    private final Maze maze;
    private final MazeController mazeController;
    private ResolverView resWindow;
    private boolean isSolving = false;
    private boolean isModifying = false; // Nouvel état pour la modification

    public ResolverController(Maze maze, MazeController mazeController) {
        this.maze = maze;
        this.mazeController = mazeController;
    }

    public void handleModify() {
        if (isSolving || mazeController.isGenerating() || isModifying) return; // Empêche la modification multiple
        
        setModifyingState(true); // Désactive tous les contrôles
        
        ModificationView modWindow = new ModificationView(maze);
        modWindow.showAndWait();  // Bloque jusqu'à fermeture de la fenêtre modale
        
        // Après fermeture de la fenêtre modale, on redessine le labyrinthe modifié
        mazeController.drawAll();
        
        setModifyingState(false); // Réactive tous les contrôles
    }

    public void handleSolveMaze(Maze maze, String solvAlgo, String mode, double delayMs, Stage stage) {
        if (isSolving || mazeController.isGenerating() || isModifying) return; // Empêche de lancer une nouvelle résolution pendant génération/résolution/modification
        
        // Clear le labyrinthe avant de commencer une nouvelle résolution
        clearMaze();
        
        setSolvingState(true);
        
        MazeSolver solver;
        switch (solvAlgo) {
            case "DFS" -> solver = new MazeSolverDFS(maze);
            case "A*" -> solver = new MazeSolverAStar(maze);
            case "Dijkstra" -> solver = new MazeSolverDijkstra(maze);
            default -> throw new AssertionError();
        }

        mazeController.setSolvingListener(time -> {
            javafx.application.Platform.runLater(() -> {
                resWindow.setSolvingTime(time);
                int cellsVisited = solver.getVisitedCount();
                resWindow.setCellsVisited(cellsVisited);
                setSolvingState(false); // Réactive les contrôles à la fin
            });
        });

        mazeController.setSolver(solver);
        System.out.println(mode);
        if ("step".equals(mode)){
            mazeController.startSolvingAnimation(delayMs);
        }
        else{
            mazeController.noSolvingAnimation();
        }
    }

    public void handleSave(Stage stage) {
        if (isSolving || mazeController.isGenerating() || isModifying) return; // Empêche la sauvegarde pendant la résolution, génération ou modification
        
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

    public void setResolverView(ResolverView resWindow) {
        this.resWindow = resWindow;
    }

    // Méthode pour gérer l'état de résolution
    private void setSolvingState(boolean solving) {
        this.isSolving = solving;
        updateControlsState();
    }

    // Méthode pour gérer l'état de modification
    private void setModifyingState(boolean modifying) {
        this.isModifying = modifying;
        updateControlsState();
    }

    // Méthode centralisée pour mettre à jour l'état des contrôles
    private void updateControlsState() {
        if (resWindow != null) {
            boolean shouldDisable = isSolving || mazeController.isGenerating() || isModifying;
            resWindow.setControlsEnabled(!shouldDisable);
            
            // Messages spécifiques selon l'état
            if (isSolving) {
                resWindow.setSolvingInProgress(true);
            } else if (isModifying) {
                resWindow.setModificationInProgress(true);
            } else if (mazeController.isGenerating()) {
                resWindow.setGenerationInProgress(true);
            } else {
                // Tous les états sont faux, on peut réactiver
                resWindow.setSolvingInProgress(false);
                resWindow.setModificationInProgress(false);
                resWindow.setGenerationInProgress(false);
            }
        }
    }

    public boolean isSolving() {
        return isSolving;
    }

    public boolean isModifying() {
        return isModifying;
    }

    // Méthode pour gérer l'état de génération depuis MazeController
    public void setGenerationInProgress(boolean generating) {
        updateControlsState();
    }

    // Méthode pour nettoyer le labyrinthe avant une nouvelle résolution
    private void clearMaze() {
        // Reset tous les états de visite et de chemin final
        for (var cell : maze.getGrid()) {
            cell.setVisited(false);
            cell.isInFinalPath = false;
        }
        
        // Redessiner le labyrinthe pour montrer qu'il est nettoyé
        mazeController.drawAll();
        
        // Reset les statistiques dans l'interface
        if (resWindow != null) {
            resWindow.setCellsVisited(0);
            resWindow.setSolvingTime(0);
        }
    }
}