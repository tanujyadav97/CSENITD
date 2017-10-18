package com.cse.csenitd.req_class;

/**
 * Created by 15121 on 7/1/2017.
 */

import android.graphics.Bitmap;

public class ListItem {

    private String classid;
    private String title;
    private String desc;
    private String noofpeople;
    private String postedon;
    private String postbyname;
    private String postbyusername;
    private Bitmap dp;
    private String attended;


    public String getclassid() {
        return classid;
    }

    public void setclassid(String time) {
        this.classid = time;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String votes) {
        this.title = votes;
    }

    public String getdesc() {
        return desc;
    }

    public void setdesc(String quesid) {
        this.desc = quesid;
    }

    public void setnoofpeople(String ansid) {
        this.noofpeople = ansid;
    }

    public String getnoofpeople() {
        return noofpeople;
    }

    public void setpostedon(String ansbyname) {
        this.postedon = ansbyname;
    }

    public String getpostedon() {
        return postedon;
    }

    public void setpostbyname(String ansbyrepo) {
        this.postbyname = ansbyrepo;
    }

    public String getpostbyname() {
        return postbyname;
    }

    public void setpostbyusername(String ansbyrepo) {
        this.postbyusername = ansbyrepo;
    }

    public String getpostbyusername() {
        return postbyusername;
    }

    public void setattended(String accepted) {
        this.attended = accepted;
    }

    public String getattended() {
        return attended;
    }

    public Bitmap getdp() {
        return dp;
    }

    public void setdp(Bitmap dp) {
        this.dp = dp;
    }

}

