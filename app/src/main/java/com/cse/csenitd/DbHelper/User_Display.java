package com.cse.csenitd.DbHelper;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.cse.csenitd.Data.User_DATA;

import java.util.ArrayList;

/**
 * Created by lenovo on 02-07-2017.Mohit yadaav
 */

public class User_Display extends AsyncTaskLoader {
    private Context context;
    private String url;
    public User_Display(Context context,String ur) {
        super(context);
        this.context=context;
        this.url=ur;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        ArrayList<User_DATA> user_datas=QueryUtils.extractUsers(url);
        return user_datas;
    }
}
