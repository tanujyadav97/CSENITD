
package com.cse.csenitd.DbHelper;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.cse.csenitd.Data.Acheivements_DATA;
import com.cse.csenitd.Data.Comment_DATA;
import com.cse.csenitd.Data.Notices_DATA;
import com.cse.csenitd.Data.Timeline_DATA;
import com.cse.csenitd.Data.User_DATA;
import com.cse.csenitd.NoticeBoard.Notices;
import com.cse.csenitd.openingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.cse.csenitd.LoginActivity.CONNECTION_TIMEOUT;
import static com.cse.csenitd.LoginActivity.READ_TIMEOUT;


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
/*
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
        */

        ArrayList<Acheivements_DATA> es=null;
        URL url;
        HttpURLConnection con;
        String result1;
        ////////////////////////////////////

        try {

            // Enter URL address where your php file resides
            url = new URL(s);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return es;
        }
        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            con = (HttpURLConnection)url.openConnection();
            con.setReadTimeout(READ_TIMEOUT);
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            con.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            con.setDoInput(true);
            con.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()

                    .appendQueryParameter("user", openingActivity.ps.getString("username","n/a"));
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            con.connect();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return es;
        }

        try {

            int response_code = con.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String json;
                StringBuilder result = new StringBuilder();
                try {
                    while ((json = reader.readLine()) != null) {
                        result.append(json + "\n");
                    }
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                    return es;
                }
               result1=result.toString().trim();

            }else{

                return es;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return es;
        } finally {
            con.disconnect();
        }



        es=result(result1);
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
            String liked=objectInArray.getString("liked");
            String nm=objectInArray.getString("name");
            int rp=objectInArray.getInt("reputation");
            long id= Long.parseLong(tdate);
            Date dateObject = new Date(id);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE, d MMM yyyy \n" +
                    " HH:mm:ss");
            tdate=simpleDateFormat.format(dateObject);
            Acheivements_DATA Obj=new Acheivements_DATA(title,y,z,w,tdate,user,usrimg,nm,rp,liked);
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
    public static ArrayList<Timeline_DATA> extractTimeline(String s)
    {
        URL url ;
        ArrayList<Timeline_DATA> es = null;
        HttpURLConnection con;
        String resultt;
        ////////////////////////////////////
        // TODO: attempt authentication against a network service.

        try {

            // Enter URL address where your php file resides
            url = new URL(s);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return es;
        }
        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            con = (HttpURLConnection)url.openConnection();
            con.setReadTimeout(READ_TIMEOUT);
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            con.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            con.setDoInput(true);
            con.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()

                    .appendQueryParameter("user", openingActivity.ps.getString("username","n/a"));
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            con.connect();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return es;
        }

        try {

            int response_code = con.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String json;
                StringBuilder result = new StringBuilder();
                try {
                    while ((json = reader.readLine()) != null) {
                        result.append(json + "\n");
                    }
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                    return es;
                }
                resultt=result.toString().trim();

            }else{

                return es;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return es;
        } finally {
            con.disconnect();
        }


        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        es= Timelineresult(resultt);
        return es;
    }
    public static ArrayList<Timeline_DATA> Timelineresult(String jsonresponse){
        ArrayList<Timeline_DATA> timeline = new ArrayList<>();

        try {

            JSONObject jsonObject=new JSONObject(jsonresponse);
            JSONArray jsonArray = jsonObject.getJSONArray("timelineresult");
            for (int i = 0, size = jsonArray.length(); i < size; i++)
            {
                JSONObject objectInArray = jsonArray.getJSONObject(i);
                String name= objectInArray.getString("name");
                String text=objectInArray.getString("text");
                String tdate=objectInArray.getString("datetime");
//                long id= Long.parseLong(tdate);
//                Date dateObject = new Date(id);
//                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE, d MMM yyyy \n" +
//                        " HH:mm:ss");
//                tdate=simpleDateFormat.format(dateObject);
                String img1=objectInArray.getString("img1");
                String img2=objectInArray.getString("img2");
                String img3=objectInArray.getString("img3");
                String img4=objectInArray.getString("img4");
                String img5=objectInArray.getString("img5");
                String video=objectInArray.getString("video");
                int likes=objectInArray.getInt("likes");
                String id=objectInArray.getString("post_id");
                String usrimg=objectInArray.getString("userimage");
                Timeline_DATA Obj=new Timeline_DATA(name,tdate,text,img1,img2,img3,img4,img5,video,likes,id,usrimg);
                timeline.add(Obj);
            }



        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Notice JSON results", e);
        }


        return timeline;
    }
    public static ArrayList<Comment_DATA> extractComments(String s,int id)
    {
        URL url = createUrl(s);
        String jsonresponse = null;
        int idp=id;
        Log.d("iddddddddddd",Integer.toString(idp));
        try {
            jsonresponse = makeHttprequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        ArrayList<Comment_DATA> es= Commentresult(jsonresponse,idp);
        return es;
    }
    public static ArrayList<Comment_DATA> Commentresult(String jsonresponse,int pid){
        ArrayList<Comment_DATA> comment = new ArrayList<>();

        try {

            JSONObject jsonObject=new JSONObject(jsonresponse);
            JSONArray jsonArray = jsonObject.getJSONArray("commentresult");
            for (int i = 0, size = jsonArray.length(); i < size; i++)
            {

                JSONObject objectInArray = jsonArray.getJSONObject(i);
                Log.d("idddddddddddddddddddddd",Integer.toString(pid));

              if(objectInArray.getInt("postid")==pid){
                  Log.d("chla","hbbbbbbbbbbbb");
                String text= objectInArray.getString("comment");
                String name=objectInArray.getString("commentername");
                String img=objectInArray.getString("commenterimg");
                String date=objectInArray.getString("date");
              long id= Long.parseLong(date);
               Date dateObject = new Date(id);
               SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE, d MMM yyyy \n" +
                       " HH:mm:ss");
               date=simpleDateFormat.format(dateObject);

               Comment_DATA Obj=new Comment_DATA(text,date,img,name);
                comment.add(Obj);}
           }



        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Notice JSON results", e);
        }


        return comment;
    }

    public static ArrayList<User_DATA> extractUsers(String ur) {
        URL url = createUrl(ur);
        String jsonresponse = null;


        try {
            jsonresponse = makeHttprequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        ArrayList<User_DATA> es= Userresult(jsonresponse);
        return es;
    }

    private static ArrayList<User_DATA> Userresult(String jsonresponse) {
        ArrayList<User_DATA> users = new ArrayList<>();

        try {

            JSONObject jsonObject=new JSONObject(jsonresponse);
            JSONArray jsonArray = jsonObject.getJSONArray("userresult");
            for (int i = 0, size = jsonArray.length(); i < size; i++)
            {

                JSONObject objectInArray = jsonArray.getJSONObject(i);

                    String name= objectInArray.getString("name");
                    String city=objectInArray.getString("username");
                    String img=objectInArray.getString("image");
                    int rep=objectInArray.getInt("reputation");
                     User_DATA Obj=new User_DATA(name,city,img,rep);
                   users.add(Obj);
            }




        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Notice JSON results", e);
        }


        return users;
    }
}
