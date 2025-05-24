package projatlab.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import projatlab.algorithms.generators.MazeGenerator;
import projatlab.algorithms.solvers.MazeSolver;
import projatlab.model.Cell;
import projatlab.model.Maze;
import projatlab.view.ErrorView;
import projatlab.view.MazeView;

/**
 * MazeController manages the generation and solving of mazes.
 * It coordinates the interaction between the maze model, the visual display (MazeView),
 * and the generation/solving algorithms. It also handles animation timing, user feedback, and state tracking.
 */
public class MazeController {
    
    /** The maze being generated or solved. */
    private final Maze maze;
    
    /** The MazeView associated with the maze, used for graphical rendering. */
    public final MazeView view;
    
    /** Random number generator used for maze generation (start/end). */
    private final Random rand;

    
    /** The current maze generation algorithm instance. */
    private MazeGenerator generator;
    
    /** The current maze solving algorithm instance. */
    private MazeSolver solver;

    /** Time taken for the last maze generation (in milliseconds). */
    private long generationTime;

    /** Time taken for the last maze solving (in milliseconds). */
    private long solvingTime;
    

    /** Listener to notify when maze generation is complete. */
    private GenerationListener generationListener;

    /** Listener to notify when maze solving is complete. */
    private SolvingListener solvingListener;
    

    /** Indicates whether the maze is currently being generated. */
    private boolean isGenerating = false;

    /** Indicates whether the maze is currently being solved. */
    private boolean isSolving = false;
    
    /** List of all visited cells after solving, used to toggle visibility. */
    private List<Cell> visitedCells;

    
    // Référence au ResolverController pour notifier l'état de génération
    private projatlab.controller.ResolverController resolverController;


    /**
     * Constructs a MazeController to manage the given maze and its view.
     *
     * @param maze The maze to manage.
     * @param view The graphical view of the maze.
     * @param seed The random seed for reproducibility.
     */
    public MazeController(Maze maze, MazeView view, long seed) {
        this.maze = maze;
        this.view = view;
        this.rand = new Random(seed);
    }

    /**
     * Sets the maze generation algorithm to use.
     *
     * @param generator The MazeGenerator implementation.
     */
    public void setGenerator(MazeGenerator generator){
        this.generator = generator;
    }

    /**
     * Sets the maze solving algorithm to use.
     *
     * @param solver The MazeSolver implementation.
     */
    public void setSolver(MazeSolver solver){
        this.solver = solver;
    }

    /** Listener interface for maze generation completion. */
    public interface GenerationListener {
        /**
         * Called when generation is finished.
         * 
         * @param generationTime duration of generation
         */
        void onGenerationFinished(long generationTime);
    }

    /**
     * Sets the listener to be called upon generation completion.
     *
     * @param listener The listener instance.
     */
    public void setGenerationListener(GenerationListener listener) {
        this.generationListener = listener;
    }

    /**
     * Associates this controller with a ResolverController for coordination.
     *
     * @param resolverController The ResolverController instance.
     */
    public void setResolverController(projatlab.controller.ResolverController resolverController) {
        this.resolverController = resolverController;
    }

    /**
     * Starts animated maze generation with a delay between each step.
     *
     * @param delayMillis Delay in milliseconds between steps.
     */

    public void startGenerationAnimation(double delayMillis) {
        if (isGenerating) return;
        setGeneratingState(true);
        
        Timeline timeline = new Timeline();
        long startTime = System.currentTimeMillis(); 

        int totalCells = maze.getRows() * maze.getCols();

        // Calcul dynamique du "draw step interval"
        int drawStepInterval = Math.max(1, totalCells / 1000);

        final int[] stepCounter = {0};

        KeyFrame keyFrame = new KeyFrame(Duration.millis(delayMillis), event -> {
            if (!generator.isFinished()) {
                generator.step();
                stepCounter[0]++;
                if (stepCounter[0] % drawStepInterval == 0) {
                    view.draw();
                }
            } else {
                finishGeneration();
                long endTime = System.currentTimeMillis();
                generationTime = endTime - startTime;
                

                view.draw(); // dessin final

                if (generationListener != null) {
                    generationListener.onGenerationFinished(generationTime);
                }
                setGeneratingState(false);
                timeline.stop();
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    /**
     * Performs maze generation instantly (without animation).
     */
    public void noGenerationAnimation(){
        if (isGenerating) return;
        setGeneratingState(true);
        
        long startTime = System.currentTimeMillis();
        while (!generator.isFinished()) {
            generator.step();
        }

        long endTime = System.currentTimeMillis();
        generationTime = endTime - startTime;

        finishGeneration();

        if (generationListener != null) {
            generationListener.onGenerationFinished(generationTime);
        }
        
        setGeneratingState(false);
    }

    /**
     * Finalizes the maze generation process, including start/end placement.
     */
    private void finishGeneration() {
        
        if (!generator.isPerfect){
            generator.introduceImperfections(maze.getGrid().size() / 10, maze.getGrid().size() / 20);
        }

        for (Cell cell : maze.getGrid()) {
            cell.setVisited(false);
        }

        int gridSize = maze.getGrid().size();
        Cell startCell;
        Cell endCell;

        do{
            startCell = maze.getCell((rand.nextInt(gridSize)));
            endCell = maze.getCell((rand.nextInt(gridSize)));
        } while(startCell == endCell);

        maze.setStart(startCell);
        maze.setEnd(endCell);

        startCell.setStart(true);
        endCell.setEnd(true);

        view.draw(); 
    }

    /** Listener interface for maze solving completion. */
    public interface SolvingListener {
        /**
         * Called when the resolution is finished
         * 
         * @param solvingTime duration of the resolution.
         */
        void onSolvingFinished(long solvingTime);
    }

    /**
     * Sets the listener to be called upon solving completion.
     *
     * @param listener The listener instance.
     */
    public void setSolvingListener(SolvingListener listener) {
        this.solvingListener = listener;
    }

    /**
     * Starts animated maze solving with visual updates.
     *
     * @param delayMs Delay in milliseconds between each step.
     */
    public void startSolvingAnimation(double delayMs) {
    if (isSolving) return;
    setSolvingState(true);

    Timeline timeline = new Timeline();
    long startTime = System.currentTimeMillis();

    int totalCells = maze.getRows() * maze.getCols();
    int drawStepInterval = Math.max(1, totalCells / 1000);
    final int[] stepCounter = {0};

    KeyFrame keyFrame = new KeyFrame(Duration.millis(delayMs), event -> {

        if (!solver.isFinished()) {
            solver.step();
            stepCounter[0]++;
            if (stepCounter[0] % drawStepInterval == 0) {
                view.draw();
            }
        } else {
            long endTime = System.currentTimeMillis();
            solvingTime = endTime - startTime;

            view.draw(); // dessin final
            visitedCells = getVisitedCells();
            view.setShowVisited(true);


            if (solvingListener != null) {
                solvingListener.onSolvingFinished(solvingTime);
            }
            setSolvingState(false);
            timeline.stop();
        }
    });

    timeline.getKeyFrames().add(keyFrame);
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
}

    /**
     * Performs maze solving instantly (without animation).
     */
    public void noSolvingAnimation() {
        if (isSolving) return;
        setSolvingState(true);
        
        long startTime = System.currentTimeMillis();

        while (!solver.isFinished()) {
            solver.step();
        }

        long endTime = System.currentTimeMillis();
        solvingTime = endTime - startTime;

        view.draw();
        visitedCells = getVisitedCells();
        view.setShowVisited(true);

        if (solvingListener != null) {
            solvingListener.onSolvingFinished(solvingTime);
        }
        
        setSolvingState(false);
    }
    
    /**
     * Returns the list of all visited cells after solving.
     *
     * @return List of visited cells.
     */
    public List<Cell> getVisitedCells() {
        List<Cell> visited = new ArrayList<>();
        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++) {
                Cell cell = maze.getCell(maze.index(col,row));
                if (cell.visited) {
                    visited.add(cell);
                }
            }
        }
        return visited;
    }

    /**
     * Toggles the display of visited cells in the maze view.
     *
     * @param showVisited Current state of visibility.
     * @return New state of visibility.
     */
    public boolean handleToggleVisited(boolean showVisited){
        if (visitedCells == null || visitedCells.isEmpty()) {
            ErrorView.showError("Vous n'avez pas encore résolu le labyrinthe");
            return showVisited;
        }
        try {
            return view.toggleVisited(visitedCells);
        } catch (Exception e) {
            ErrorView.showError("Vous n'avez pas encore résolu le labyrinthe");
            return showVisited;
        }
    }

    /** Redraws the entire maze view. */
    public void drawAll() {
        view.draw();
    }

    /**
     * Returns the duration of the last maze generation.
     *
     * @return Generation time in milliseconds.
     */
    public long getGenerationTime() {
        return generationTime;
    }

    /**
     * Returns the duration of the last maze solving.
     *
     * @return Solving time in milliseconds.
     */
    public long getSovingTime() {
        return solvingTime;
    }

    /**
     * Updates internal and external state to reflect generation activity.
     *
     * @param generating Whether generation is in progress.
     */
    private void setGeneratingState(boolean generating) {
        this.isGenerating = generating;
        if (resolverController != null) {
            resolverController.setGenerationInProgress(generating);
        }
    }

    /**
     * Updates internal state to reflect solving activity.
     *
     * @param solving Whether solving is in progress.
     */
    private void setSolvingState(boolean solving) {
        this.isSolving = solving;
    }

    /**
     * Returns whether the maze is currently being generated.
     *
     * @return {@code true} if generating, else {@code false}.
     */
    public boolean isGenerating() {
        return isGenerating;
    }

    /**
     * Returns whether the maze is currently being solved.
     *
     * @return {@code true} if solving, else {@code false}.
     */
    public boolean isSolving() {
        return isSolving;
    }
}
