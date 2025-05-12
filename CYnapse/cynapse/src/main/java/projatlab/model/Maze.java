package projatlab.model;

import java.util.List;
import java.util.ArrayList;

public class Maze {
    private final int width, height; 
    private final List<List<Wall>> hWalls; 
    private final List<List<Wall>> vWalls; 
    private final Cell[][] grid;


    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width];

        // Wall grids initialization 
        this.hWalls = new ArrayList<>();
        this.vWalls = new ArrayList<>();

        // Cells creation
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }

        // Walls initialization
        for (int i = 0; i < height - 1; i++) {
            List<Wall> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new Wall(grid[i][j], grid[i + 1][j]));  // Walls between rows i and i+1
            }
            hWalls.add(row);
        }

        for (int i = 0; i < height; i++) {
            List<Wall> row = new ArrayList<>();
            for (int j = 0; j < width - 1; j++) {
                row.add(new Wall(grid[i][j], grid[i][j + 1]));  // Walls between columns j and j+1
            }
            vWalls.add(row);
        }

        linkWalls();
    }

    // Link Walls to their Cells
    private void linkWalls() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = grid[i][j];

                // Haut
                if (i > 0) {
                    cell.walls[0] = hWalls.get(i - 1).get(j);
                } else {
                    cell.walls[0] = null;
                }

                // Droite
                if (j < width - 1) {
                    cell.walls[1] = vWalls.get(i).get(j);
                } else {
                    cell.walls[1] = null;
                }

                // Bas
                if (i < height - 1) {
                    cell.walls[2] = hWalls.get(i).get(j);
                } else {
                    cell.walls[2] = null;
                }

                // Gauche
                if (j > 0) {
                    cell.walls[3] = vWalls.get(i).get(j - 1);
                } else {
                    cell.walls[3] = null;
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

    public List<List<Wall>> getHWalls() {
        return hWalls;
    }

    public List<List<Wall>> getVWalls() {
        return vWalls;
    }

    public Cell[][] getGrid() {
        return grid;
    }
}
