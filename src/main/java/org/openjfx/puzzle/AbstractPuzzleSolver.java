package org.openjfx.puzzle;

import java.util.ArrayDeque;
import java.util.HashMap;

import org.openjfx.benchmark.Memory;
import org.openjfx.benchmark.Timer;

public abstract class AbstractPuzzleSolver implements IPuzzleSolver {
    protected final PuzzleState initial;
    protected final PuzzleState target;
    protected HashMap<PuzzleState, Boolean> discovered = new HashMap<>();
    protected int iterations = 0;
    protected HashMap<PuzzleState, PuzzleState> previous_map;
    private final Timer t;
    private long memory;

    public AbstractPuzzleSolver(PuzzleState initial, PuzzleState target) {
        this.initial = initial;
        this.target = target;
        this.previous_map = new HashMap<>();
        this.t = new Timer();
    }

    private void clear() {
        discovered.clear();
        iterations = 0;
    }

    @Override
    public boolean findSolution() {
        clear();
        t.start();
        Memory.clear();
        boolean search = search();
        memory = Memory.usage();
        t.end();
        return search;
    }

    @Override
    public int iterations() {
        return iterations;
    }

    @Override
    public long duration() {
        return t.duration();
    }

    @Override
    public long memory() {
        return memory;
    }

    protected ArrayDeque<PuzzleState> path(PuzzleState state) {
        ArrayDeque<PuzzleState> path = new ArrayDeque<>();
        path.addFirst(state);

        while(previous_map.get(state) != null) {
            state = previous_map.get(state);
            path.addFirst(state);
        }

        return path;
    }

    public abstract boolean search();

}

