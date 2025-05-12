package projatlab.model;

public class Maze {
    private final int width, height;
    private final Cell[][] grid;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width];

        // Cell Creation
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                grid[i][j] = new Cell(i, j);

        linkWalls();
    }

    // Update the cell's walls from their state on the maze
    private void linkWalls() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell current = grid[i][j];

                if (i > 0) {
                    current.walls[0] = new Wall(grid[i - 1][j], current);
                } else {
                    current.walls[0] = new Wall(current, null); // No cell above if on top edge
                }

                if (j < width - 1) {
                    current.walls[1] = new Wall(current, grid[i][j + 1]);
                } else {
                    current.walls[1] = new Wall(current, null); // No cell to the right if on right edge
                }

                if (i < height - 1) {
                    current.walls[2] = new Wall(current, grid[i + 1][j]);
                } else {
                    current.walls[2] = new Wall(current, null); // No cell below if on bottom edge
                }

                if (j > 0) {
                    current.walls[3] = new Wall(grid[i][j - 1], current);
                } else {
                    current.walls[3] = new Wall(current, null); // No cell to the left if on left edge
                }
            }
        }
    }

    // Getters
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(int i, int j) {
        return grid[i][j];
    }

    public Cell[][] getGrid() {
        return grid;
    }
}
