package com.dai.utils;

import android.app.Application;

/**
 * Created by dai on 2017/9/14.
 */

public class UtilApplication extends Application {

    private static UtilApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("this = " + this);
        System.out.println("getApplicationContext() = " + getApplicationContext());
        application = (UtilApplication) getApplicationContext();
        System.out.println("application = " + application);

    }

    public static UtilApplication getInstance() {
        return application;
    }


}
