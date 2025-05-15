package projatlab.controller;

import javafx.animation.AnimationTimer;
import projatlab.model.Maze;
import projatlab.model.MazeGenerator;
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
        startAnimation();
    }

    public interface GenerationListener {
            void onGenerationFinished(long generationTime);
        }

        private GenerationListener generationListener;

        public void setGenerationListener(GenerationListener listener) {
            this.generationListener = listener;
        }

     private void startAnimation() {
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
    public long getGenerationTime() {
        System.out.println(generationTime);
        return generationTime;
    }
}