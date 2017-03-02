package com.chirodestiny.android.net;

import android.content.Context;

import com.chirodestiny.android.Common;

public class LoginTask extends LoginRequestTask {

    public LoginTask(Context context,
                     String email, String password, String token,
                     onExecuteListener listener) {

        super(context, listener);

        isAvailableNullResponse = true;
        bShowDlg = false;

        strUrl = Common.SERVER_BASE;
        params.putString("deviceid", token);
        params.putString("type", "1");
        params.putString("email", email);
        params.putString("password", password);
        params.putString("request", "Login");

    }
}