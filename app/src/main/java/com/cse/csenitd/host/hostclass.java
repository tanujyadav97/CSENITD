package com.cse.csenitd.host;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cse.csenitd.R;
import com.cse.csenitd.openingActivity;
import com.cse.csenitd.req_class.requested_classes;

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

import static com.cse.csenitd.LoginActivity.CONNECTION_TIMEOUT;
import static com.cse.csenitd.LoginActivity.READ_TIMEOUT;

public class hostclass extends AppCompatActivity {

    RecyclerView.Adapter adapter;
    public static RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public int count = 0, numberofclasses = 0;
    ProgressDialog progressDialog;
    Config config;
    static RelativeLayout layout;
    static FloatingActionButton add;
    Button cancel, cdate, ctime;
    static Button post;
    DatePicker datep;
    TimePicker timep;
    LinearLayout date, time;
    static TextView tdate, ttime;
    static EditText title, desc, link, tutor, venue;
    static int id;
    Button getinfo;
    static String titlee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostclass);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressDialog = ProgressDialog.show(hostclass.this, "Fetching Classes", "Please wait...", false, false);

        layout = (RelativeLayout) findViewById(R.id.layout);
        add = (FloatingActionButton) findViewById(R.id.add);
        cancel = (Button) findViewById(R.id.cancel);
        post = (Button) findViewById(R.id.post);
        datep = (DatePicker) findViewById(R.id.datep);
        cdate = (Button) findViewById(R.id.cdate);
        ctime = (Button) findViewById(R.id.ctime);
        timep = (TimePicker) findViewById(R.id.timep);
        date = (LinearLayout) findViewById(R.id.date);
        time = (LinearLayout) findViewById(R.id.time);
        ttime = (TextView) findViewById(R.id.ttime);
        tdate = (TextView) findViewById(R.id.tdate);
        title = (EditText) findViewById(R.id.title);
        desc = (EditText) findViewById(R.id.desc);
        link = (EditText) findViewById(R.id.link);
        tutor = (EditText) findViewById(R.id.tutor);
        venue = (EditText) findViewById(R.id.venue);
        getinfo = (Button) findViewById(R.id.getinfo);

        cdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.setVisibility(View.VISIBLE);
                cancel.setClickable(false);
                post.setClickable(false);
                cdate.setClickable(false);
                ctime.setClickable(false);
            }
        });


        ctime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel.setClickable(false);
                post.setClickable(false);
                time.setVisibility(View.VISIBLE);
                cdate.setClickable(false);
                ctime.setClickable(false);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title.setText("");
                desc.setText("");
                link.setText("");
                tutor.setText("");
                venue.setText("");
                tdate.setText("");
                ttime.setText("");
                getinfo.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                post.setText("Post");
                getSupportActionBar().setTitle("Add Classes");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerView.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                getinfo.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle("Available Classes");
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().equals("") || desc.getText().toString().equals("")) {
                    Toast.makeText(hostclass.this, "Title and Description are Compulsory!", Toast.LENGTH_LONG).show();
                } else {
                    String titlee = title.getText().toString();
                    String descc = desc.getText().toString();
                    String linkk = link.getText().toString();
                    String tutorr = tutor.getText().toString();
                    String venuee = venue.getText().toString();
                    String timee = ttime.getText().toString();
                    String datee = tdate.getText().toString();
                    if (tutorr.equals(""))
                        tutorr = "n/a";
                    if (venuee.equals(""))
                        venuee = "n/a";
                    if (timee.equals(""))
                        timee = "n/a";
                    if (datee.equals(""))
                        datee = "n/a";


                    if (post.getText().equals("Post")) {
                        progressDialog = ProgressDialog.show(hostclass.this, "Adding Class", "Please wait...", false, false);

                        new addclassTask().execute(titlee, descc, linkk, tutorr, venuee, timee, datee);
                    } else if (post.getText().equals("Save")) {
                        progressDialog = ProgressDialog.show(hostclass.this, "Saving Class", "Please wait...", false, false);

                        new saveclassTask().execute(titlee, descc, linkk, tutorr, venuee, timee, datee, id + "");

                    }
                }
            }
        });

        getinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(hostclass.this, "Sending Info", "Please wait...", false, false);

                new getInfoTask().execute(id + "", titlee);
            }
        });

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_host, menu);
        //menuu=menu;

        //menuu.findItem(R.id.post).setVisible(false);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.req) {
            Intent intent = new Intent(this, requested_classes.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (date.getVisibility() == View.VISIBLE) {
            date.setVisibility(View.GONE);
            post.setClickable(true);
            cancel.setClickable(true);
            cdate.setClickable(true);
            ctime.setClickable(true);
            int day = datep.getDayOfMonth();
            String month = month(datep.getMonth());
            int year = datep.getYear();
            String datee = month + " " + day + "," + year;
            tdate.setText(datee);
        } else if (time.getVisibility() == View.VISIBLE) {
            time.setVisibility(View.GONE);
            post.setClickable(true);
            cancel.setClickable(true);
            cdate.setClickable(true);
            ctime.setClickable(true);
            int hour, minute;
            if (Build.VERSION.SDK_INT < 23) {
                hour = timep.getCurrentHour();
                minute = timep.getCurrentMinute();

            } else {
                hour = timep.getHour();
                minute = timep.getMinute();
            }
            if (hour > 12)
                ttime.setText(hour - 12 + ":" + minute + " pm");
            else
                ttime.setText(hour + ":" + minute + " am");
        } else if ((layout.getVisibility() == View.VISIBLE)) {
            recyclerView.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Available Classes");
        } else {
            super.onBackPressed();
        }
    }

    private void getData() {
        class GetData extends AsyncTask<String, String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s.equals("false") || s.equals("exception") || s.equals("unsuccessful"))
                    Toast.makeText(hostclass.this, "Unable to fetch answers.", Toast.LENGTH_LONG).show();
                else {

                    parseJSON(s);
                }
            }

            @Override
            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                URL url;
                HttpURLConnection con;

                ////////////////////////////////////
                // TODO: attempt authentication against a network service.

                try {

                    // Enter URL address where your php file resides
                    url = new URL(Config.GET_URL);

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "exception";
                }
                try {
                    // Setup HttpURLConnection class to send and receive data from php and mysql
                    con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(READ_TIMEOUT);
                    con.setConnectTimeout(CONNECTION_TIMEOUT);
                    con.setRequestMethod("POST");

                    // setDoInput and setDoOutput method depict handling of both send and receive
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    // Append parameters to URL
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("username", openingActivity.ps.getString("username", "n/a"));
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
                    return "exception";
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
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            return "exception";
                        }
                        return result.toString().trim();

                    } else {

                        return ("unsuccessful");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return "exception";
                } finally {
                    con.disconnect();
                }


                /////////////////////////////////////
            }

            @Override
            protected void onCancelled() {
                //  progressDialog1.dismiss();
            }

        }
        GetData gd = new GetData();
        gd.execute();
    }

    public void showData() {
        adapter = new CardAdapter(this.getApplicationContext(), Config.classids, Config.titles, Config.descs, Config.links, Config.tutors, Config.noofpeoples, Config.venues, Config.dates, Config.postbynames, Config.postbyusernames, Config.dps, Config.attendeds);
        recyclerView.setAdapter(adapter);

        // Toast.makeText(questionsActivity.this, ""+adapter.getItemCount()+"", Toast.LENGTH_LONG).show();
    }

    private void parseJSON(String json) {
        String dpurls[] = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("result");

            config = new Config(array.length());
            dpurls = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject j = array.getJSONObject(i);

                Config.classids[i] = getclassid(j);
                Config.titles[i] = gettitle(j);
                Config.descs[i] = getdesc(j);
                Config.links[i] = getlink(j);
                Config.tutors[i] = gettutor(j);
                Config.noofpeoples[i] = getstudattending(j);
                Config.venues[i] = getvenue(j);
                Config.dates[i] = getdate(j);
                Config.postbynames[i] = getpostbyname(j);
                Config.postbyusernames[i] = getusername(j);
                Config.attendeds[i] = getattending(j);
                dpurls[i] = getdpurl(j);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog.dismiss();
        showData();
        getdpofanswers(dpurls);
    }

    public void getdpofanswers(String[] dpurls) {
        Integer i = 0;
        count = 0;
        numberofclasses = dpurls.length;
        for (i = 0; i < dpurls.length; i++) {
            new getansdpTask().execute(dpurls[i], i.toString());
        }
        //start asynctask to get dp in a for loop passing dpurl at one by one
        // if in getansdptask() in false or unsuccessful, make all dp=null and call show data
        // in post exec of all async tasks, put retrieved dp in Config dp array using the index passed as param, increase a counter, and if it becomes equal to size of spurl array then call showData(); in post exec of getansdpTask();
    }


    public class getansdpTask extends AsyncTask<String, String, String> {
        Bitmap image, retrieved = null;
        URL url;

        @Override
        protected String doInBackground(String... params) {

            //int index=params[1];
            try {

                url = new URL(params[0]);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                retrieved = image;
                return "true" + " " + params[1];

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception" + " " + params[1];
                //Toast.makeText(profile1.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                return "exception" + " " + params[1];
                //Toast.makeText(profile1.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();

            }


        }

        @Override
        protected void onPostExecute(String result) {


            int index = (Integer.parseInt((result.split(" "))[1]));
            //Toast.makeText(profile1.this, result, Toast.LENGTH_LONG).show();

            if (result.startsWith("exception")) {

                //   Toast.makeText(hostclass.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();
                retrieved = null;
            }
            if (result.startsWith("true")) {

            }
            Config.dps[index] = retrieved;
            count++;
            if (count == numberofclasses)
                showData();


        }

        @Override
        protected void onCancelled() {

        }
    }

    private String getclassid(JSONObject j) {
        String name = null;
        try {
            name = j.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String gettitle(JSONObject j) {
        String name = null;
        try {
            name = j.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getdesc(JSONObject j) {
        String name = null;
        try {
            name = j.getString("desc");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getlink(JSONObject j) {
        String name = null;
        try {
            name = j.getString("link");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getusername(JSONObject j) {
        String name = null;
        try {
            name = j.getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getstudattending(JSONObject j) {
        String name = null;
        try {
            name = j.getString("studattending");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String gettutor(JSONObject j) {
        String name = null;
        try {
            name = j.getString("tutor");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getdate(JSONObject j) {
        String name = null;
        try {
            name = j.getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getvenue(JSONObject j) {
        String name = null;
        try {
            name = j.getString("venue");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Toast.makeText(questiondetailActivity.this, name, Toast.LENGTH_LONG).show();

        return name;
    }

    private String getpostbyname(JSONObject j) {
        String name = null;
        try {
            name = j.getString("postbyname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getattending(JSONObject j) {
        String name = null;
        try {
            name = j.getString("attending");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getdpurl(JSONObject j) {
        String name = null;
        try {
            name = j.getString("dpurl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    String month(int t) {
        switch (t) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "Aug";
            case 9:
                return "Sept";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }
        return "";

    }

    class addclassTask extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {

                // Enter URL address where your php file resides
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/addclass.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user", openingActivity.ps.getString("username", "n/a"))
                        .appendQueryParameter("title", params[0])
                        .appendQueryParameter("desc", params[1])
                        .appendQueryParameter("link", params[2])
                        .appendQueryParameter("tutor", params[3])
                        .appendQueryParameter("venue", params[4])
                        .appendQueryParameter("date", params[5] + " " + params[6]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            // Toast.makeText(questiondetailActivity.this, result, Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            if (result.equals("false") || result.equals("exception") || result.equals("unsuccessful")) {

                Toast.makeText(hostclass.this, "OOPs! Unable to add class.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(hostclass.this, "Class added successfully", Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Available Classes");
                getData();

            }
        }

        @Override
        protected void onCancelled() {

            progressDialog.dismiss();
        }
    }

    class getInfoTask extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {

                // Enter URL address where your php file resides
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/getattendeinfo.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", openingActivity.ps.getString("username", "n/a"))
                        .appendQueryParameter("id", params[0])
                        .appendQueryParameter("title", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            // Toast.makeText(questiondetailActivity.this, result, Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            if (result.equals("false") || result.equals("exception") || result.equals("unsuccessful")) {

                Toast.makeText(hostclass.this, "OOPs! Unable to Send Info.", Toast.LENGTH_LONG).show();
            } else if (result.equals("mailerror")) {
                Toast.makeText(hostclass.this, "Invalid email-id! Please change if from the profile menu", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(hostclass.this, "Participation info mailed to you.", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {

            progressDialog.dismiss();
        }
    }


    class saveclassTask extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {

                // Enter URL address where your php file resides
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/saveclass.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("title", params[0])
                        .appendQueryParameter("desc", params[1])
                        .appendQueryParameter("link", params[2])
                        .appendQueryParameter("tutor", params[3])
                        .appendQueryParameter("venue", params[4])
                        .appendQueryParameter("date", params[5] + " " + params[6])
                        .appendQueryParameter("id", params[7]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            // Toast.makeText(questiondetailActivity.this, result, Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            if (result.equals("false") || result.equals("exception") || result.equals("unsuccessful")) {

                Toast.makeText(hostclass.this, "OOPs! Unable to save class.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(hostclass.this, "Class saved successfully", Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Available Classes");
                getData();

            }
        }

        @Override
        protected void onCancelled() {

            progressDialog.dismiss();
        }
    }

    public static void onedit(String a, String b, String c, String d, String e, String f, String g) {
        recyclerView.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        //getinfo.setVisibility(View.VISIBLE);
        post.setText("Save");
        title.setText(b);
        desc.setText(c);
        link.setText(d);
        tutor.setText(e);
        venue.setText(g);
        id = Integer.parseInt(a);
        titlee = b;
        if (f.equals("n/a")) {
            ttime.setText("n/a");
            tdate.setText("n/a");
        } else {
            String s[] = f.split(" ");
            ttime.setText(s[0] + " " + s[1]);
            tdate.setText(s[2] + " " + s[3]);
        }
    }


}
