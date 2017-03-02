package com.chirodestiny.android.data;

/**
 * Created by Administrator on 2016-09-06.
 */
public class PodcastCatData {
    public int id;
    public String name;
    public int parentId;

    public PodcastCatData(int _id, String _name, int _parentId){
        id = _id;
        name = _name;
        parentId = _parentId;
    }

}
