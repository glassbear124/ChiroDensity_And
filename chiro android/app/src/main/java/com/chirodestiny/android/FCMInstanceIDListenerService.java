package com.chirodestiny.android;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aaa7 on 2016/6/21.
 */
public class FCMInstanceIDListenerService extends FirebaseInstanceIdService {

    String strUrl = "";
    Bundle params = new Bundle();


    private static final String[] TOPICS = {"global"};

    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("logs", "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.


    }
}
