package org.openjfx.benchmark;

public class Timer {
    private long t_start;
    private long t_end;

    public void start() {
        t_start = System.currentTimeMillis();
    }

    public void end() {
        t_end = System.currentTimeMillis();
    }

    public long duration () {
        return t_end - t_start;
    }


    @Override
    public String toString() {
        return duration() + "ms";
    }
}
