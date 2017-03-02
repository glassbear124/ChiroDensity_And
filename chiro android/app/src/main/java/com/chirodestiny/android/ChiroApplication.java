package com.chirodestiny.android;

import android.app.Application;
import android.content.Context;

/**
 * Created by RYJ on 2016-09-06.
 */
public class ChiroApplication extends Application{

    private static ChiroApplication instance = new ChiroApplication();

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
