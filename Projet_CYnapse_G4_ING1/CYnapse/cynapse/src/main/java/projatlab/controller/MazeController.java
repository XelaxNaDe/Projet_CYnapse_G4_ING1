package projatlab.controller;

import javafx.animation.AnimationTimer;
import projatlab.model.Maze;
import projatlab.model.MazeGenerator;
import projatlab.view.MazeView;

public class MazeController {
    private final Maze maze;
    private final MazeView view;
    private final MazeGenerator generator;

    public MazeController(Maze maze, MazeView view, MazeGenerator generator) {
        this.maze = maze;
        this.view = view;
        this.generator = generator;
        startAnimation();
    }

    private void startAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!generator.isFinished()) {
                    generator.step();
                    view.draw();
                } else {
                    System.out.println("Génération terminée.");
                    this.stop();
                }
            }
        };
        timer.start();
    }
}
