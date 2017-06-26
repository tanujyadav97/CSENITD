package com.cse.csenitd.DbHelper;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.cse.csenitd.Data.Acheivements_DATA;

import java.util.ArrayList;

/**
 * Created by lenovo on 22-06-2017.Mohit yadav
 */

public class Achievements_Display extends AsyncTaskLoader<ArrayList<Acheivements_DATA>>{

    private String mUrl;
    private Context context;
    public Achievements_Display(Context context,String s) {
        super(context);
        this.context=context;
        this.mUrl=s;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Acheivements_DATA> loadInBackground() {
        Log.d("asynkloader","loadinback");
        ArrayList<Acheivements_DATA> eathQuak=QueryUtils.extractAchievements(mUrl);
        return eathQuak;
    }
}
