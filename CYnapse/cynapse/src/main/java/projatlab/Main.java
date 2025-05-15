package projatlab;

import javafx.application.Application;
import javafx.stage.Stage;
import projatlab.controller.GenerationController;
import projatlab.view.GenerationView;

public class Main extends Application {

    @Override
    public void start(Stage genStage) {
        GenerationController controller = new GenerationController();
        GenerationView genWindow = new GenerationView(controller);
        genWindow.show(genStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
