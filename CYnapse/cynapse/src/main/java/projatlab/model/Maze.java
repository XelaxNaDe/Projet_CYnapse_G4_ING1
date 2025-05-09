package projatlab.model;


public class Maze {
    int width, height;
    Cell[][] grid;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                grid[i][j] = new Cell(i, j);
        
    }
    
}
