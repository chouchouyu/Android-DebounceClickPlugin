package com.cm.android;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * 创建时间:  2018/03/23 15:43 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MyApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
