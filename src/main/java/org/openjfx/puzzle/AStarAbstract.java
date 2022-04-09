package org.openjfx.puzzle;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public abstract class AStarAbstract extends AbstractPuzzleSolver {

    public AStarAbstract(PuzzleState initial, PuzzleState target) {
        super(initial, target);
    }


    private static class FastRandomAccessPriorityQueue {
        PriorityQueue<PuzzleState> queue;
        HashMap<PuzzleState, Boolean> cache;

        interface GetScore {
            int score(PuzzleState s);
        }

        private FastRandomAccessPriorityQueue(GetScore g) {
            queue = new PriorityQueue<>(Comparator.comparingInt(g::score));
            cache = new HashMap<>();
        }

        void enqueue(PuzzleState s) {
            queue.add(s);
            cache.put(s, true);
        }

        PuzzleState dequeue() {
            var s = queue.poll();
            cache.remove(s);
            return s;
        }

        boolean contains(PuzzleState s) {
            return cache.get(s) != null;
        }

        boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    public abstract int h(PuzzleState state);

    @Override
    public boolean search() {
        var score_map = new HashMap<PuzzleState, Integer>();
        var open = new FastRandomAccessPriorityQueue(score_map::get);

        open.enqueue(initial);
        score_map.put(initial, h(initial));

        while (!open.isEmpty()) {
            iterations++;

            var state = open.dequeue();
            if (state.equals(target)) return true;

            for(var n : state.getNeighbours()) {
               var n_score_through_current = score_map.get(state) + 1;
               var n_score_best = score_map.get(n);

                if (n_score_best == null || n_score_through_current < n_score_best) {
                    previous_map.put(n, state);
                    score_map.put(n, n_score_through_current + h(n));
                    if (!open.contains(n)) open.enqueue(n);
                }
            }
        }

        return false;
    }
}
