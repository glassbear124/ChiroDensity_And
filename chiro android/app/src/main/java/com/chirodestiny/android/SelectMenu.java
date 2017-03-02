package com.chirodestiny.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chirodestiny.android.net.GetMainTask;
import com.chirodestiny.android.net.LoginRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016-09-05.
 */
public class SelectMenu extends Activity {
    ImageView   communicationBut;
    ImageView   podcastBut;
    ImageView   videoBut;
    ImageView   scheduleBut;
    ImageView   onlineBut;
    ImageView   faceLoginBut, referBtn, memberList;
    ImageView   logoutBut;

    public  static String      scheduleLink = null;
    public  static String      onlineCourseLink = null;
    public  static String      facebookLogin = null;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectmenu);

        new GetMainTask(SelectMenu.this, "main", new LoginRequestTask.onExecuteListener(){

            @Override
            public void onExecute(JSONObject result) throws JSONException {
                if ( result != null )
                {
                    Log.d("Log", "after result : " + result.toString());
                    scheduleLink = result.getString("schedule");
                    onlineCourseLink = result.getString("online_courses");
                    facebookLogin = result.getString("facebook");
                }
            }
        }).execute();

        communicationBut = (ImageView)findViewById(R.id.communication_but);
        podcastBut = (ImageView)findViewById(R.id.podcast_but);
        videoBut = (ImageView)findViewById(R.id.videos_but);
        scheduleBut = (ImageView)findViewById(R.id.schedule);
        onlineBut = (ImageView)findViewById(R.id.online_but);
        faceLoginBut = (ImageView)findViewById(R.id.face_login);
        referBtn = (ImageView)findViewById(R.id.referBtn);
        memberList = (ImageView)findViewById(R.id.memberList);
        logoutBut = (ImageView)findViewById(R.id.logout_but);


        if( Common.status.compareTo("2") == 0 ) {
            referBtn.setVisibility(View.GONE);
            memberList.setVisibility(View.GONE);
            scheduleBut.setVisibility(View.GONE);
        }

        communicationBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectMenu.this, CommunicationActivity.class);
                startActivity(intent);
            }
        });

        podcastBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectMenu.this, PodcastCatActivity.class);
                startActivity(intent);
            }
        });

        videoBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectMenu.this, VideoActivity.class);
                startActivity(intent);
            }
        });

        scheduleBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( scheduleLink != null ){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheduleLink));
                    startActivity(browserIntent);
                }
            }
        });

        onlineBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( onlineCourseLink != null ){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(onlineCourseLink));
                    startActivity(browserIntent);
                }
            }
        });

        faceLoginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( facebookLogin != null ){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookLogin));
                    startActivity(browserIntent);
                }
            }
        });

        referBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = String.format("http://app.chirodestiny.com/appapis/referral.php?userid=%s", Common.userId);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
                startActivity(browserIntent);
            }
        });

        memberList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = String.format("http://app.chirodestiny.com/appapis/members.php?userid=%s", Common.userId);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
                startActivity(browserIntent);
            }
        });

        logoutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            SharedPreferences p = getApplicationContext().getSharedPreferences("pref", 0);
            SharedPreferences.Editor ed = p.edit();
            Common.userId = "0";
                Common.status = "0";
            ed.putString("userID", Common.userId);
                ed.putString("status", Common.status);
            ed.commit();

            Intent intent = new Intent(SelectMenu.this, LoginActivity.class);
            startActivity(intent);
            }
        });
    }
}
