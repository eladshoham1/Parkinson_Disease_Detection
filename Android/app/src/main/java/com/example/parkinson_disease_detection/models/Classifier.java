package com.example.parkinson_disease_detection.models;

import android.util.Log;
import android.widget.ImageView;

import java.util.List;

public class Classifier {
    public Classifier(ImageView test_IMG_spiral, List<Point> points) {
        for (int i = 0; i < points.size(); i++) {
            Log.d("check2", "X: " + points.get(i).getX() + " Y: " + points.get(i).getY() + " time: " + points.get(i).getTime());
        }
        double time = points.get(points.size() - 1).getTime() - points.get(0).getTime();
        Log.d("check2", "size: " + points.size());
        Log.d("check2", "time: " + time);
    }
}