package projatlab.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import projatlab.model.Maze;
import projatlab.view.ModificationView;

public class ResolverController {

    private final Maze maze;

    public ResolverController(Maze maze) {
        this.maze = maze;
    }

    public void handleModify() {
        ModificationView modWindow = new ModificationView(maze);
        modWindow.show();
    }

   
    public void handleSave(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder le labyrinthe");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"));

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(maze.getCols() + " " + maze.getRows());
                writer.newLine();
                for (var cell : maze.getGrid()) {
                    writer.write(cell.i + " " + cell.j + " " +
                            (cell.walls[0] ? "1" : "0") +
                            (cell.walls[1] ? "1" : "0") +
                            (cell.walls[2] ? "1" : "0") +
                            (cell.walls[3] ? "1" : "0"));
                    writer.newLine();
                }
                System.out.println("Labyrinthe sauvegardé dans " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
            }
        }
    }

    public void handleSolve(boolean useAStar, boolean useBFS, boolean useDFS, boolean isCompleteMode) {
        // Exemple de logique future
        if (useAStar) {
            System.out.println("Résolution avec A* " + (isCompleteMode ? "en mode complet" : "en mode pas à pas"));
        } else if (useBFS) {
            System.out.println("Résolution avec BFS " + (isCompleteMode ? "en mode complet" : "en mode pas à pas"));
        } else if (useDFS) {
            System.out.println("Résolution avec DFS " + (isCompleteMode ? "en mode complet" : "en mode pas à pas"));
        } else {
            System.out.println("Veuillez sélectionner un algorithme.");
        }

        // TODO : intégrer les algorithmes de résolution ici
    }
}
