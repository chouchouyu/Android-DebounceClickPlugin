package com.cm.android;

import android.view.View;
import com.cm.android.doubleclick.java.DebouncedPredictor;

public class Test {
    public void onClick(View v) {
        DebouncedPredictor.maybe(v);
        System.nanoTime();
    }
}
