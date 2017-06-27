package com.cse.csenitd.DbHelper;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.cse.csenitd.Data.Notices_DATA;

import java.util.ArrayList;

/**
 * Created by lenovo on 23-06-2017.Mohit yadav
 */

public class Notice_Display extends AsyncTaskLoader<ArrayList<Notices_DATA>> {
    private String mUrl;
    private Context context;
    public Notice_Display(Context context,String s) {
        super(context);
        this.context=context;
        this.mUrl=s;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<Notices_DATA> loadInBackground() {
        ArrayList<Notices_DATA> notices=QueryUtils.extractNotices(mUrl);
        return notices;
    }
}
