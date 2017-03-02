package com.chirodestiny.android.net;

import android.content.Context;

import com.chirodestiny.android.Common;

import java.util.TimeZone;


public class GetDataTask extends RequestTask {

    public GetDataTask(Context context,
                       String type,
                       onExecuteListener listener) {

        super(context, listener);

        isAvailableNullResponse = true;
        bShowDlg = false;

        strUrl = Common.SERVER_BASE;
        params.putString("request", type);

        if( type == "podcastscats" ) {

        } else if( type == "podcastssubcats" ) {
            params.putString("catid", String.format("%d", Common.catId) );
        } else if( type == "podcasts" ) {
            params.putString("category", String.format("%d", Common.catId));
        } else if( type == "timezone" ) {
            params.putString("id", Common.userId);
            String tzName = TimeZone.getDefault().getID();
            params.putString("timezone", tzName);
            return;
        }
        params.putString("userid", Common.userId );
    }


}