package projatlab.algorithms.generators;

import java.util.List;
import java.util.Random;
import java.util.Stack;

import projatlab.model.Cell;
import projatlab.model.Maze;

/**
 * MazeGeneratorDFS is a maze generation algorithm that uses
 * Depth-First Search (DFS) to generate a random maze.
 */
public class MazeGeneratorDFS extends MazeGenerator {

    /** The current cell being processed in the DFS algorithm. */
    public Cell current;

    /** Stack to backtrack during DFS. */
    private final Stack<Cell> stack = new Stack<>();

    /**
     * Constructs a MazeGeneratorDFS instance.
     *
     * @param maze The maze to generate.
     * @param seed The seed for random number generation (for reproducibility).
     * @param isPerfect Whether the maze should be perfect (no cycles).
     */
    public MazeGeneratorDFS(Maze maze, long seed, Boolean isPerfect) {
        this.maze = maze;
        this.isPerfect = isPerfect;
        this.rand = new Random(seed);

        if (!maze.getGrid().isEmpty()) {
            this.current = maze.getGrid().get(0);
        }
    }

    /**
     * Performs a step in the maze generation process using DFS.
     */
    @Override
    public void step() {
        if (current == null || finished) return;

        if (!current.visited) {
            current.visited = true;
        }

        List<Cell> neighbors = getUnvisitedNeighbors(current);

        if (!neighbors.isEmpty()) {
            Cell next = neighbors.get(rand.nextInt(neighbors.size()));
            next.visited = true;
            stack.push(current);
            removeWalls(current, next);
            current = next;
        } else if (!stack.isEmpty()) {
            current = stack.pop(); // Backtrack
        }
    }

    /**
     * Indicates whether the generation is complete.
     * @return true if all cells are visited and stack is empty.
     */
    @Override
    public boolean isFinished() {
        if (stack.isEmpty() && getUnvisitedNeighbors(current).isEmpty()) {
            finished = true;
        }
        return finished;
    }
}
