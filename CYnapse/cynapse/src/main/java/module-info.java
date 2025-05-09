module projatlab {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens projatlab to javafx.fxml;
    exports projatlab;
}
