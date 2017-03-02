package com.chirodestiny.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.chirodestiny.android.data.PodcastData;
import com.chirodestiny.android.net.GetDataTask;
import com.chirodestiny.android.net.RequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-09-06.
 */
public class VideoActivity extends Activity {
    public ListView mListView;
    public VideoListAdapter mListAdapter;
    public List<PodcastData> mList = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoscreen);

        new GetDataTask(VideoActivity.this, "videos", new RequestTask.onExecuteListener(){
            @Override
            public void onExecute(JSONArray result)
            {
                if ( result != null){
                    Log.d("Log", "Login Response : " + result.toString());
                    try {
                        for ( int i = 0; i < result.length(); i++ )
                        {
                            JSONObject obj = result.getJSONObject(i);
                            Log.d("Log", "Login Response : " + obj.toString());
                            PodcastData tmp = new PodcastData(obj.getInt("id"), obj.getString("name"), obj.getString("description"), obj.getString("link"), obj.getString("date"));
                            mList.add(tmp);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mListView = (ListView)findViewById(R.id.listView);
                mListAdapter = new VideoListAdapter(VideoActivity.this, mList);
                mListView.setAdapter(mListAdapter);

                mListAdapter.notifyDataSetChanged();
            }
        }).execute();
    }

    public class VideoListAdapter extends BaseAdapter {

        private LayoutInflater mInflater = null;

        List<PodcastData> mList = new ArrayList<PodcastData>();
        TextView dateTxt;
        TextView nameTxt;
        TextView descriptionTxt;
        Context mContext;


        public VideoListAdapter(Context context, List<PodcastData> list){
            this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mList = list;
            mContext = context;
            Log.d("Log", mList.get(0).toString());
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
                v = mInflater.inflate(R.layout.item_video, null);
            }
            LinearLayout linear = (LinearLayout)v.findViewById(R.id.body);
            Log.d("Log", mList.get(pos).toString());
            final PodcastData obj = mList.get(pos);

            dateTxt = (TextView)v.findViewById(R.id.date);
            nameTxt = (TextView)v.findViewById(R.id.podcast_name);
            descriptionTxt = (TextView)v.findViewById(R.id.podcast_description);

            dateTxt.setText(obj.getDate());
            nameTxt.setText(obj.getName());
            descriptionTxt.setText(obj.getDescription());

            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PlayVideo.VideoURL = obj.getLink();
                    Intent myIntent = new Intent(mContext, PlayVideo.class);
                    mContext.startActivity(myIntent);
                }
            });

            return v;
        }
    }

    public void onPopupClk(View view)
    {
        //View menuItemView = findViewById(R.id.menu_setting);
        PopupMenu popupMenu = new PopupMenu(VideoActivity.this, view);
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
                        finish();
                        Intent intent1 = new Intent(getApplicationContext(), PodcastActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.video:
                        break;
                    case  R.id.schedule:
                        if ( SelectMenu.scheduleLink != null ){
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SelectMenu.scheduleLink));
                            startActivity(browserIntent);
                        }
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
