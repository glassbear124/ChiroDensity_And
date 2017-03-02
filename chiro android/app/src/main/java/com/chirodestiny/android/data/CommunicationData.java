package com.chirodestiny.android.data;

/**
 * Created by Administrator on 2016-09-05.
 */
public class CommunicationData {
    private int id;
    private String message;
    private String date;

    public CommunicationData(int _id, String _message, String _date){
        id = _id;
        message = _message;
        date = _date;
    }

    public int getID(){
        return  id;
    }

    public String getMessage(){
        return  message;
    }

    public String  getDate(){
        return  date;
    }
}
