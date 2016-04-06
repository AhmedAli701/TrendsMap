package com.cookiescode.trendsmap;

/**
 * Created by Ahmed Ali on 29/03/2016.
 */
public class StopWatch {
    private long startTime;
    private long stopTime;

    public void start(){
        startTime = System.currentTimeMillis();
    }

    public void stop(){
        stopTime = System.currentTimeMillis();
    }

    public long getElapsedMillis(){
        return stopTime - startTime;
    }

    public long getElapsedSecs(){
        return (stopTime - startTime) / 1000;
    }

}
