package com.chirodestiny.android.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginRequestTask extends LoginBaseNetTask {
	String strUrl = "";
	Bundle params = new Bundle();
	onExecuteListener listener;

	ProgressDialog dlgLoading = null;
	Context context;

	boolean isAvailableNullResponse = false;

	public LoginRequestTask(Context context, onExecuteListener listener)
	{
		this.context = context;
		this.listener = listener;
		bShowDlg = true;
	}

	public LoginRequestTask(Context context, onExecuteListener listener, boolean isShowing)
	{
		this.context = context;
		this.listener = listener;
		bShowDlg = isShowing;
	}

	public LoginRequestTask(Context context, String url, Bundle params, onExecuteListener listener)
	{
		this.context = context;
		this.strUrl = url;
		this.params = params;
		this.listener = listener;
		bShowDlg = true;
	}

	public LoginRequestTask(Context context, String url, Bundle params, onExecuteListener listener, boolean isShowing)
	{
		this.context = context;
		this.strUrl = url;
		this.params = params;
		this.listener = listener;
		this.bShowDlg = isShowing;
	}
	
	@Override
	protected void onPreExecute() {
		if ( bShowDlg)
		{
			//dlgLoading = DialogUtils.createProgressDialog("Connecting...", context);
		}
		super.onPreExecute();
	}

	@Override
	protected JSONObject doInBackground(Object... arg0) {
		try {
			String response = new HttpWorkClass(strUrl, params).callHttpPost();
			Log.d("Log", "Response : " + response);
			if ( response.equals("") )
				return null;

			JSONObject result = new JSONObject(response);

			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("null")
	protected void onPostExecute(JSONObject result) {
		if (result != null || isAvailableNullResponse)
			try {
				sendResult(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}
	
	@Override
	protected void onCancelled() {
    	if (dlgLoading != null && dlgLoading.isShowing()){
    		dlgLoading.dismiss();
		}
    }
	
	void sendResult(JSONObject result) throws JSONException {
		if (listener != null) {
			if (result != null || isAvailableNullResponse) {
				listener.onExecute(result);
			}
		}
	}
	
	public interface onExecuteListener
	{
		void onExecute(JSONObject result) throws JSONException;
	}

}
