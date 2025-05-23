package projatlab.algorithms.generators;

import projatlab.model.Cell;

public class MazeGenerator {
    public boolean finished = false;

    public void removeWalls(Cell a, Cell b) {
        int x = a.i - b.i;
        int y = a.j - b.j;

        if (x == 1) {
            a.walls[3] = false;
            b.walls[1] = false;
        } else if (x == -1) {
            a.walls[1] = false;
            b.walls[3] = false;
        }

        if (y == 1) {
            a.walls[0] = false;
            b.walls[2] = false;
        } else if (y == -1) {
            a.walls[2] = false;
            b.walls[0] = false;
        }
    }

    public void step(){

    }
    public boolean isFinished() {
        return finished;
    }
}
