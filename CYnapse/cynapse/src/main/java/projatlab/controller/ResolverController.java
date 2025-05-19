package projatlab.controller;

import javafx.stage.Stage;
import projatlab.algorithms.solvers.MazeSolver;
import projatlab.algorithms.solvers.MazeSolverDFS;
import projatlab.model.Maze;
import projatlab.view.ModificationView;

public class ResolverController {

    private final Maze maze;
    private final MazeController mazeController;

    public ResolverController(Maze maze, MazeController mazeController) {
        this.maze = maze;
        this.mazeController = mazeController;

    }

    public void handleModify() {
        ModificationView modWindow = new ModificationView(maze);
        modWindow.show();
    }

    public void handleSolveMaze(Maze maze, String solvAlgo, Stage stage) {
        try {
            MazeSolver solver;
            switch (solvAlgo) {
                //case "BFS" -> solver = new MazeSolverBFS(maze);
                case "DFS" -> solver = new MazeSolverDFS(maze);
                //case "Dijkstra" -> solver = new MazeSolverDijkstra(maze);
                //case "A*" -> solver = new MazeSolverAstar(maze);
                default -> throw new AssertionError();
                
            }
            mazeController.solveMaze(solver);

        } catch (NumberFormatException e) {}
    }
}
