package com.chirodestiny.android;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.chirodestiny.android.net.GetDataTask;
import com.chirodestiny.android.net.RequestTask;

import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TimeZone;


/**
 * Created by Administrator on 2016-09-04.
 */
public class SplashActivity extends Activity {
    static int msgid = 10000;
    long when;

    private Handler mHandler = new Handler();
    private Runnable mTask = new Runnable()
    {
        @Override
        public void run()
        {

            SharedPreferences p = getApplicationContext().getSharedPreferences("pref", 0);
            Common.userId = p.getString("userID", "0");
            int userId = Integer.parseInt(Common.userId);

            if( userId < 1 ) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {

                Common.status = p.getString("status", "1");
                Intent intent = new Intent(SplashActivity.this, SelectMenu.class);
                startActivity(intent);

                new GetDataTask(SplashActivity.this, "timezone", new RequestTask.onExecuteListener(){
                    @Override
                    public void onExecute(JSONArray result)
                    {
                    }
                }).execute();

            }

            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        mHandler.postDelayed(mTask, 3000);
//        mHandler.postDelayed(mTask, 3);
    }


}
