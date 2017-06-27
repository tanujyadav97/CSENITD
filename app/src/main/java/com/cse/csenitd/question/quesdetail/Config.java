package com.cse.csenitd.question.quesdetail;

import android.graphics.Bitmap;

/**
 * Created by 15121 on 6/24/2017.
 */

public class Config {

    public static String[] times;
    public static String[] votess;
    public static String[] quesids;
    public static String[] anss;
    public static String[] ansbys;
    public static String[] ansbynames;
    public static String[] ansbyrepos;
    public static String[] links;
    public static String[] accepteds;
    public static String[] ansids;
    public static String[] voteds;
    public static Bitmap[] dps;

    public static final String GET_URL = "https://nitd.000webhostapp.com/cse%20nitd/getanswers.php";
    public static final String TAG_TIME = "time";
    public static final String TAG_VOTES = "votes";
    public static final String TAG_QUESID = "quesid";
    public static final String TAG_ANS = "ans";
    public static final String TAG_ANSBY = "ansby";
    public static final String TAG_LINK = "link";
    public static final String TAG_ACCEPTED = "accepted";
    public static final String TAG_ANSID = "ansid";
    public static final String TAG_JSON_ARRAY="result";

    public Config(int i){
        times = new String[i];
        votess = new String[i];
        quesids= new String[i];
        anss= new String[i];
        ansbys= new String[i];
        ansbynames= new String[i];
        ansbyrepos= new String[i];
        links= new String[i];
        accepteds= new String[i];
        ansids= new String[i];
        voteds= new String[i];
        dps= new Bitmap[i];
    }
}