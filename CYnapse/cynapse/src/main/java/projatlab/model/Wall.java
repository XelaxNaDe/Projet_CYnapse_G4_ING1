package projatlab.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Wall {
    private Cell cell1;
    private Cell cell2;
    private boolean active;
    private Line line;

    public Wall(Cell cell1, Cell cell2) {
        this.cell1 = cell1;
        this.cell2 = cell2;
        this.active = true;
        this.line = new Line();
        this.line.setStroke(Color.BLACK);
        this.line.setStrokeWidth(2);
    }

    public void toggle() {
        this.active = !this.active;
        this.line.setStroke(this.active ? Color.BLACK : Color.TRANSPARENT);
    }

    public boolean isActive() {
        return active;
    }

    public Line getLine() { // A method to display the wall
        return line;
    }

    public Cell[] getCells() {
        return new Cell[] { cell1, cell2 }; // Return both cells separated by this wall
    }
}
