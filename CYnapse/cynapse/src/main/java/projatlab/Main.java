package projatlab;

import javafx.application.Application;
import javafx.stage.Stage;
import projatlab.view.GenerationView;

public class Main extends Application{

    @Override
    public void start(Stage genStage) {
        GenerationView genWindow = new GenerationView();
        genWindow.show(genStage);
    }
    public static void main(String[] args) {
        launch(args);
    }
}