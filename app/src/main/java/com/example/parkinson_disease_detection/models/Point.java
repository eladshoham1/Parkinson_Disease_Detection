package com.example.parkinson_disease_detection.models;

public class Point {
    private float x;
    private float y;
    private double time;

    public Point() { }

    public Point(float x, float y, double time) {
        this.x = x;
        this.y = y;
        this.time = time;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
