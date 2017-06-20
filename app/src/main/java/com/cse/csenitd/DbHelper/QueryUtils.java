package com.cse.csenitd.DbHelper;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by lenovo on 20-06-2017.Mohit yadav
 */

public final class QueryUtils {

    public QueryUtils()
    {

    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("myApp", "Error with creating URL ", e);
        }
        return url;
    }


    public static String makeHttprequest(URL url, String edes,String im) {
        RequestHandler rh=new RequestHandler();

        String uploadImage = im;

        HashMap<String,String> data = new HashMap<>();
        data.put("des", edes);

        String result = rh.sendPostRequest(url,data);

        return result;
    }

    public static String insertAchievements(String murl, String mdes,String im) {
        URL url = createUrl(murl);
        Log.d("myApp","equake");
        String result = null;
        result=makeHttprequest(url, mdes,im);
        return result;
    }
}
