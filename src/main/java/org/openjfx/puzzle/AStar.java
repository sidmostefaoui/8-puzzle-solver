package org.openjfx.puzzle;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AStar extends AbstractPuzzleSolver {
    private final Heuristic h;

    public AStar(PuzzleState initial, PuzzleState target, Heuristic h) {
        super(initial, target);
        this.h = h;
    }

    public interface Heuristic {
        int score(PuzzleState current, PuzzleState target);
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
            PuzzleState s = queue.poll();
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

    @Override
    public boolean search() {
        HashMap<PuzzleState, Integer> score_map = new HashMap<>();
        FastRandomAccessPriorityQueue open = new FastRandomAccessPriorityQueue(score_map::get);

        open.enqueue(initial);
        score_map.put(initial, h.score(initial, target));

        while (!open.isEmpty()) {
            iterations++;

            PuzzleState state = open.dequeue();
            if (state.equals(target)) return true;

            for(PuzzleState n : state.neighbors()) {
               int n_score_through_current = score_map.get(state) + 1;
               Integer n_score_best = score_map.get(n);

                if (n_score_best == null || n_score_through_current < n_score_best) {
                    previous_map.put(n, state);
                    score_map.put(n, n_score_through_current + h.score(n, target));
                    if (!open.contains(n)) open.enqueue(n);
                }
            }
        }

        return false;
    }
}
