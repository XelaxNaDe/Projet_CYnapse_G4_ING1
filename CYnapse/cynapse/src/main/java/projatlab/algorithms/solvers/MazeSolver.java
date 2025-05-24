package projatlab.algorithms.solvers;

import java.util.ArrayList;

import projatlab.model.Cell;
import projatlab.model.Maze;

/** Abstract class for maze solvers 
 *
 */
public class MazeSolver {

    /** The maze to solve. */
    public Maze maze;

    /** Counter to keep track of the number of visited cells. */
    public int visitedCount = 0;

    /** True if a path is found between de start and the end of the maze */
    public boolean pathFound = false;

    /** True if the the solving algorithm has ended */
    public boolean finished = false;

    /** Stores the final path from the start cell to the end cell */
    public ArrayList<Cell> finalPath = new ArrayList<>();
    
    /** Perform a step of the solving algorithm */
    public void step(){}
    
    /** Check if the solving process is complete
     * @return true if the algorithm is finished, false otherwise
     */
    public boolean isFinished(){
        return finished;
    }

    /** Return the list of cells forming the final path from start to the end 
     * @return list of cells in the final solution path
     */
    public ArrayList<Cell> getFinalPath() {
        return finalPath;
    }

    /** Returns the number of cells visited by the algorithm
     * Can be overriden 
     */
    public int getVisitedCount() {
        return 0;
    }

    /** Check if a solution exist
     * @return true if a path between the star cell and the end cell is found 
     */
    public boolean isPathFound() {
        return pathFound;
    }
    
}
