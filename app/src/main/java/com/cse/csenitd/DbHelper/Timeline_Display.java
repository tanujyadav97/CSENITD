package com.cse.csenitd.DbHelper;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.cse.csenitd.Data.Timeline_DATA;

import java.util.ArrayList;

/**
 * Created by lenovo on 28-06-2017. Mohit yadav
 */

public class Timeline_Display extends AsyncTaskLoader<ArrayList<Timeline_DATA>> {
    private String mUrl;
    private Context context;

    public Timeline_Display(Context context, String s) {
        super(context);
        this.context = context;
        this.mUrl = s;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<Timeline_DATA> loadInBackground() {
        ArrayList<Timeline_DATA> timeline_datas = QueryUtils.extractTimeline(mUrl);
        return timeline_datas;
    }
}
