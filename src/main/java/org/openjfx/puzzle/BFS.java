package org.openjfx.puzzle;

import java.util.ArrayDeque;
import java.util.Queue;

public class BFS extends AbstractPuzzleSolver {

    public BFS(PuzzleState initial, PuzzleState target) {
        super(initial, target);
    }

    @Override
    public boolean search() {
        Queue<PuzzleState> queue = new ArrayDeque<>();
        discovered.put(initial, true);
        queue.add(initial);

        while(!queue.isEmpty()) {
            iterations++;

            PuzzleState state = queue.poll();
            if(state.equals(target)) return true;

            for(PuzzleState n : state.getNeighbours())
                if(discovered.get(n) == null) {
                    discovered.put(n, true);
                    previous_map.put(n, state);
                    queue.add(n);
                }
        }

        return false;
    }
}
