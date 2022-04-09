package org.openjfx.puzzle;

import org.openjfx.benchmark.Memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

public class PuzzleState {
    private final int[][] state;

    public PuzzleState() {
        this.state = generateRandom();
        Memory.add(9 * Integer.SIZE);
    }

    public PuzzleState(PuzzleState other) {
        this.state = new int[3][3];
        for(int i = 0; i < 3; i++)
            System.arraycopy(other.state[i], 0, this.state[i], 0, 3);

        Memory.add(Integer.SIZE);
    }

    int distances(PuzzleState target) {
        var current = List.of(matrixToArray(this.state));
        var goal = List.of(matrixToArray(target.state));

        int distances = 0;
        for(int k = 0; k < 9; k++) {
            int i1 = current.indexOf(k);
            int i2 = goal.indexOf(k);
            distances += abs(i2 - i1);
        }
        return distances;
    }

    int misplaced(PuzzleState target) {
        var current = List.of(matrixToArray(this.state));
        var goal = List.of(matrixToArray(target.state));

        int misplaced = 0;
        for(int k = 0; k < 9; k++) {
            int i1 = current.indexOf(k);
            int i2 = goal.indexOf(k);
            if(i1 != i2) misplaced++;
        }
        return misplaced;
    }

    int inversions(PuzzleState target) {
        var current_array = matrixToArray(this.state);
        var goal_array = matrixToArray(target.state);

        int inversions = 0;

        for (int k1 = 0; k1 < 9; k1++)
            for(int k2 = 0; k2 < 9; k2++) {
                if (k1 == k2) continue;

                int i1 = 0;
                while (i1 < 9 && current_array[i1] != k1) { i1++; }

                int j1 = 0;
                while (j1 < 9 && goal_array[j1] != k1) { j1++; }

                int i2 = 0;
                while (i2 < 9 && current_array[i2] != k2) { i2++; }

                int j2 = 0;
                while (j2 < 9 && goal_array[j2] != k2) { j2++; }

                boolean order_current = (i1 - i2) >= 0;
                boolean order_target = (j1 - j2) >= 0;
                if (order_current != order_target) inversions++;
            }

        return inversions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var other = (PuzzleState) o;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (this.state[i][j] != other.state[i][j]) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(state);
    }


    static class Pos {
        public int i;
        public int j;

        public Pos(int x, int y) {
            this.i = x;
            this.j = y;
        }

        public boolean isValid() {
            if (i < 0 || i >= 3) return false;
            return j >= 0 && j < 3;
        }

        public Pos up() {
            return new Pos(this.i - 1, this.j);
        }

        public Pos down() {
            return new Pos(this.i + 1, this.j);
        }

        public Pos right() {
            return new Pos(this.i, this.j + 1);
        }

        public Pos left() {
            return new Pos(this.i, this.j - 1);
        }
    }

    private Pos getBlankPos() {
        var pos = new Pos(0, 0);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (this.state[i][j] == 0) {
                    pos.i = i;
                    pos.j = j;
                }
        return pos;
    }

    private void swap(Pos first, Pos second) {
        var tmp = this.state[first.i][first.j];
        this.state[first.i][first.j] = this.state[second.i][second.j];
        this.state[second.i][second.j] = tmp;
    }

    public ArrayList<PuzzleState> getNeighbours() {
        var blank = getBlankPos();

        var next_states = new ArrayList<PuzzleState>();

        Pos[] neighbors = {blank.up(), blank.down(), blank.right(), blank.left()};
        for (var pos : neighbors)
            if (pos.isValid()) {
                var new_state = new PuzzleState(this);
                new_state.swap(blank, pos);
                next_states.add(new_state);
            }
        return next_states;
    }

    public Integer[] toIntegerArray() {
        var array = matrixToArray(state);
        return Arrays.stream(array).boxed().toArray( Integer[]::new );
    }

    public void print() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                System.out.print(state[i][j] + " ");
            System.out.println();
        }
        System.out.println("------------");
    }

    private int[][] arrayToMatrix(int[] array) {
        var tmp = new int[3][3];
        for (int i = 0; i < 3; i++)
            System.arraycopy(array, i * 3, tmp[i], 0, 3);
        return tmp;
    }

    private int[] matrixToArray(int[][] matrix) {
        var array = new int[9];
        for (int i = 0; i < 3; i++)
            System.arraycopy(matrix[i], 0, array, i * 3, 3);
        return array;
    }

    private int[][] generateRandom() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8};

        // shuffle array https://stackoverflow.com/a/1520212
        Random rnd = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }

        return arrayToMatrix(array);
    }
}
