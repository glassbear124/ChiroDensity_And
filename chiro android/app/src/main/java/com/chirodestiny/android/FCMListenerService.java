package com.chirodestiny.android;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by aaa7 on 2016/6/21.
 */
public class FCMListenerService extends FirebaseMessagingService {

    static int msgid = 10000;
    long when;

    @Override
    public void onMessageReceived(RemoteMessage message) {
        final String title = Html.fromHtml( message.getData().get("title") ).toString();
        final String message1 = Html.fromHtml( message.getData().get("message") ).toString();
        final String subtitle = Html.fromHtml( message.getData().get("subtitle") ).toString();
        final String ticketText = Html.fromHtml( message.getData().get("tickerText") ).toString();

        generateNotification(getApplicationContext(), title, message1, subtitle, ticketText);
    }

    private void generateNotification(Context context, String title, String message, String subtitle, String tiker ) {
        int icon = R.drawable.logo;
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        Intent notificationIntent = null;
        {
            notificationIntent = new Intent(context, LoginActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Bundle bundle = new Bundle();

            notificationIntent.putExtras(bundle);
        }

        //Common.NOTIFICATION = Common.NOTIFICATION + 1;
        //SharedHelper.saveLastNotifyID(context, Common.NOTIFICATION);
        msgid++;

        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT );

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setSubText(subtitle)
                .setTicker(tiker)
                .setWhen(when)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .setColor(Color.parseColor("#008BBF"))
                .setFullScreenIntent(intent, true);

        if (bitmap != null) {
            mCompatBuilder.setLargeIcon(bitmap);
        }


        mCompatBuilder.setDefaults(Notification.DEFAULT_SOUND | NotificationCompat.DEFAULT_LIGHTS);

        // Play default notification sound
        notificationManager.notify(msgid, mCompatBuilder.build());
        //mIndicator.notifyDataSetChanged();
    }
}