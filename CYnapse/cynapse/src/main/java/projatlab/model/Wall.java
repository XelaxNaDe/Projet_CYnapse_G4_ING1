package projatlab.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall {
    private Cell cell1;
    private Cell cell2;
    private boolean active;
    private Rectangle rect;

    public Wall(Cell cell1, Cell cell2) {
        this.cell1 = cell1;
        this.cell2 = cell2;
        this.active = true;
        this.rect = new Rectangle();
        this.rect.setStroke(Color.BLACK);
        this.rect.setStrokeWidth(2);
    }

    public void toggle() {
        this.active = !this.active;
        this.rect.setStroke(this.active ? Color.BLACK : Color.TRANSPARENT);
    }

    public boolean isActive() {
        return active;
    }

    // Getters
    public Rectangle getRect() {
        return rect;
    }

    public Cell[] getCells() {
        return new Cell[] { cell1, cell2 }; // Return both Cells separated by this Wall
    }
}
