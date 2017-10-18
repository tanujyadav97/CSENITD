package com.cse.csenitd.req_class;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cse.csenitd.R;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.cse.csenitd.LoginActivity.CONNECTION_TIMEOUT;
import static com.cse.csenitd.LoginActivity.READ_TIMEOUT;

public class requested_classes extends AppCompatActivity {

    RecyclerView.Adapter adapter;
    public static RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public int count = 0, numberofclasses = 0;
    ProgressDialog progressDialog;
    Config config;
    static RelativeLayout layout;
    static FloatingActionButton add;
    Button cancel;
    static Button post;
    static EditText title, desc;
    static int id;
    Button getinfo;
    static String titlee;
    TextView postedon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_classes);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressDialog = ProgressDialog.show(com.cse.csenitd.req_class.requested_classes.this, "Fetching Classes", "Please wait...", false, false);

        layout = (RelativeLayout) findViewById(R.id.layout);
        add = (FloatingActionButton) findViewById(R.id.add);
        cancel = (Button) findViewById(R.id.cancel);
        post = (Button) findViewById(R.id.post);
        title = (EditText) findViewById(R.id.title);
        desc = (EditText) findViewById(R.id.desc);
        postedon = (TextView) findViewById(R.id.time);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title.setText("");
                desc.setText("");
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
                getSupportActionBar().setTitle("Available Classes");
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().equals("") || desc.getText().toString().equals("")) {
                    Toast.makeText(com.cse.csenitd.req_class.requested_classes.this, "Title and Description are Compulsory!", Toast.LENGTH_LONG).show();
                } else {
                    String titlee = title.getText().toString();
                    String descc = desc.getText().toString();

                    if (post.getText().equals("Post")) {
                        progressDialog = ProgressDialog.show(com.cse.csenitd.req_class.requested_classes.this, "Adding Class", "Please wait...", false, false);

                        new com.cse.csenitd.req_class.requested_classes.addclassTask().execute(titlee, descc);
                    } else if (post.getText().equals("Save")) {
                        progressDialog = ProgressDialog.show(com.cse.csenitd.req_class.requested_classes.this, "Saving Class", "Please wait...", false, false);

                        new com.cse.csenitd.req_class.requested_classes.saveclassTask().execute(titlee, descc, id + "");

                    }
                }
            }
        });


        getData();
    }


    @Override
    public void onBackPressed() {
        if ((layout.getVisibility() == View.VISIBLE)) {
            recyclerView.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Requested Classes");
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
                    Toast.makeText(com.cse.csenitd.req_class.requested_classes.this, "Unable to fetch answers.", Toast.LENGTH_LONG).show();
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
        adapter = new CardAdapter(this.getApplicationContext(), Config.classids, Config.titles, Config.descs, Config.noofpeoples, Config.postbynames, Config.postbyusernames, Config.postedon, Config.dps, Config.attendeds);
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
                Config.noofpeoples[i] = getstudattending(j);
                Config.postbynames[i] = getpostbyname(j);
                Config.postbyusernames[i] = getusername(j);
                Config.attendeds[i] = getattending(j);
                Config.postedon[i] = getpostedon(j);
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
            new com.cse.csenitd.req_class.requested_classes.getansdpTask().execute(dpurls[i], i.toString());
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

                //   Toast.makeText(com.cse.csenitd.req_class.requested_classes.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();
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
            name = j.getString("requesting");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }


    private String getpostedon(JSONObject j) {
        String name = null;
        try {
            name = j.getString("postedon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Toast.makeText(questiondetailActivity.this, name, Toast.LENGTH_LONG).show();

        return getDate(Long.parseLong(name));
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
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/addreqclass.php");

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
                        .appendQueryParameter("desc", params[1]);
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

                Toast.makeText(com.cse.csenitd.req_class.requested_classes.this, "OOPs! Unable to add class.", Toast.LENGTH_LONG).show();
            } else {
                //     Toast.makeText(com.cse.csenitd.req_class.requested_classes.this, "Class added successfully", Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Requested Classes");
                getData();

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
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/savereqclass.php");

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
                        .appendQueryParameter("id", params[2]);
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

                Toast.makeText(com.cse.csenitd.req_class.requested_classes.this, "OOPs! Unable to save class.", Toast.LENGTH_LONG).show();
            } else {
                //    Toast.makeText(com.cse.csenitd.req_class.requested_classes.this, "Class saved successfully", Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Requested Classes");
                getData();

            }
        }

        @Override
        protected void onCancelled() {

            progressDialog.dismiss();
        }
    }

    public static void onedit(String a, String b, String c) {
        recyclerView.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        //getinfo.setVisibility(View.VISIBLE);
        post.setText("Save");
        title.setText(b);
        desc.setText(c);
        id = Integer.parseInt(a);
        titlee = b;
    }

    public String getDate(long timestamp) {
        //     Toast.makeText(questiondetailActivity.this, ""+timestamp, Toast.LENGTH_LONG).show();
        try {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
            sdf.setTimeZone(tz);


            return sdf.format(new Date(timestamp * 1000));
        } catch (Exception e) {
        }
        return "";
    }

}
