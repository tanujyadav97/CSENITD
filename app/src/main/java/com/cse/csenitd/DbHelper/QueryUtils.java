
package com.cse.csenitd.DbHelper;

import android.util.Log;
import com.cse.csenitd.Data.Acheivements_DATA;
import com.cse.csenitd.Data.Notices_DATA;
import com.cse.csenitd.NoticeBoard.Notices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by lenovo on 05-06-2017. Mohit yadav
 */

public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("my query", "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Return a list of {@link Achievements_Display} objects that has been built up from
     * parsing a JSON response.
     */
    public static String makeHttprequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("query try ", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("query catch", "Problem retrieving the Achievement JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<Acheivements_DATA> extractAchievements(String s)
    {

        // Create an empty ArrayList that we can start adding achievements to
        URL url = createUrl(s);
        Log.d("fetch","achievements");
        String jsonresponse = null;

        try {
            jsonresponse = makeHttprequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        ArrayList<Acheivements_DATA> es=result(jsonresponse);
        return es;
    }
    public static ArrayList<Acheivements_DATA> result(String jsonresponc)
    {
        ArrayList<Acheivements_DATA> achievements = new ArrayList<>();

        try {

        JSONObject jsonObject=new JSONObject(jsonresponc);
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        for (int i = 0, size = jsonArray.length(); i < size; i++)
        {
            JSONObject objectInArray = jsonArray.getJSONObject(i);
            String y= objectInArray.getString("des");
            String z=objectInArray.getString("img");
            String tdate=objectInArray.getString("datetime");
            int w=objectInArray.getInt("likes");
            String user=objectInArray.getString("username");
            String usrimg=objectInArray.getString("userimage");
            String title=objectInArray.getString("title");
            String nm=objectInArray.getString("name");
            int rp=objectInArray.getInt("reputation");
            long id= Long.parseLong(tdate);
            Date dateObject = new Date(id);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE, d MMM yyyy \n" +
                    " HH:mm:ss");
            tdate=simpleDateFormat.format(dateObject);
            Acheivements_DATA Obj=new Acheivements_DATA(title,y,z,w,tdate,user,usrimg,nm,rp);
            achievements.add(Obj);
        }



    } catch (JSONException e) {
        // If an error is thrown when executing any of the above statements in the "try" block,
        // catch the exception here, so the app doesn't crash. Print a log message
        // with the message from the exception.
        Log.e("QueryUtils", "Problem parsing the Ach JSON results", e);
    }


        return achievements;

}
public static ArrayList<Notices_DATA> extractNotices(String s)
{
    URL url = createUrl(s);
    Log.d("fetch","Notices");
    String jsonresponse = null;

    try {
        jsonresponse = makeHttprequest(url);
    } catch (IOException e) {
        e.printStackTrace();
    }
    // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
    // is formatted, a JSONException exception object will be thrown.
    // Catch the exception so the app doesn't crash, and print the error message to the logs.
    ArrayList<Notices_DATA> es= Noticeresult(jsonresponse);
    return es;
}

   public static ArrayList<Notices_DATA> Noticeresult(String jsonresponse) {
       ArrayList<Notices_DATA> Notices = new ArrayList<>();

       try {

           JSONObject jsonObject=new JSONObject(jsonresponse);
           JSONArray jsonArray = jsonObject.getJSONArray("noticeresult");
           for (int i = 0, size = jsonArray.length(); i < size; i++)
           {
               JSONObject objectInArray = jsonArray.getJSONObject(i);
               String y= objectInArray.getString("name");
               String z=objectInArray.getString("notice");
               String tdate=objectInArray.getString("datetime");
               long id= Long.parseLong(tdate);
               TimeZone tz = TimeZone.getDefault();

               SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
               sdf.setTimeZone(tz);
               tdate=sdf.format(new Date(id * 1000));;
               Notices_DATA Obj=new Notices_DATA(y,z,tdate);
               Notices.add(Obj);
           }



       } catch (JSONException e) {
           // If an error is thrown when executing any of the above statements in the "try" block,
           // catch the exception here, so the app doesn't crash. Print a log message
           // with the message from the exception.
           Log.e("QueryUtils", "Problem parsing the Notice JSON results", e);
       }


       return Notices;

   }



}
