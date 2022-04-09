package org.openjfx.puzzle;

public class AStarMisplaced extends AStarAbstract {
    public AStarMisplaced(PuzzleState initial, PuzzleState target) {
        super(initial, target);
    }

    @Override
    public int h(PuzzleState state) {
        return state.misplaced(target);
    }
}
