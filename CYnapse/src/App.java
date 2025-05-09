import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Entrez votre nom :");
        TextField textField = new TextField();
        Button button = new Button("Dire bonjour");
        Label result = new Label();

        button.setOnAction(e -> {
            String name = textField.getText();
            result.setText("Bonjour, " + name + " !");
        });

        VBox layout = new VBox(10); // 10 pixels d'espacement
        layout.getChildren().addAll(label, textField, button, result);

        Scene scene = new Scene(layout, 300, 200);

        primaryStage.setTitle("Exemple JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
