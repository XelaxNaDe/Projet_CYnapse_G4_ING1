package projatlab;

import javafx.application.Application;
import javafx.stage.Stage;
import projatlab.controller.GenerationController;
import projatlab.view.GenerationView;

/**
 * Entry point for the Maze Generation and Resolution JavaFX application.
 * This class initializes the primary stage and sets up the maze generation view.
 */
public class Main extends Application {

    /**
     * Called when the JavaFX application starts.
     * Sets up and displays the maze generation window.
     *
     * @param genStage The primary stage (window) provided by the JavaFX runtime.
     */
    @Override
    public void start(Stage genStage) {
        GenerationController controller = new GenerationController();
        GenerationView genWindow = new GenerationView(controller);
        genWindow.show(genStage);
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
