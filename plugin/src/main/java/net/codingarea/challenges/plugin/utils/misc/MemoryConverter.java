package net.codingarea.challenges.plugin.utils.misc;

public class MemoryConverter {

    public static int getGB(long bytes) {
        double kb = (double) bytes / 1024;
        double mb = kb / 1024;
        double gb = mb / 1024;

        return (int) Math.ceil(gb);
    }

}