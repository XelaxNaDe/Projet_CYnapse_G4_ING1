package projatlab.controller;

import javafx.animation.AnimationTimer;
import projatlab.model.Maze;
import projatlab.view.MazeView;
import projatlab.algorithms.generation.dfs;

public class MazeController {
    private final Maze maze;
    private final MazeView view;
    private final dfs generator;

    public MazeController(Maze maze, MazeView view) {
        this.maze = maze;
        this.view = view;
        this.generator = new dfs(maze.getGrid());
        startAnimation();
    }

    private void startAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                generator.step(maze.getcols(), maze.getrows());
                view.draw();
            }
        };
        timer.start();
    }
}
