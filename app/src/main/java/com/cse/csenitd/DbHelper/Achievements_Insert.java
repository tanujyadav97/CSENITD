package com.cse.csenitd.DbHelper;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

/**
 * Created by lenovo on 20-06-2017.
 */

public class Achievements_Insert extends AsyncTaskLoader<String> {
    private String Url;
    private String mdes;
    public Achievements_Insert(Context context,String murl,String params) {
        super(context);
        this.Url=murl;
        this.mdes=params;
    }
    @Override
    protected void onStartLoading() {
        Log.d("myApp","onstartloading");
        forceLoad();
    }
    @Override
    public String loadInBackground() {
        String result=QueryUtils.insertAchievements(Url,mdes);
        return result;
    }
}
