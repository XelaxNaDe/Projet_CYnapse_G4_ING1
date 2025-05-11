package projatlab.view;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResolverView {
    public void show() {

        Stage resStage = new Stage();
        
        VBox root = new VBox();
        
        Scene scene = new Scene(root, 250, 150);
        resStage.setScene(scene);
        resStage.show();
    }
    
}
