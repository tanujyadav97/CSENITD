package com.cse.csenitd.question;

/**
 * Created by 15121 on 6/22/2017.
 */
public class Config {

    public static String[] times;
    public static String[] votess;
    public static String[] topics;
    public static String[] quess;
    public static String[] tagss;
    public static String[] usernames;
    public static String[] accepteds;

    public static final String GET_URL = "https://nitd.000webhostapp.com/cse%20nitd/getquestions.php";
    public static final String TAG_TIME = "time";
    public static final String TAG_VOTES = "votes";
    public static final String TAG_TOPIC = "topic";
    public static final String TAG_QUES = "ques";
    public static final String TAG_TAGS = "tags";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_ACCEPTED = "accepted";
    public static final String TAG_JSON_ARRAY="result";

    public Config(int i){
        times = new String[i];
        votess = new String[i];
        topics= new String[i];
        quess= new String[i];
        usernames= new String[i];
        accepteds= new String[i];
        tagss= new String[i];
    }
}