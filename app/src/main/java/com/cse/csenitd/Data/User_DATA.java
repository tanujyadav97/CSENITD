package com.cse.csenitd.Data;

/**
 * Created by lenovo on 02-07-2017.Mohit yadaav
 */

public class User_DATA {
    String name,username,img;
    int rep;

    public User_DATA(String name, String username, String img, int rep) {
        this.name = name;
        this.username = username;
        this.img = img;
        this.rep = rep;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getRep() {
        return rep;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }
}
