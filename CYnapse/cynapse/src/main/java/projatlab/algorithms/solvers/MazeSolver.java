package projatlab.algorithms.solvers;

import java.util.ArrayList;

import projatlab.model.Cell;


public class MazeSolver {

    public boolean finished = false;
    public ArrayList<Cell> finalPath = new ArrayList<>();
    
    public void step(){}
    
    public boolean isFinished(){
        return finished;
    }

    public ArrayList<Cell> getFinalPath() {
        return finalPath;
    }
    
}
