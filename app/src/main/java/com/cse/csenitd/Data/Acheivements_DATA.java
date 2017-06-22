package com.cse.csenitd.Data;

/**
 * Created by lenovo on 19-06-2017. Mohit yadav
 */

public class Acheivements_DATA{
    private String _des;
    private String _urlString;
    private String userImag;
    private int    _likes;
    private String _UserName;
    private String Date;

    public Acheivements_DATA(String _des, String _urlString, int _likes, String date) {
        this._des = _des;
        this._urlString = _urlString;
        this._likes = _likes;
        Date = date;
    }

    public String getUserImag() {
        return userImag;
    }

    public void setUserImag(String userImag) {
        this.userImag = userImag;
    }
    public String get_UserName() {
        return _UserName;
    }

    public void set_UserName(String _UserName) {
        this._UserName = _UserName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String get_des() {
        return _des;
    }

    public void set_des(String _des) {
        this._des = _des;
    }

    public String get_urlString() {
        return _urlString;
    }

    public void set_urlString(String _urlString) {
        this._urlString = _urlString;
    }

    public int get_likes() {
        return _likes;
    }

    public void set_likes(int _likes) {
        this._likes = _likes;
    }
}
