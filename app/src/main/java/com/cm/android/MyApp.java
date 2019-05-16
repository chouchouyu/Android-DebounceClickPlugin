package com.cm.android;

import android.app.Application;
import com.cm.android.doubleclick.java.DebouncedPredictor;

/**
 * 创建时间:  2018/03/23 15:43 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DebouncedPredictor.FROZEN_WINDOW_MILLIS = 300;
    }
}
