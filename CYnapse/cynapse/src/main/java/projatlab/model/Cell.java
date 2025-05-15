package projatlab.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cell {
    public int i, j;
    public boolean[] walls;
    public boolean visited;
    public static int cellSize = 20;

    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
        this.walls = new boolean[]{true, true, true, true}; // haut, droite, bas, gauche
        this.visited = false;
    }

    public void show(GraphicsContext gc, int w) {
        double x = i * w;
        double y = j * w;

        // Couleur du fond si visité
        if (visited) {
            gc.setFill(Color.rgb(200, 0, 100));
            gc.fillRect(x, y, w, w);
        }

        gc.setStroke(Color.WHITE);
        if (walls[0]) gc.strokeLine(x, y, x + w, y);         // haut
        if (walls[1]) gc.strokeLine(x + w, y, x + w, y + w); // droite
        if (walls[2]) gc.strokeLine(x + w, y + w, x, y + w); // bas
        if (walls[3]) gc.strokeLine(x, y + w, x, y);         // gauche
    }

    public int getSize() {
        return cellSize;
    }
}
