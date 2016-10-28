package com.example.landy.projectbase;

import android.app.Application;

/**
 * Created by landy on 16/9/10.
 */
public class MyApplication extends Application {

    private static MyApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
    }

    public MyApplication() {
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

}
