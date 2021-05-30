package net.codingarea.challenges.plugin.utils.misc;

public class MemoryConverter {

    static final long kilo = 1024;
    static final long mega = kilo * kilo;
    static final long giga = mega * kilo;
    static final long tera = giga * kilo;

    public static int getGB(long bytes) {
        double kb = (double) bytes / kilo;
        double mb = kb / kilo;
        double gb = mb / kilo;
        return (int) gb;
    }

}