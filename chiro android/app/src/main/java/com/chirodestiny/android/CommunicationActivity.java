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

import com.chirodestiny.android.data.CommunicationData;
import com.chirodestiny.android.net.GetDataTask;
import com.chirodestiny.android.net.RequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-09-05.
 */
public class CommunicationActivity extends Activity {
    public  ListView mListView;
    public CommunicationListAdapter mListAdapter;
    public List<CommunicationData> mList = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communications);

        new GetDataTask(CommunicationActivity.this, "communication", new RequestTask.onExecuteListener(){
            @Override
            public void onExecute(JSONArray result)
            {
                if ( result != null){
                    Log.d("Log", "Login Response : " + result.toString());
                    try {
                        for ( int i = 0; i < result.length(); i++ )
                        {
                            JSONObject obj = result.getJSONObject(i);
                            CommunicationData tmp = new CommunicationData(obj.getInt("id"), obj.getString("message"), obj.getString("date"));
                            mList.add(tmp);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mListView = (ListView)findViewById(R.id.listView);
                mListAdapter = new CommunicationListAdapter(CommunicationActivity.this, mList);
                mListView.setAdapter(mListAdapter);

                mListAdapter.notifyDataSetChanged();
            }
        }).execute();
    }

    public class CommunicationListAdapter extends BaseAdapter {

        private LayoutInflater mInflater = null;

        List<CommunicationData> mList = new ArrayList<CommunicationData>();
        TextView dateTxt;
        TextView msgTxt;

        public CommunicationListAdapter(Context context, List<CommunicationData> list){
            this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mList = list;
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
                v = mInflater.inflate(R.layout.item_communication, null);
            }
            Log.d("Log", mList.get(pos).toString());
            CommunicationData obj = mList.get(pos);
            dateTxt = (TextView)v.findViewById(R.id.date);
            msgTxt = (TextView)v.findViewById(R.id.message);

            dateTxt.setText(obj.getDate());
            msgTxt.setText(obj.getMessage());
            return v;
        }
    }

    public void onPopupClk(View view)
    {
        //View menuItemView = findViewById(R.id.menu_setting);
        PopupMenu popupMenu = new PopupMenu(CommunicationActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            public boolean onMenuItemClick(MenuItem item){
                switch (item.getItemId()){
                    case R.id.communicate:
                        break;
                    case R.id.podcast:
                        finish();
                        Intent intent1 = new Intent(getApplicationContext(), PodcastActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.video:
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
