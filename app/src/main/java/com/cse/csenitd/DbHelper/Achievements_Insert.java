package com.cse.csenitd.DbHelper;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by lenovo on 20-06-2017.Mohit yadav
 */

public class Achievements_Insert extends AsyncTaskLoader<String> {
    private String Url;
    private String mdes;
    private String btm;
    public Achievements_Insert(Context context,String murl,String params) {
        super(context);
        this.Url=murl;
        this.mdes=params;
        //this.btm=bt;
    }
    @Override
    protected void onStartLoading() {
        Log.d("myApp","onstartloading");
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        Log.d("myApp","loadback");
        String result = null;
        RequestHandler rh=new RequestHandler();
        HashMap<String,String> data = new HashMap<>();
        data.put("des", mdes);
      //  data.put("image",btm);
        result = rh.sendPostRequest(Url,data);
        return result;

    }

}
