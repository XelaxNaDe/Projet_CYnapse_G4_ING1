package projatlab.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorView {
    
    public static void showError(String message) {
        Stage errorStage = new Stage();
        errorStage.initModality(Modality.APPLICATION_MODAL);
        errorStage.setTitle("Erreur");
        errorStage.setResizable(false);
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f8f9fa;");
        
        // Message Label
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(300);
        
    
        Button okButton = new Button("OK");
        okButton.setPrefWidth(80);
        okButton.setStyle("-fx-background-color: white; " +
                        "-fx-text-fill: black; " +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;");

        okButton.setOnMouseEntered(e -> 
            okButton.setStyle("-fx-background-color: #8a8687; " +
                            "-fx-text-fill: black; " +
                            "-fx-border-color: black; " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-radius: 5px; " +
                            "-fx-background-radius: 5px;"));

        okButton.setOnMouseExited(e -> 
            okButton.setStyle("-fx-background-color: white; " +
                            "-fx-text-fill: black; " +
                            "-fx-border-color: black; " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-radius: 5px; " +
                            "-fx-background-radius: 5px;"));
        okButton.setOnAction(e -> errorStage.close());


        root.getChildren().addAll(errorLabel, okButton);
        
        Scene scene = new Scene(root);
        errorStage.setScene(scene);
        errorStage.showAndWait();
    }
}