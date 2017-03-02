package com.chirodestiny.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.chirodestiny.android.data.PodcastData;
import com.chirodestiny.android.net.GetDataTask;
import com.chirodestiny.android.net.RequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-09-06.
 */
public class PodcastActivity extends Activity {
    public ListView mListView;
    public PodcastListAdapter mListAdapter;

    TextView textView;

    public List<PodcastData> mList = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.podcastscreen);

        textView = (TextView)findViewById(R.id.textView);
        textView.setText("Podcasts Category");


        new GetDataTask(PodcastActivity.this, "podcasts", new RequestTask.onExecuteListener(){
            @Override
            public void onExecute(JSONArray result)
            {
                if ( result != null){

                    try {
                        for ( int i = 0; i < result.length(); i++ )
                        {
                            JSONObject obj = result.getJSONObject(i);

                            PodcastData tmp = new PodcastData(obj.getInt("id"), obj.getString("name"),
                                    obj.getString("description"), obj.getString("link"), obj.getString("date"));
                            mList.add(tmp);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mListView = (ListView)findViewById(R.id.listView);
                mListAdapter = new PodcastListAdapter(PodcastActivity.this, mList);
                mListView.setAdapter(mListAdapter);

                mListAdapter.notifyDataSetChanged();
            }
        }).execute();
    }

    protected void onPause(){
        super.onPause();
        mListAdapter.mediaPlayer.release();
    }

    public class PodcastListAdapter extends BaseAdapter {

        private LayoutInflater mInflater = null;

        List<PodcastData> mList = new ArrayList<PodcastData>();
        TextView dateTxt;
        TextView nameTxt;
        TextView descriptionTxt, linkTxt;
        ImageView playBut;
        ImageView pauseBut;
        ImageView stopBut;
        Context mContext;

        public MediaPlayer mediaPlayer;
        /**
         * remain false till media is not completed, inside OnCompletionListener make it true.
         */
        private boolean intialStage = true;


        public PodcastListAdapter(Context context, List<PodcastData> list){
            this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mList = list;
            Log.d("Log", mList.get(0).toString());
            mContext = context;

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int pos, View view, ViewGroup viewGroup) {
            View v = view;
            if (v == null) {
                v = mInflater.inflate(R.layout.item_podcast, null);
            }
            Log.d("Log", mList.get(pos).toString());
            final PodcastData obj = mList.get(pos);

            dateTxt = (TextView)v.findViewById(R.id.date);
            nameTxt = (TextView)v.findViewById(R.id.podcast_name);
            descriptionTxt = (TextView)v.findViewById(R.id.podcast_description);
            linkTxt = (TextView)v.findViewById(R.id.podcast_link);

            playBut = (ImageView)v.findViewById(R.id.playPodcastBut);
            pauseBut = (ImageView)v.findViewById(R.id.pausePodcastBut);
            stopBut = (ImageView)v.findViewById(R.id.stopPodcastBut);

            dateTxt.setText(obj.getDate());
            nameTxt.setText(obj.getName());
            descriptionTxt.setText(obj.getDescription());
            linkTxt.setText(obj.getLink());

            playBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                try {
                    if (intialStage)
                        new Player().execute(obj.getLink());
                    else {
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                }
                }
            });

            pauseBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                }
            });

            stopBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if ( mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                }
            });
            return v;
        }

        class Player extends AsyncTask<String, Void, Boolean> {
            private ProgressDialog progress;

            @Override
            protected Boolean doInBackground(String... params) {
                // TODO Auto-generated method stub
                Boolean prepared;
                try {

                    mediaPlayer.setDataSource(params[0]);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            intialStage = true;
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                        }
                    });
                    mediaPlayer.prepare();
                    prepared = true;
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    Log.d("IllegarArgument", e.getMessage());
                    prepared = false;
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    prepared = false;
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    prepared = false;
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    prepared = false;
                    e.printStackTrace();
                }
                return prepared;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (progress.isShowing()) {
                    progress.cancel();
                }
                Log.d("Prepared", "//" + result);
                mediaPlayer.start();

                intialStage = false;
            }

            public Player() {
                progress = new ProgressDialog(mContext);
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                this.progress.setMessage("Buffering...");
                this.progress.show();

            }
        }
    }

    public void onPopupClk(View view)
    {
        //View menuItemView = findViewById(R.id.menu_setting);
        PopupMenu popupMenu = new PopupMenu(PodcastActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            public boolean onMenuItemClick(MenuItem item){
                switch (item.getItemId()){
                    case R.id.communicate:
                        finish();
                        Intent intent = new Intent(getApplicationContext(), CommunicationActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.podcast:
                        break;
                    case R.id.video:
                        finish();
                        Intent intent2 = new Intent(getApplicationContext(), VideoActivity.class);
                        startActivity(intent2);
                        break;
                    case  R.id.online:
                        if ( SelectMenu.onlineCourseLink != null ){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SelectMenu.onlineCourseLink));
                            startActivity(browserIntent);
                        }
                        break;
                    case  R.id.facebook:
                        if ( SelectMenu.facebookLogin != null ){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SelectMenu.facebookLogin));
                            startActivity(browserIntent);
                        }
                        break;
                    case  R.id.logout:
                        finish();
                        Intent intent3 = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
