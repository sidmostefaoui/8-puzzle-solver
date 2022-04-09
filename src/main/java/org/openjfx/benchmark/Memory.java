package org.openjfx.benchmark;

public class Memory
{
    private static long memory;

    public static void add(long size) {
        memory += size;
    }

    public static void clear() {
        memory = 0;
    }

    public static long usage() {
        return memory;
    }
}
