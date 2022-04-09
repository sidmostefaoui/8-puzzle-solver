package org.openjfx.puzzle;

public class AStarInversions  extends AStarAbstract {

    public AStarInversions(PuzzleState initial, PuzzleState target) {
        super(initial, target);
    }

    @Override
    public int h(PuzzleState state){
        return state.inversions(target);
    }
}
