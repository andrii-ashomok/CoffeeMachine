package com.machine.coffee.watcher;

/**
 * Created by rado on 06.02.2016.
 */
public class Watcher {

    private long startTime;
    private long stopTime;
    private long interval;

    public Watcher() {
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        stopTime = System.currentTimeMillis();

        if (startTime < stopTime)
            interval = stopTime - startTime;
    }

    public long getInterval() {
        return interval;
    }

}
