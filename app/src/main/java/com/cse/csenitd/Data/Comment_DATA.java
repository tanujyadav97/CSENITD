package com.cse.csenitd.Data;

/**
 * Created by lenovo on 30-06-2017.Mohit yadav
 */

public class Comment_DATA {
    String comment;
    String date;
    String commenterImg;

    public Comment_DATA(String comment, String date, String commenterImg, String commenterName) {
        this.comment = comment;
        this.date = date;
        this.commenterImg = commenterImg;
        this.commenterName = commenterName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommenterImg() {
        return commenterImg;
    }

    public void setCommenterImg(String commenterImg) {
        this.commenterImg = commenterImg;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    String commenterName;
}
