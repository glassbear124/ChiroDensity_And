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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.chirodestiny.android.data.PodcastCatData;
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
public class PodcastSubCatActivity extends Activity {

    public ListView mListView;
    public PodcastCatListAdapter mListAdapter;
    public TextView textView;
    public List<PodcastCatData> mList = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.podcastscreen);

        textView = (TextView)findViewById(R.id.textView);
        textView.setText("Podcasts Sub Category");

        new GetDataTask(PodcastSubCatActivity.this, "podcastssubcats", new RequestTask.onExecuteListener(){
            @Override
            public void onExecute(JSONArray result)
            {
                if ( result != null){
                    try {
                        for ( int i = 0; i < result.length(); i++ ) {
                            JSONObject obj = result.getJSONObject(i);
                            PodcastCatData tmp = new PodcastCatData(obj.getInt("id"), obj.getString("category_name"), obj.getInt("parent_id"));
                            mList.add(tmp);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mListView = (ListView)findViewById(R.id.listView);
                mListAdapter = new PodcastCatListAdapter(PodcastSubCatActivity.this, mList);
                mListView.setAdapter(mListAdapter);

                mListAdapter.notifyDataSetChanged();
            }
        }).execute();
    }

    protected void onPause(){
        super.onPause();
    }

    public void onPopupClk(View view)
    {
        //View menuItemView = findViewById(R.id.menu_setting);
        PopupMenu popupMenu = new PopupMenu(PodcastSubCatActivity.this, view);
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

    public class PodcastCatListAdapter extends BaseAdapter {

        private LayoutInflater mInflater = null;

        List<PodcastCatData> mList = new ArrayList<PodcastCatData>();
        TextView nameTxt;
        Context mContext;


        public PodcastCatListAdapter(Context context, List<PodcastCatData> list){
            this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mList = list;
            Log.d("Log", mList.get(0).toString());
            mContext = context;
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
                v = mInflater.inflate(R.layout.item_podcast_cat, null);
            }

            final PodcastCatData obj = mList.get(pos);

            nameTxt = (TextView)v.findViewById(R.id.name);
            nameTxt.setText(obj.name);
            v.setTag(obj);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final PodcastCatData obj = (PodcastCatData)view.getTag();
                    Common.catId = obj.id;
                    Intent intent = new Intent(PodcastSubCatActivity.this, PodcastActivity.class);
                    startActivity(intent);
                }
            });

            return v;
        }
    }

}
