package com.chirodestiny.android.data;

/**
 * Created by Administrator on 2016-09-06.
 */
public class PodcastData {
    private int id;
    private String name;
    private String description;
    private String link;
    private String date;

    public PodcastData(int _id, String _name, String _description, String _link, String _date){
        id = _id;
        name = _name;
        link = _link;
        date = _date;
        description = _description;
    }

    public int getID(){
        return  id;
    }

    public String getName(){
        return  name;
    }

    public String getDescription(){
        return  description;
    }

    public String getLink(){
        return  link;
    }

    public String getDate(){
        return  date;
    }
}
