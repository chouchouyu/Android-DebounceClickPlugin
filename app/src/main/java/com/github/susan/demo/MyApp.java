package com.github.susan.demo;

import android.app.Application;
import com.github.susan.debounceclick.java.DebounceClickHandler;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DebounceClickHandler.FROZEN_WINDOW_MILLIS = 700L;
        new TestJava();
    }
}
