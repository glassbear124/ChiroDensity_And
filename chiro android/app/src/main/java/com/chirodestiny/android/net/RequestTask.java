package com.chirodestiny.android.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public class RequestTask extends BaseNetTask {
	String strUrl = "";
	Bundle params = new Bundle();
	onExecuteListener listener;
	
	ProgressDialog dlgLoading = null;
	Context context;
	
	boolean isAvailableNullResponse = false;
	
	public RequestTask(Context context, onExecuteListener listener)
	{
		this.context = context;
		this.listener = listener;
		bShowDlg = true;
	}
	
	public RequestTask(Context context, onExecuteListener listener, boolean isShowing)
	{
		this.context = context;
		this.listener = listener;
		bShowDlg = isShowing;
	}
	
	public RequestTask(Context context, String url, Bundle params, onExecuteListener listener)
	{
		this.context = context;
		this.strUrl = url;
		this.params = params;
		this.listener = listener;
		bShowDlg = true;
	}
	
	public RequestTask(Context context, String url, Bundle params, onExecuteListener listener, boolean isShowing)
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
	protected JSONArray doInBackground(Object... arg0) {
		try {
			String response = new HttpWorkClass(strUrl, params).callHttpPost();
			Log.d("Log", "Response : " + response);
			if ( response.equals("") )
				return null;

			JSONArray result = new JSONArray(response);

			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("null")
	protected void onPostExecute(JSONArray result) {
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
	
	void sendResult(JSONArray result) throws JSONException {
		if (listener != null) {
			if (result != null || isAvailableNullResponse) {
				listener.onExecute(result);
			}
		}
	}
	
	public interface onExecuteListener
	{
		void onExecute(JSONArray result) throws JSONException;
	}

}
