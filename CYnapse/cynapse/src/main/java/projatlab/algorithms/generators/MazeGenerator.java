package projatlab.algorithms.generators;

import projatlab.model.Cell;

/** Abstract base class for maze generators */

public class MazeGenerator {

    /** Indicates true if the maze generation is complete */
    public boolean finished = false;

    /** Removes the wall between two adjacent cells a and b
     * @param a the first cell
     * @param b the second cell 
     */
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

    /** Performs one stop of the maze generation algorithm */
    public void step(){

    }

    /** Return true if the maze generation is finished
     * @return true if generation is complete, false otherwise
     */
    public boolean isFinished() {
        return finished;
    }
}
