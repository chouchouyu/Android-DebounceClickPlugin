package com.github.susan.demo;

import android.app.Application;

/**
 * 创建时间:  2018/03/23 15:43 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new TestJava();
    }
}
