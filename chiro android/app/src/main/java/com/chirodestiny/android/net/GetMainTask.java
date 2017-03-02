package com.chirodestiny.android.net;

import android.content.Context;

import com.chirodestiny.android.Common;

public class GetMainTask extends LoginRequestTask {

    public GetMainTask(Context context,
                       String para,
                       onExecuteListener listener) {

        super(context, listener);

        isAvailableNullResponse = true;
        bShowDlg = false;

        strUrl = Common.SERVER_BASE;
        params.putString("request", para);
        params.putString("status", Common.status );
    }
}