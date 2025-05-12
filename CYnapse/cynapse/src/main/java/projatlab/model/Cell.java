package projatlab.model;

public class Cell {
    public int i, j;
    public Wall[] walls = new Wall[4]; // [top, right, bottom, left]
    public boolean visited = false;

    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
    }
}
