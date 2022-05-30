package org.openjfx.puzzle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.min;
import static org.openjfx.puzzle.PuzzleState.*;

public class Genetic  implements IPuzzleSolver {
    private final PuzzleState initial;
    private final PuzzleState target;
    private final CostFunction f;
    private int iterations = 0;
    private Chromosome solution = null;

    @Override
    public boolean findSolution() {
        return search();
    }

    @Override
    public int iterations() {
        return 0;
    }

    @Override
    public long duration() {
        return 0;
    }

    @Override
    public long memory() {
        return 0;
    }

    static class HyperParams {
     private static final int N_FIT_TO_SELECT = 10;
     private static final int MAX_ITERATIONS = 10000000;
     private static final double MUTATION_RATE = 0.8;
     private static final boolean CROSSOVER_TRUNK_CHROMOSOME = true;
    }

    public Genetic(PuzzleState initial, PuzzleState target, CostFunction f) {
        this.initial = initial;
        this.target = target;
        this.f = f;
    }

    public interface CostFunction {
        int score(PuzzleState current, PuzzleState target);
    }

    static class Chromosome {
        ArrayList<Move> moves;

        Chromosome() {
            moves = new ArrayList<>();
        }

        Chromosome(Move move) {
            this();
            moves.add(move);
        }

        Chromosome(Chromosome other) {
            this();
            this.moves.addAll(other.moves);
        }

        private int length() {
            return moves.size();
        }

       public static ArrayList<Chromosome> crossover(Chromosome first, Chromosome second) {
            int max = min(first.length(), second.length());
            int pivot = ThreadLocalRandom.current().nextInt(0, max);

            Chromosome child_1 = new Chromosome(first);
            for(int i = 0; i < pivot; i++)
                child_1.moves.set(i, second.moves.get(i));

            Chromosome child_2 = new Chromosome(second);
            for(int i = 0; i < pivot; i++)
                child_2.moves.set(i, first.moves.get(i));

            ArrayList<Chromosome> children = new ArrayList<>();
            children.add(child_1);
            children.add(child_2);

            if(HyperParams.CROSSOVER_TRUNK_CHROMOSOME) {
                Chromosome child_3 = new Chromosome();
                for(int i = 0; i < pivot; i++)
                    child_3.moves.add(first.moves.get(i));
                for(int i = pivot; i < max; i++)
                    child_3.moves.add(second.moves.get(i));

                Chromosome child_4 = new Chromosome();
                for(int i = 0; i < pivot; i++)
                    child_4.moves.add(second.moves.get(i));
                for(int i = pivot; i < max; i++)
                    child_4.moves.add(first.moves.get(i));

                children.add(child_3);
                children.add(child_4);
            }

            return children;
        }

    }

    int chromosomeFitness (Chromosome c){
        PuzzleState current = new PuzzleState(initial);
        if(!current.applyMoves(c.moves)) return 10000;
        return f.score(current, target);
    }

    public ArrayList<Chromosome> generateInitialPopulation() {
        ArrayList<Chromosome> population = new ArrayList<>();

        for(Move m : initial.validMoves())
            population.add(new Chromosome(m));

        return population;
    }

    ArrayList<Chromosome> crossover(ArrayList<Chromosome> population) {
        ArrayList<Chromosome> children = new ArrayList<>();
        for(Chromosome c1 : population)
            for(Chromosome c2 : population) {
                if(c1 == c2) continue;
                children.addAll(Chromosome.crossover(c1, c2));
            }
        return children;
    }

    ArrayList<Chromosome> selection(ArrayList<Chromosome> population) {
        PriorityQueue<Chromosome> fitness_queue = new PriorityQueue<>(Comparator.comparingInt(this::chromosomeFitness));
        fitness_queue.addAll(population);

        ArrayList<Chromosome> fit = new ArrayList<>();

        int bound = min(HyperParams.N_FIT_TO_SELECT, fitness_queue.size());
        for(int i = 0; i < bound; i++)
            fit.add(fitness_queue.poll());

        for(Chromosome c : fit)
            if(chromosomeFitness(c) == 0)
                solution = c;

        return fit;
    }


    void mutation(Chromosome c) {
        double random = ThreadLocalRandom.current().nextDouble(0, 1.0);

        if(random <= HyperParams.MUTATION_RATE) {
            int pos = ThreadLocalRandom.current().nextInt(0, c.length());
            Move move = Move.random();
            c.moves.add(pos, move);
        }
    }

    public boolean search() {
        ArrayList<Chromosome> population = generateInitialPopulation();

        while(iterations <= HyperParams.MAX_ITERATIONS && solution == null) {
            // apply genetic operators
            ArrayList<Chromosome> fit = selection(population);
            ArrayList<Chromosome> children = crossover(fit);
            for(Chromosome c : children) mutation(c);

            // update new population
            population.clear();
            population.addAll(fit);
            population.addAll(children);

            iterations++;
        }

        return solution != null;
    }
}
