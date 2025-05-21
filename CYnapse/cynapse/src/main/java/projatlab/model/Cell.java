package projatlab.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cell {
    public int i, j;
    public boolean[] walls = {true, true, true, true}; // Haut, droite, bas, gauche
    public boolean visited = false;
    public static int cellSize = 20;

    public boolean isStart = false;
    public boolean isEnd = false;
    public boolean isInFinalPath = false;

    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public void show(GraphicsContext gc) {
        double x = i * cellSize;
        double y = j * cellSize;


        if (isStart) {
            gc.setFill(Color.GREEN);
            gc.fillRect(x ,y , cellSize, cellSize);
        } else if (isEnd) {
            gc.setFill(Color.RED);
            gc.fillRect(x ,y ,cellSize, cellSize);
        } else if (visited) {
            gc.setFill(Color.web("FF77FF"));
            gc.fillRect(x , y,cellSize, cellSize);
        }
        if (isInFinalPath) {
            gc.setFill(Color.web("#0099FF"));
            gc.fillRect(x , y,cellSize, cellSize);
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        if (walls[0]) gc.strokeLine(x, y, x + cellSize, y);
        if (walls[1]) gc.strokeLine(x + cellSize, y, x + cellSize, y + cellSize);
        if (walls[2]) gc.strokeLine(x + cellSize, y + cellSize, x, y + cellSize);
        if (walls[3]) gc.strokeLine(x, y + cellSize, x, y);
    }

    
    public Cell copy() {
        Cell copy = new Cell(i, j);
        copy.isStart = this.isStart;
        copy.isEnd = this.isEnd;
        System.arraycopy(this.walls, 0, copy.walls, 0, this.walls.length);
        return copy;
    }
    
    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }


    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    
    public int getSize() {
        return cellSize;
    }
}
