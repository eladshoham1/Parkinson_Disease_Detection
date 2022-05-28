package com.example.parkinson_disease_detection;

import android.app.Application;

import com.example.parkinson_disease_detection.utils.MySP;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MySP.init(this);
    }
}
