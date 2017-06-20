package com.cse.csenitd.DbHelper;

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


    private static String makeHttprequest(URL url, String edes) {
        try {

            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data= URLEncoder.encode("des","UTF-8")+"="+URLEncoder.encode(edes,"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream in=httpURLConnection.getInputStream();
            in.close();
            httpURLConnection.disconnect();
            return "registration successful";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String insertAchievements(String murl, String mdes) {
        URL url = createUrl(murl);
        Log.d("myApp","equake");
        String result = null;
        result=makeHttprequest(url, mdes);
        return result;
    }
}
