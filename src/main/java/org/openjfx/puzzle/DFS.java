package org.openjfx.puzzle;

import java.util.ArrayDeque;

public class DFS extends AbstractPuzzleSolver {

    public DFS(PuzzleState initial, PuzzleState target) {
        super(initial, target);
    }

    @Override
    public boolean search() {
        ArrayDeque<PuzzleState> stack = new ArrayDeque<>();
        stack.push(initial);

        while(!stack.isEmpty()) {
            iterations++;
            PuzzleState state = stack.pop();

            if(discovered.get(state) != null) continue;
            discovered.put(state, true);

            if(state.equals(target)) return true;

            for(PuzzleState n : state.getNeighbours()) {
                previous_map.put(n, state);
                stack.push(n);
            }
        }

        return false;
    }
}
