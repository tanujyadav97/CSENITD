package com.cse.csenitd.question.quesdetail;

import android.graphics.Bitmap;

/**
 * Created by 15121 on 6/24/2017.
 */
public class ListItem {

    private String quesid;
    private String ansid;
    private String time;
    private String votes;
    private String ans;
    private String ansby;
    private String link;
    private String accepted;
    private String ansbyname;
    private String ansbyrepo;
    private Bitmap dp;
    private String voted;

    public String gettime() {
        return time;
    }

    public void settime(String time) {
        this.time = time;
    }

    public String getvotes() {
        return votes;
    }

    public void setvotes(String votes) {
        this.votes = votes;
    }

    public String getquesid() {
        return quesid;
    }

    public void setquesid(String quesid) {
        this.quesid = quesid;
    }

    public String getans() {
        return ans;
    }

    public void setans(String ans) {
        this.ans = ans;
    }

    public String getansid() {
        return ansid;
    }

    public void setansid(String ansid) {
        this.ansid = ansid;
    }

    public String getansby() {
        return ansby;
    }

    public void setansby(String ansby) {
        this.ansby = ansby;
    }

    public String getansbyname() {
        return ansbyname;
    }

    public void setansbyname(String ansbyname) {
        this.ansbyname = ansbyname;
    }

    public String getansbyrepo() {
        return ansbyrepo;
    }

    public void setansbyrepo(String ansbyrepo) {
        this.ansbyrepo = ansbyrepo;
    }

    public String getaccepted() {
        return accepted;
    }

    public void setaccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getlink() {
        return link;
    }

    public void setlink(String link) {
        this.link = link;
    }

    public Bitmap getdp() {
        return dp;
    }

    public void setdp(Bitmap dp) {
        this.dp = dp;
    }

    public String getvoted() {
        return voted;
    }

    public void setvoted(String voted) {
        this.voted = voted;
    }
}

