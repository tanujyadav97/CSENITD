package com.cse.csenitd.question.quesdetail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse.csenitd.R;
import com.cse.csenitd.openingActivity;
import com.cse.csenitd.profile1;

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
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.cse.csenitd.LoginActivity.CONNECTION_TIMEOUT;
import static com.cse.csenitd.LoginActivity.READ_TIMEOUT;

public class questiondetailActivity extends AppCompatActivity {


    Bitmap retrieveddp=null;
    ImageView dp;
    TextView votes;
    Button upvote;
    Button downvote;
    TextView topic;
    TextView tags;
    TextView ques;
    TextView link;
    TextView date;
    TextView name;
    TextView repo;
    ProgressDialog progressDialog;
    TextView anscount;
    Button addanswer;
    TextView addanswerlink;
    TextView addanswertext;
    Menu menuu;

    int flag,count,numberofanswers;
    public static RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter adapter;
    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questiondetail);
        Toast.makeText(this, openingActivity.ps.getString("quesid","n/a")+" "+ openingActivity.ps.getString("quesusername","n/a"), Toast.LENGTH_LONG).show();
        flag=0;count=0;numberofanswers=0;
        link=(TextView)findViewById(R.id.link);
        dp=(ImageView)findViewById(R.id.dp);
        votes=(TextView)findViewById(R.id.votes);
        topic=(TextView)findViewById(R.id.topic);
        tags=(TextView)findViewById(R.id.tags);
        ques=(TextView)findViewById(R.id.ques);
        date=(TextView)findViewById(R.id.date);
        name=(TextView)findViewById(R.id.name);
        repo=(TextView)findViewById(R.id.repo);
        upvote=(Button)findViewById(R.id.upvote);
        downvote=(Button)findViewById(R.id.downvote);
        anscount=(TextView)findViewById(R.id.anscount);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        addanswer=(Button)findViewById(R.id.addanswer);
        layoutManager = new LinearLayoutManager(this);
        addanswerlink=(TextView)findViewById(R.id.addanswerlink);
        addanswertext=(TextView)findViewById(R.id.addanswertext);
        recyclerView.setLayoutManager(layoutManager);

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                openingActivity.pe.putString("useusername",openingActivity.ps.getString("quesusername","n/a"));
                openingActivity.pe.commit();
                Intent in=new Intent(questiondetailActivity.this,profile1.class);
                startActivity(in);

            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openingActivity.pe.putString("useusername",openingActivity.ps.getString("quesusername","n/a"));
                openingActivity.pe.commit();
                Intent in=new Intent(questiondetailActivity.this,profile1.class);
                startActivity(in);

            }
        });

        addanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               String an=addanswertext.getText().toString();
                String lnk=addanswerlink.getText().toString();
                String a1=an;
                if(an.length()<20||a1.replace(" ","").length()==0)
                    Toast.makeText(questiondetailActivity.this, "Answer is too short!.", Toast.LENGTH_LONG).show();
                else
                {

                    new addansTask().execute(openingActivity.ps.getString("username","n/a"),openingActivity.ps.getString("quesid","n/a"),an,lnk);
                    getAnswers();
                }


            }
        });

        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String qid=openingActivity.ps.getString("quesid","n/a");
                String uid=openingActivity.ps.getString("username","n/a");
                String quid=openingActivity.ps.getString("quesusername","n/a");

                if(uid.equals(quid))
                    Toast.makeText(questiondetailActivity.this, "You cant vote your own question!", Toast.LENGTH_LONG).show();
                else
                {
                    new quesvoteTask().execute(qid,uid,quid,"1");   // first one forupvote
                    //also handle repo in this task, dont update repo in layout.
                }



            }
        });

        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String qid=openingActivity.ps.getString("quesid","n/a");
                String uid=openingActivity.ps.getString("username","n/a");
                String quid=openingActivity.ps.getString("quesusername","n/a");

                if(uid.equals(quid))
                    Toast.makeText(questiondetailActivity.this, "You cant vote your own question!", Toast.LENGTH_LONG).show();
                else
                {
                    new quesvoteTask().execute(qid,uid,quid,"-1");   // first -one for for downvote
                    //also handle repo in this task, dont update repo in layout.
                }

            }
        });

        getQuestionData();
        getAnswers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menuu=menu;
        menuu.findItem(R.id.search).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            progressDialog=ProgressDialog.show(questiondetailActivity.this, "Refreshing", "Please wait...",false,false);
            getAnswers();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getQuestionData()
    {
        progressDialog = ProgressDialog.show(questiondetailActivity.this, "Fetching question!","Please wait...",false,false);

        new questionTask().execute(openingActivity.ps.getString("quesid","n/a"),openingActivity.ps.getString("quesusername","n/a"),openingActivity.ps.getString("username","n/a"));
        new retrievedpTask().execute(openingActivity.ps.getString("quesusername","n/a"));


    }

    public void getAnswers()
    {
           getData();

    }

    public class quesvoteTask extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;


        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {

                // Enter URL address where your php file resides
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/vote.php");  //use same script for both ques and ans

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", params[0])
                        .appendQueryParameter("luid", params[1])
                        .appendQueryParameter("puid", params[2])
                        .appendQueryParameter("vote", params[3])
                        .appendQueryParameter("isques", "1");
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
                    return(result.toString());

                }else{

                    return("unsuccessful");
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
            if (result.equals("false")||result.equals("exception")||result.equals("unsuccessful") ) {

                Toast.makeText(questiondetailActivity.this, "OOPs! Unable to cast vote.", Toast.LENGTH_LONG).show();
            }
            else
            {
               /*  case 1: one -  upvoting first time
                   case 2: two-   downvoting first time
                   case 3: three- upvoting again(cancelling)
                   case 4: four-  downvoting again(cancelling)
                   case 5: five-  upvoting after downvoting
                   case 6: six-   downvoting after upvoting
                */
                 if(result.equals("one"))
                 {
                     Toast.makeText(questiondetailActivity.this, "upvoted", Toast.LENGTH_LONG).show();
                     int curvote=Integer.parseInt(votes.getText().toString());
                     String newvote=""+(curvote+1);
                     votes.setText(newvote);
                     upvote.setBackground(getResources().getDrawable(R.drawable.ic_up));
                 }
                 else if(result.equals("two"))
                 {
                     Toast.makeText(questiondetailActivity.this, "Downvoted", Toast.LENGTH_LONG).show();
                     int curvote=Integer.parseInt(votes.getText().toString());
                     String newvote=""+(curvote-1);
                     votes.setText(newvote);
                     downvote.setBackground(getResources().getDrawable(R.drawable.ic_down));
                 }
                 else if(result.equals("three"))
                 {
                     Toast.makeText(questiondetailActivity.this, "Vote cancelled.", Toast.LENGTH_LONG).show();
                     int curvote=Integer.parseInt(votes.getText().toString());
                     String newvote=""+(curvote-1);
                     votes.setText(newvote);
                     upvote.setBackground(getResources().getDrawable(R.drawable.ic_caret_arrow_up));
                 }
                 else if(result.equals("four"))
                 {
                     Toast.makeText(questiondetailActivity.this, "Vote cancelled.", Toast.LENGTH_LONG).show();
                     int curvote=Integer.parseInt(votes.getText().toString());
                     String newvote=""+(curvote+1);
                     votes.setText(newvote);
                     downvote.setBackground(getResources().getDrawable(R.drawable.ic_caret_down));
                 }
                 else if(result.equals("five"))
                 {
                     Toast.makeText(questiondetailActivity.this, "upvoted", Toast.LENGTH_LONG).show();
                     int curvote=Integer.parseInt(votes.getText().toString());
                     String newvote=""+(curvote+2);
                     votes.setText(newvote);
                     upvote.setBackground(getResources().getDrawable(R.drawable.ic_up));
                     downvote.setBackground(getResources().getDrawable(R.drawable.ic_caret_down));
                 }
                 else if(result.equals("six"))
                 {
                     Toast.makeText(questiondetailActivity.this, "downvoted", Toast.LENGTH_LONG).show();
                     int curvote=Integer.parseInt(votes.getText().toString());
                     String newvote=""+(curvote-2);
                     votes.setText(newvote);
                     upvote.setBackground(getResources().getDrawable(R.drawable.ic_caret_arrow_up));
                     downvote.setBackground(getResources().getDrawable(R.drawable.ic_down));
                 }
            }
        }

        @Override
        protected void onCancelled() {

            //progressDialog.dismiss();
        }
    }


    public class questionTask extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;


        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {

                // Enter URL address where your php file resides
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/getquesdetails.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("quesid", params[0])
                        .appendQueryParameter("quesusername", params[1])
                        .appendQueryParameter("username", params[2]);
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
                    return(result.toString());

                }else{

                    return("unsuccessful");
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
            if (result.equals("false")||result.equals("exception")||result.equals("unsuccessful") ) {

                Toast.makeText(questiondetailActivity.this, "OOPs! Unable to retrieve question! Connection problem.", Toast.LENGTH_LONG).show();
            }
            else
            {
             String s[];
                s=result.trim().split("___");
               // Toast.makeText(questiondetailActivity.this, ""+s.length, Toast.LENGTH_LONG).show();
                name.setText(s[0]);
                repo.setText(s[1]);
                votes.setText(s[2]);
                topic.setText(s[3]);
                tags.setText(s[4]);
                ques.setText(s[5]);
                link.setText(s[6]);

                Toast.makeText(questiondetailActivity.this, s[8], Toast.LENGTH_LONG).show();
                if(s[8].equals("1"))
                    upvote.setBackground(getResources().getDrawable(R.drawable.ic_up));
                    else if(s[7].equals("-1"))
                    downvote.setBackground(getResources().getDrawable(R.drawable.ic_down));

                date.setText("Asked on "+getDate(Long.parseLong(s[7])));

            }
        }

        @Override
        protected void onCancelled() {

            progressDialog.dismiss();
        }
    }

    public class addansTask extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;


        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {

                // Enter URL address where your php file resides
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/addanswer.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("ansby", params[0])
                        .appendQueryParameter("quesid", params[1])
                        .appendQueryParameter("ans", params[2])
                        .appendQueryParameter("link", params[3]);
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
                    return(result.toString());

                }else{

                    return("unsuccessful");
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
            if (result.equals("false")||result.equals("exception")||result.equals("unsuccessful") ) {

                Toast.makeText(questiondetailActivity.this, "OOPs! Unable to add answer.", Toast.LENGTH_LONG).show();
            }
            else
            {
                 Toast.makeText(questiondetailActivity.this, "Answer added successfully", Toast.LENGTH_LONG).show();
                addanswertext.setText("");
                addanswerlink.setText("");

            }
        }

        @Override
        protected void onCancelled() {

            progressDialog.dismiss();
        }
    }



    public class retrievedpTask extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;


        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {

                // Enter URL address where your php file resides
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/dpretrieve.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0]);
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
                    return(result.toString());

                }else{

                    return("unsuccessful");
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



            //Toast.makeText(profile1.this, result, Toast.LENGTH_LONG).show();

            if (result.equals("false")||result.equals("exception")||result.equals("unsuccessful") ) {


                Toast.makeText(questiondetailActivity.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();

            }
            else
            {

                new getdpTask().execute(result);

            }
        }

        @Override
        protected void onCancelled() {


        }
    }

    public class getdpTask extends AsyncTask<String, String, String> {
        Bitmap image;
        URL url;

        @Override
        protected String doInBackground(String... params) {


            try{

                url = new URL(params[0]);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                retrieveddp=image;
                return "true";

            }catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
                //Toast.makeText(profile1.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();
            }catch (IOException e) {
                e.printStackTrace();
                return "exception";
                //Toast.makeText(profile1.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();

            }


        }

        @Override
        protected void onPostExecute(String result) {



            //Toast.makeText(profile1.this, result, Toast.LENGTH_LONG).show();

            if (result.equals("exception") ) {


                Toast.makeText(questiondetailActivity.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();
                retrieveddp=null;
            }
            if(result.equals("true"))
            {
                if(retrieveddp!=null)
                    dp.setImageBitmap(retrieveddp);
            }

        }

        @Override
        protected void onCancelled() {

        }
    }

    public  String getDate(long timestamp) {
   //     Toast.makeText(questiondetailActivity.this, ""+timestamp, Toast.LENGTH_LONG).show();
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
            sdf.setTimeZone(tz);


            return sdf.format(new Date(timestamp * 1000));
        }catch (Exception e) {
        }
        return "";
    }

    private void getData(){
        class GetData extends AsyncTask<String,String,String>{
            ProgressDialog progressDialog1;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressDialog1 = ProgressDialog.show(questiondetailActivity.this, "Fetching Questions", "Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                if(s.equals("false")||s.equals("exception")||s.equals("unsuccessful"))
                    Toast.makeText(questiondetailActivity.this, "Unable to fetch answers.", Toast.LENGTH_LONG).show();
                else {
                    flag=1;
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
                    con = (HttpURLConnection)url.openConnection();
                    con.setReadTimeout(READ_TIMEOUT);
                    con.setConnectTimeout(CONNECTION_TIMEOUT);
                    con.setRequestMethod("POST");

                    // setDoInput and setDoOutput method depict handling of both send and receive
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    // Append parameters to URL
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("quesid", params[0])
                            .appendQueryParameter("username", openingActivity.ps.getString("username","n/a"));
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
                        }catch (NullPointerException e)
                        {
                            e.printStackTrace();
                            return "exception";
                        }
                        return result.toString().trim();

                    }else{

                        return("unsuccessful");
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
        gd.execute(openingActivity.ps.getString("quesid","n/a"));
    }

    public void showData(){
        adapter = new CardAdapter(this.getApplicationContext(),Config.times, Config.votess,Config.quesids,Config.anss,Config.ansids,Config.ansbys,Config.ansbynames,Config.ansbyrepos,Config.links,Config.accepteds,Config.dps,Config.voteds);
        recyclerView.setAdapter(adapter);
        String s;
        if(Config.times.length==1)
            s="1 Answer";
        else
            s=Config.times.length+" Answers";

        anscount.setText(s);
        // Toast.makeText(questionsActivity.this, ""+adapter.getItemCount()+"", Toast.LENGTH_LONG).show();
    }

    private void parseJSON(String json){
        String dpurls[]=null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            dpurls=new String[array.length()];
            config = new Config(array.length());
            for(int i=0; i<array.length(); i++){
                JSONObject j = array.getJSONObject(i);

                Config.times[i] = getTime(j);
                Config.votess[i] = getVote(j);
                Config.quesids[i] = getQuesid(j);
                Config.anss[i] = getAns(j);
                Config.ansbys[i] = getAnsby(j);
                Config.ansbynames[i] = getAnsbyname(j);
                Config.ansbyrepos[i] = getAnsbyrepo(j);
                Config.links[i] = getLink(j);
                Config.ansids[i] = getAnsid(j);
                Config.accepteds[i] = getAccep(j);
                Config.voteds[i] = getVoted(j);
                dpurls[i]=getdpurl(j);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        showData();
        getdpofanswers(dpurls);
    }

    public void getdpofanswers(String[] dpurls)
    {
        Integer i=0;
        count=0;
        numberofanswers=dpurls.length;
        for(i=0;i<dpurls.length;i++)
        {
            new getansdpTask().execute(dpurls[i],i.toString());
        }
        //start asynctask to get dp in a for loop passing dpurl at one by one
        // if in getansdptask() in false or unsuccessful, make all dp=null and call show data
        // in post exec of all async tasks, put retrieved dp in Config dp array using the index passed as param, increase a counter, and if it becomes equal to size of spurl array then call showData(); in post exec of getansdpTask();
    }



    public class getansdpTask extends AsyncTask<String, String, String> {
        Bitmap image,retrieved=null;
        URL url;

        @Override
        protected String doInBackground(String... params) {

            //int index=params[1];
            try{

                url = new URL(params[0]);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                retrieved=image;
                return "true"+" "+params[1];

            }catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception"+" "+params[1];
                //Toast.makeText(profile1.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();
            }catch (IOException e) {
                e.printStackTrace();
                return "exception"+" "+params[1];
                //Toast.makeText(profile1.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();

            }


        }

        @Override
        protected void onPostExecute(String result) {

             progressDialog.dismiss();
             int index=(Integer.parseInt((result.split(" "))[1]));
            //Toast.makeText(profile1.this, result, Toast.LENGTH_LONG).show();

            if (result.startsWith("exception") ) {

                Toast.makeText(questiondetailActivity.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();
                retrieved=null;
            }
            if(result.startsWith("true"))
            {

            }
            Config.dps[index]=retrieved;
            count++;
            if(count==numberofanswers)
                showData();


        }

        @Override
        protected void onCancelled() {

        }
    }

    private String getTime(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_TIME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getVote(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_VOTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }
    private String getQuesid(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_QUESID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }
    private String getAns(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_ANS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }
    private String getAnsby(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_ANSBY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getAccep(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_ACCEPTED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getAnsbyname(JSONObject j){
        String name = null;
        try {
            name = j.getString("ansbyname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getAnsbyrepo(JSONObject j){
        String name = null;
        try {
            name = j.getString("ansbyrepo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getVoted(JSONObject j){
        String name = null;
        try {
            name = j.getString("voted");
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // Toast.makeText(questiondetailActivity.this, name, Toast.LENGTH_LONG).show();

        return name;
    }

    private String getLink(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_LINK);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getAnsid(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_ANSID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getdpurl(JSONObject j){
        String name = null;
        try {
            name = j.getString("dpurl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

}
