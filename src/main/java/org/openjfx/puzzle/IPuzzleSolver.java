package org.openjfx.puzzle;

public interface IPuzzleSolver {
    boolean findSolution();
    int iterations();
    long duration();
    long memory();
}
