package ru.nsu.ccfit.gulyaev.utils;

public class Pair {
    private final double first;

    private final double second;

    public Pair(double first, double second){
        this.first = first;
        this.second = second;
    }

    public double getFirst(){
        return this.first;
    }

    public double getSecond(){
        return this.second;
    }
}
