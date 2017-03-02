package com.chirodestiny.android.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

public class HttpWorkClass {

	private String uri = "";
	private ArrayList<BasicNameValuePair> params = null;
	private boolean htmlFormat = false;
	private StringBuilder sb = new StringBuilder(20000);
	
	public HttpWorkClass() {
		
	}
	public HttpWorkClass(String strUri, Bundle postParams) {
		
		//if (Config.DEBUG)
		//	Log.d("Log1", strUri);
		
		this.uri = strUri;
		
		params = new ArrayList<BasicNameValuePair>();
		
		if ( postParams != null )
		{
			for ( String key : postParams.keySet() )
			{
				//if (Config.DEBUG)
					//Log.d("Log1", key+"=>"+postParams.getString(key));
//				params.add(new BasicNameValuePair(key, postParams.getString(key)));
				try {
					params.add(new BasicNameValuePair(key, URLEncoder.encode(postParams.getString(key), "utf-8")));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void setUri(String strUri) {
		
		this.uri = strUri;
	}
	public void setHtmlFormat(boolean bHtml ) {
		
		this.htmlFormat = bHtml; 
	}
	public void setPostParams(ArrayList<BasicNameValuePair> postParams) {
		
		this.params = postParams;				
	}
	public String callHttpGetWithUrl() {
				
		URL url  = null;
		try {
			url = new URL(uri);
		} catch (MalformedURLException e) {

			e.printStackTrace();
			return "";
		}
		try {
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if (conn != null) {
			
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(10000);
				conn.setDefaultUseCaches(false);
				
				int resCode = 0;
				
				try {
					resCode = conn.getResponseCode();
				}
				catch (IOException e) {
					
					e.printStackTrace();
				}				
				
				if (resCode == HttpURLConnection.HTTP_OK ) {
					
					InputStreamReader is= new InputStreamReader(conn.getInputStream());
					BufferedReader br = new BufferedReader(is);
					String line;
					sb.delete(0, sb.length());
					while((line = br.readLine()) != null) {
						
						if (htmlFormat) sb.append(Html.fromHtml(line) + "\n");
						else  sb.append(line + "\n");
					}
					if (sb.length() > 0)
						sb.deleteCharAt(sb.lastIndexOf("\n"));
				}				
				conn.disconnect();
			}
		} catch (IOException e) {

			e.printStackTrace();
			return "";
		}
		
		return sb.toString();
	}
	public String callHttpGetWithParams() {
		
		URL url  = null;
		try {
			sb.delete(0, sb.length());
			sb.append(uri).append("?");
			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i).getName()).append("=");
				sb.append(params.get(i).getValue()).append("&");
			}
			url = new URL(sb.toString());
			
		} catch (MalformedURLException e) {

			e.printStackTrace();
			return "";
		}
		try {
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if (conn != null) {
			
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(10000);
				conn.setDefaultUseCaches(false);
				int resCode = conn.getResponseCode();
				
				if (resCode == HttpURLConnection.HTTP_OK ) {
					InputStreamReader is= new InputStreamReader(conn.getInputStream());
					BufferedReader br = new BufferedReader(is);
					String line;
					sb.delete(0, sb.length());
					while((line = br.readLine()) != null) {
						
						if (htmlFormat) sb.append(Html.fromHtml(line) + "\n");
						else  sb.append(line + "\n");
					}
					if (sb.length() > 0)
						sb.deleteCharAt(sb.lastIndexOf("\n"));
				}
				conn.disconnect();
			}
		} catch (IOException e) {

			e.printStackTrace();
			return "";
		}
		
		return sb.toString();
	}
	public String callHttpPost() {
		URL url  = null;
		try {
			url = new URL(uri);
		} catch (MalformedURLException e) {

			e.printStackTrace();
			return "";
		}
		try {
			/*(HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if (conn != null) {
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(10000);
				conn.setUseCaches(false);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
				conn.setRequestProperty( "charset", "utf-8");

				sb.delete(0, sb.length());
				for (int i = 0; i < params.size(); i++) {
					sb.append(params.get(i).getName()).append("=");
					sb.append(params.get(i).getValue()).append("&");
				}
				byte[] postData       = sb.toString().getBytes( Charset.forName("UTF-8") );
				int    postDataLength = postData.length;
				conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
				//if ( Config.DEBUG )
					Log.d("Log", uri+"?"+sb.toString());
				OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
				PrintWriter pw = new PrintWriter(ow);
				pw.write(sb.toString());
				pw.flush();
				InputStreamReader is = new InputStreamReader(conn.getInputStream());
				BufferedReader br = new BufferedReader(is);

				String line;
				sb.delete(0, sb.length());
				Log.d("Log", "after : " +br.readLine().toString());
				while((line = br.readLine()) != null) {
					if (htmlFormat) sb.append(Html.fromHtml(line) + "\n");
					else  sb.append(line + "\n");
				}
				if (sb.length() > 0)
					sb.deleteCharAt(sb.lastIndexOf("\n"));
			}
			conn.disconnect();*/

			HttpURLConnection urlConnection = null;

			sb.delete(0, sb.length());
			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i).getName()).append("=");
				sb.append(params.get(i).getValue()).append("&");
			}

			Log.d("Log", uri+"?"+sb.toString());
			url = new URL(uri+"?"+sb.toString());

			urlConnection = (HttpURLConnection) url
					.openConnection();

			InputStreamReader is = new InputStreamReader(urlConnection.getInputStream());
			BufferedReader br = new BufferedReader(is);

			String line="";
			sb.delete(0, sb.length());

			while((line = br.readLine()) != null) {
				if (htmlFormat) sb.append(Html.fromHtml(line) + "\n");
				else  sb.append(line + "\n");
			}
			if (sb.length() > 0)
				sb.deleteCharAt(sb.lastIndexOf("\n"));
			//Log.d("Log", "after : " +sb.toString());
		} catch (IOException e) {

			e.printStackTrace();
			return "";
		}		
		return sb.toString();
	}
}
