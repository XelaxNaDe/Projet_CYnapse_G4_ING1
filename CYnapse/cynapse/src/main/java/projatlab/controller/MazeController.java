package projatlab.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.animation.AnimationTimer;
import projatlab.algorithms.generation.MazeGenerator;
import projatlab.model.Cell;
import projatlab.model.Maze;
import projatlab.view.MazeView;

public class MazeController {
    private final Maze maze;
    private final MazeView view;
    private final MazeGenerator generator;
    private long generationTime;
    private boolean finished = false;


    public MazeController(Maze maze, MazeView view, MazeGenerator generator) {
        this.maze = maze;
        this.view = view;
        this.generator = generator;
    }

    public interface GenerationListener {
            void onGenerationFinished(long generationTime);
        }

        private GenerationListener generationListener;

        public void setGenerationListener(GenerationListener listener) {
            this.generationListener = listener;
        }

     public void startAnimation() {
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
                        if (!finished) {
                            long endTime = System.currentTimeMillis();
                            generationTime = endTime - startTime;
                            finished = true;
                            System.out.println("Génération terminée en " + generationTime + " ms");

                            if (generationListener != null) {
                                generationListener.onGenerationFinished(generationTime);
                            }
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
        finished = true;

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
        System.out.println(generationTime);
        return generationTime;
    }
}