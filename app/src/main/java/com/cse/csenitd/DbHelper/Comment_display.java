package com.cse.csenitd.DbHelper;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.cse.csenitd.Data.Acheivements_DATA;
import com.cse.csenitd.Data.Comment_DATA;

import java.util.ArrayList;

/**
 * Created by lenovo on 30-06-2017.
 */

public class Comment_display extends AsyncTaskLoader<ArrayList<Comment_DATA>> {
    private String mUrl;
    private Context context;
    private int idp;

    public Comment_display(Context context,String s,int id) {
        super(context);
        this.context=context;
        this.mUrl=s;
        this.idp=id;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Comment_DATA> loadInBackground() {
        Log.d("idddddddddddddddddd",Integer.toString(idp));
        Log.d("idddddddddddddddddd",Integer.toString(idp));
        Log.d("idddddddddddddddddd",Integer.toString(idp));
        Log.d("idddddddddddddddddd",Integer.toString(idp));
        Log.d("idddddddddddddddddd",Integer.toString(idp));
        Log.d("idddddddddddddddddd",Integer.toString(idp));
        ArrayList<Comment_DATA> comments=QueryUtils.extractComments(mUrl,idp);
        return comments;
    }
}
