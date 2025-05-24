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

public class MazeController {

    private final Maze maze;
    public final MazeView view;
    private final Random rand;
    
    private MazeGenerator generator;
    private MazeSolver solver;
    private long generationTime;
    private long solvingTime;

    private GenerationListener generationListener;
    private SolvingListener solvingListener;

    private boolean isGenerating = false;
    private boolean isSolving = false;

    private List<Cell> visitedCells;
    
    // Référence au ResolverController pour notifier l'état de génération
    private projatlab.controller.ResolverController resolverController;

    public MazeController(Maze maze, MazeView view, long seed) {
        this.maze = maze;
        this.view = view;
        this.rand = new Random(seed);
    }

    public void setGenerator(MazeGenerator generator){
        this.generator = generator;
    }

    public void setSolver(MazeSolver solver){
        this.solver = solver;
    }

    public interface GenerationListener {
        void onGenerationFinished(long generationTime);
    }

    public void setGenerationListener(GenerationListener listener) {
        this.generationListener = listener;
    }

    public void setResolverController(projatlab.controller.ResolverController resolverController) {
        this.resolverController = resolverController;
    }

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

    public interface SolvingListener {
        void onSolvingFinished(long solvingTime);
    }

    public void setSolvingListener(SolvingListener listener) {
        this.solvingListener = listener;
    }

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

    public void drawAll() {
        view.draw();
    }

    public long getGenerationTime() {
        return generationTime;
    }

    public long getSovingTime() {
        return solvingTime;
    }

    // Méthodes pour gérer les états
    private void setGeneratingState(boolean generating) {
        this.isGenerating = generating;
        // Notifie le ResolverController de l'état de génération
        if (resolverController != null) {
            resolverController.setGenerationInProgress(generating);
        }
    }

    private void setSolvingState(boolean solving) {
        this.isSolving = solving;
    }

    public boolean isGenerating() {
        return isGenerating;
    }

    public boolean isSolving() {
        return isSolving;
    }
}