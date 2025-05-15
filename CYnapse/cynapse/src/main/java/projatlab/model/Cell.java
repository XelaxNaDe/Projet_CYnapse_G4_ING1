package projatlab.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cell {
    public int i, j;
    public boolean[] walls = {true, true, true, true}; // Haut, droite, bas, gauche
    public boolean visited = false;
    public static final int cellSize = 20;

    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public void show(GraphicsContext gc) {
        double x = i * cellSize;
        double y = j * cellSize;

        if (visited) {
            gc.setFill(Color.rgb(200, 0, 100));
            gc.fillRect(x, y, cellSize, cellSize);
        }

        gc.setStroke(Color.WHITE);
        if (walls[0]) gc.strokeLine(x, y, x + cellSize, y);           // Haut
        if (walls[1]) gc.strokeLine(x + cellSize, y, x + cellSize, y + cellSize); // Droite
        if (walls[2]) gc.strokeLine(x + cellSize, y + cellSize, x, y + cellSize); // Bas
        if (walls[3]) gc.strokeLine(x, y + cellSize, x, y);           // Gauche
    }

    public Cell copy() {
        Cell copy = new Cell(i, j);
        copy.visited = this.visited;
        System.arraycopy(this.walls, 0, copy.walls, 0, this.walls.length);
        return copy;
    }
    
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    
    public int getSize() {
        return cellSize;
    }
}
