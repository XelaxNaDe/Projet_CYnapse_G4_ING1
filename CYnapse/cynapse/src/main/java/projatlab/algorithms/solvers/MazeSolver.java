package projatlab.algorithms.solvers;

import java.util.ArrayList;

import projatlab.model.Cell;
import projatlab.model.Maze;


public class MazeSolver {

    /** The maze to solve. */
    public Maze maze;

    /** True if a path is found between de start and the end of the maze */
    public boolean pathFound = false;

    public boolean finished = false;
    public ArrayList<Cell> finalPath = new ArrayList<>();
    
    public void step(){}
    
    public boolean isFinished(){
        return finished;
    }

    public ArrayList<Cell> getFinalPath() {
        return finalPath;
    }

    public int getVisitedCount() {
        return 0;
    }
    
}
