package com.cse.csenitd.Data;

/**
 * Created by lenovo on 19-06-2017. Mohit yadav
 */

public class Acheivements_DATA{
    private String _des;
    private String _title;
    private String _urlString;
    private String userImag;
    private int    _likes;
    private String _UserName;
    private String Date;
    private String _name;
    private int _rep;


    public Acheivements_DATA(String ttl, String _des, String _urlString, int _likes, String date, String usenm,
                             String usrimg,String nm,int rp) {
        this._des = _des;
        this._urlString = _urlString;
        this._likes = _likes;

        Date = date;
        this._UserName=usenm;
        this.userImag=usrimg;
        this._title=ttl;
        this._name=nm;
        this._rep=rp;
    }
    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_rep() {
        return _rep;
    }

    public void set_rep(int _rep) {
        this._rep = _rep;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
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
