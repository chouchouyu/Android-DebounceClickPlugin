package com.cm.android;

import android.view.View;
import com.cm.android.doubleclick.java.DebouncedPredictor;

public class Test {
    public void onClick(View v) {
        if (!DebouncedPredictor.shouldDoClick(v)) {
            return;
        }
        System.nanoTime();
    }
}
