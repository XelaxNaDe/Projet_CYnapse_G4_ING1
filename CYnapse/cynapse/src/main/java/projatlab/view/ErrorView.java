package projatlab.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * ErrorView is a utility class for displaying styled error popup dialogs in a JavaFX application.
 * It provides a static method {@code showError(String)} to display an error message
 * in a modal window with a custom "OK" button.
 */
public class ErrorView {
    
    /**
     * Displays a modal error popup window with a provided message.
     *
     * @param message The error message to display.
     */
    public static void showError(String message) {
        // Create a new stage for the error dialog
        Stage errorStage = new Stage();
        errorStage.initModality(Modality.APPLICATION_MODAL);
        errorStage.setTitle("Erreur");
        errorStage.setResizable(false);

        // Layout container with padding and spacing
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f8f9fa;");
        
        // Label to display the error message
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(300);
        
        // OK button to close the dialog
        Button okButton = new Button("OK");
        okButton.setPrefWidth(80);
        okButton.setStyle("-fx-background-color: white; " +
                        "-fx-text-fill: black; " +
                        "-fx-border-color: black; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;");
        // Hover effects
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
        
        // Close the dialog when the button is clicked
        okButton.setOnAction(e -> errorStage.close());

        // Add components to layout
        root.getChildren().addAll(errorLabel, okButton);
        
        // Set the scene and show the dialog
        Scene scene = new Scene(root);
        errorStage.setScene(scene);
        errorStage.showAndWait();
    }
}
