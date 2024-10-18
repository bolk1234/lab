package by.Danik.lab.models;

/**
 * Счётчик обращений к основному
 */
public class RequestCounter {
    private int count;

    public synchronized void increment () {
        count++;
    }

    public synchronized int getCount () {
        return count;
    }
}
