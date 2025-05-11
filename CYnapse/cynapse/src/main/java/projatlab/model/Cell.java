package projatlab.model;

public class Cell {
    int i, j;
    Boolean[] walls = {true, true ,true, true};
    Boolean visited = false;

    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
    }
        
}
