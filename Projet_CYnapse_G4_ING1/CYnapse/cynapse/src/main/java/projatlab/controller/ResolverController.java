package projatlab.controller;

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
