package org.openjfx.puzzle;

import org.openjfx.benchmark.Memory;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class PuzzleState {
    private int data;

    public PuzzleState() {
        this.data = generate_random();
        Memory.add(Integer.SIZE);
    }

    public PuzzleState(PuzzleState other) {
        this.data = other.data;
        Memory.add(Integer.SIZE);
    }

    public static int distances(PuzzleState current, PuzzleState target) {
        List<Integer> actual = Arrays.stream(int_to_array(current.data)).boxed().collect(Collectors.toList());
        List<Integer> goal = Arrays.stream(int_to_array(target.data)).boxed().collect(Collectors.toList());


        int distances = 0;
        for(int k = 0; k < 9; k++) {
            int i1 = actual.indexOf(k);
            int i2 = goal.indexOf(k);
            distances += abs(i2 - i1);
        }

        return distances;
    }

    public static int misplaced(PuzzleState current, PuzzleState target) {
        List<Integer> actual = Arrays.stream(int_to_array(current.data)).boxed().collect(Collectors.toList());
        List<Integer> goal = Arrays.stream(int_to_array(target.data)).boxed().collect(Collectors.toList());

        int misplaced = 0;

        for(int k = 0; k < 9; k++) {
            int i1 = actual.indexOf(k);
            int i2 = goal.indexOf(k);
            if(i1 != i2) misplaced++;
        }

        return misplaced;
    }



    public static int inversions(PuzzleState current, PuzzleState target) {
        int[] current_array = int_to_array(current.data);
        int[] goal_array = int_to_array(target.data);

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

        PuzzleState other = (PuzzleState) o;
        return this.data == other.data;
    }

    @Override
    public int hashCode() {
        return this.data;
    }


    static class Pos {
        public int index;

        public Pos(int index) {
            this.index = index;
        }

        public boolean is_valid() {
            if(i() < 0 || i() >= 3) return false;
            return j() >= 0;
        }

        public Pos left() {
            return new Pos(i() * 3 + (j() - 1));
        }

        public Pos right() {
            return new Pos(i() * 3 + (j() + 1));
        }

        public Pos down() {
            return new Pos((i() + 1) * 3 + j());
        }
        public Pos up() {
            return new Pos((i() - 1) * 3 + j());
        }

        private int i() {
            return index / 3;
        }

        private int j() {
            return index % 3;
        }

        private static int index_of(int data, int n) {
            assert(n >= 0 && n < 9);

            for(int i = 0; i < 9; i++) {
                int digit = data % 10;
                if(digit == n) return 8 - i;
                data -= digit;
                data /= 10;
            }

            return -1;
        }
    }

    private void swap(Pos first, Pos second) {
        int[] arr = int_to_array(this.data);
        int tmp = arr[first.index];
        arr[first.index] = arr[second.index];
        arr[second.index] = tmp;
        this.data = array_to_int(arr);
    }

    public ArrayList<PuzzleState> neighbors() {
        Pos blank = new Pos(Pos.index_of(data, 0));

        Pos[] neighbors = {blank.up(), blank.down(), blank.right(), blank.left()};
        ArrayList<PuzzleState> valid = new ArrayList<>();

        for (Pos pos : neighbors)
            if (pos.is_valid()) {
                PuzzleState new_state = new PuzzleState(this);
                new_state.swap(blank, pos);
                valid.add(new_state);
            }

        return valid;
    }

    public Integer[] as_integer_array() {
        int[] array = int_to_array(this.data);
        return Arrays.stream(array).boxed().toArray( Integer[]::new );

    }


    private static int array_to_int(int[] array) {
        int data = 0;
        for(int i = 0; i < array.length; i++)
            data += array[i] * (pow(10, 8 - i));
        return data;
    }

    private static int[] int_to_array(int data) {
        int[] array = new int[9];

        for(int i = 0; i < 9; i++) {
            int digit = data % 10;
            array[8 - i] = digit;
            data -= digit;
            data /= 10;
        }

        return array;
    }

    private int generate_random() {
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

        return array_to_int(array);
    }


}
