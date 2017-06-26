package com.cse.csenitd.DbHelper;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by lenovo on 26-06-2017.
 */

public class timelinePost_insert extends AsyncTaskLoader<String> {
    private Context mContext;
    private ArrayList<String> imgst;
    private String video,gif,posttext;
    RequestHandler requestHandler;
    private String url;
    public timelinePost_insert(Context context,String txt, ArrayList<String> strs,String video,String gif,String url) {
        super(context);
        this.mContext=context;
        this.imgst=strs;
        this.video=video;
        this.gif=gif;
        requestHandler=new RequestHandler();
        this.url=url;
        this.posttext=txt;
    }

    @Override
    public String loadInBackground() {
        HashMap<String,String > hs=new HashMap<>();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        hs.put("username","abc");
        hs.put("text",posttext);

            hs.put("img1",imgst.get(0));

        hs.put("video","v");
        hs.put("gif","vff");
        hs.put("datetime",timeStamp);
        String result=requestHandler.sendPostRequest(url,hs);
        return result;
    }
}
