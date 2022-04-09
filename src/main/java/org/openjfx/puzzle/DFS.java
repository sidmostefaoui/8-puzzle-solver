package org.openjfx.puzzle;

import java.util.ArrayDeque;

public class DFS extends AbstractPuzzleSolver {

    public DFS(PuzzleState initial, PuzzleState target) {
        super(initial, target);
    }

    @Override
    public boolean search() {
        var stack = new ArrayDeque<PuzzleState>();
        stack.push(initial);

        while(!stack.isEmpty()) {
            iterations++;
            var state = stack.pop();

            if(discovered.get(state) != null) continue;
            discovered.put(state, true);

            if(state.equals(target)) return true;

            for(var n : state.getNeighbours()) {
                previous_map.put(n, state);
                stack.push(n);
            }
        }

        return false;
    }
}
