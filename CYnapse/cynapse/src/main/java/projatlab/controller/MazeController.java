package projatlab.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javafx.animation.AnimationTimer;
import projatlab.algorithms.generation.MazeGenerator;
import projatlab.algorithms.solvers.MazeSolver;
import projatlab.model.Cell;
import projatlab.model.Maze;
import projatlab.view.MazeView;

public class MazeController {

    private final Maze maze;
    private final MazeView view;
    private final Random rand;
    
    private MazeGenerator generator;
    private MazeSolver solver;
    private long generationTime;
    private long solvingTime;

    private GenerationListener generationListener;
    private SolvingListener solvingListener;

    public MazeController(Maze maze, MazeView view, long seed) {
        this.maze = maze;
        this.view = view;
        this.rand = new Random(seed);
    }

    public void generateMaze(MazeGenerator generator){
        this.generator = generator;
        startGenerationAnimation();
    }

    public void setGenerator(MazeGenerator generator){
        this.generator = generator;
    }

    public void solveMaze(MazeSolver solver){
        this.solver = solver;
        startSolvingAnimation();
    }


    public interface GenerationListener {
        void onGenerationFinished(long generationTime);
    }

    
    public void setGenerationListener(GenerationListener listener) {
        this.generationListener = listener;
    }


    public void startGenerationAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            private long startTime = -1;

            @Override
            public void handle(long now) {
                if (startTime == -1) {
                    startTime = System.currentTimeMillis();
                }

                if (!generator.isFinished()) {
                    generator.step();
                    view.draw();
                } else {
                    
                    long endTime = System.currentTimeMillis();
                    generationTime = endTime - startTime;
                    System.out.println("Génération terminée en " + generationTime + " ms");

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

                    if (generationListener != null) {
                        generationListener.onGenerationFinished(generationTime);
                    }
                    view.draw();
                
                    this.stop();
                }
            }
        };
        timer.start();
    }



    public interface SolvingListener {
        void onSolvingFinished(long solvingTime);
    }


    public void setSolvingListener(SolvingListener listener) {
        this.solvingListener = listener;
    }

    public void startSolvingAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            private long startTime = -1;

            @Override
            public void handle(long now) {
                if (startTime == -1) {
                    startTime = System.currentTimeMillis();
                }

                if (!solver.isFinished()) {
                    solver.step();
                    view.draw();
                } else {
                    long endTime = System.currentTimeMillis();
                    solvingTime = endTime - startTime;
                    System.out.println("Résolution terminée en " + solvingTime + " ms");

                    if (solvingListener != null) {
                        solvingListener.onSolvingFinished(solvingTime);
                    }


                    this.stop();
                }
            }
        };
        timer.start();
    }




    public void noAnimation(){
        long startTime = System.currentTimeMillis();
        while (!generator.isFinished()) {
            generator.step();
        }

        long endTime = System.currentTimeMillis();
        generationTime = endTime - startTime;


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


        System.out.println("Génération complète terminée en " + generationTime + " ms");

        if (generationListener != null) {
            generationListener.onGenerationFinished(generationTime);
        }
    }

    public void saveMazeToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            int cols = maze.getCols();
            int rows = maze.getRows();

            //dimensions
            writer.write(cols + " " + rows);
            writer.newLine();

            //cells (i j walls)
            for (Cell cell : maze.getGrid()) {
                StringBuilder walls = new StringBuilder();
                for (boolean wall : cell.walls) {
                    walls.append(wall ? "1" : "0");
                }
                writer.write(cell.i + " " + cell.j + " " + walls);
                writer.newLine();
            }

            System.out.println("Labyrinthe sauvegardé dans : " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
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
}