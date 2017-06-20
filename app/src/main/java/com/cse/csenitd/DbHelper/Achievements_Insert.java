package com.cse.csenitd.DbHelper;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by lenovo on 20-06-2017.Mohit yadav
 */

public class Achievements_Insert extends AsyncTaskLoader<String> {
    private String Url;
    private String mdes;
    private Bitmap btm;
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public Achievements_Insert(Context context,String murl,String params,Bitmap bitmap) {
        super(context);
        this.Url=murl;
        this.mdes=params;
        this.btm=bitmap;
    }
    @Override
    protected void onStartLoading() {
        Log.d("myApp","onstartloading");
        forceLoad();
    }
    @Override
    public String loadInBackground() {
        String btstr=getStringImage(btm);
        String result=QueryUtils.insertAchievements(Url,mdes,btstr);
        return result;
    }
}
