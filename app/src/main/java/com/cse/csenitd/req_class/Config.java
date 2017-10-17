package com.cse.csenitd.req_class;

import android.graphics.Bitmap;

/**
 * Created by 15121 on 7/1/2017.
 */


public class Config {

    public static String[] classids;
    public static String[] titles;
    public static String[] descs;
    public static String[] noofpeoples;
    public static String[] postedon;
    public static String[] postbynames;
    public static String[] postbyusernames;
    public static Bitmap[] dps;
    public static String[] attendeds;

    public static final String GET_URL = "https://nitd.000webhostapp.com/cse%20nitd/getreqclass.php";


    public Config(int i){
        classids = new String[i];
        titles = new String[i];
        descs= new String[i];
        noofpeoples= new String[i];
        postedon= new String[i];
        postbynames= new String[i];
        postbyusernames= new String[i];
        attendeds= new String[i];
        dps= new Bitmap[i];
    }
}