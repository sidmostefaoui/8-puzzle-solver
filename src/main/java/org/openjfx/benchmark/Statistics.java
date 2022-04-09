package org.openjfx.benchmark;

public class Statistics {
    private final String algorithm;
    private final Boolean solution;
    private final Integer iterations;
    private final Long duration;
    private final Long memory;

    public Statistics(String algorithm, Boolean solution, Integer iterations,
                      Long duration, Long memory) {
        this.algorithm = algorithm;
        this.solution = solution;
        this.iterations = iterations;
        this.duration = duration;
        this.memory = memory / 1024;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Boolean getSolution() {
        return solution;
    }

    public Integer getIterations() {
        return iterations;
    }

    public Long getDuration() {
        return duration;
    }

    public Long getMemory() {
        return memory;
    }
}
