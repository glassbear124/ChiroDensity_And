package com.chirodestiny.android.net;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

public abstract class LoginBaseNetTask extends AsyncTask<Object, Long, JSONObject> {
	
	boolean bShowDlg = true;
	
	public static final String TYPE_TICKET 	= "TICKET";
	public static final String TYPE_SPACE 	= "SPACE";
	
	public void showNetworkError(Context context)
	{
		if ( bShowDlg )
			Toast.makeText(context, "Please check network status.", Toast.LENGTH_SHORT).show();
	}
	
	public void showUnknowFormat(Context context)
	{
		if ( bShowDlg )
			Toast.makeText(context, "Unknown Format", Toast.LENGTH_SHORT).show();
	}
	
	public void showResultMsg(Context context, String msg)
	{
		if ( bShowDlg && (msg != null && !msg.equals("")))
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}




}
