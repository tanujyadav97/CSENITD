package com.cse.csenitd.NoticeBoard;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cse.csenitd.Adapters.adapter_acheivement;
import com.cse.csenitd.Adapters.adapter_notice;
import com.cse.csenitd.Data.Acheivements_DATA;
import com.cse.csenitd.Data.Notices_DATA;
import com.cse.csenitd.DbHelper.Notice_Display;
import com.cse.csenitd.R;
import com.cse.csenitd.openingActivity;

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
import java.util.ArrayList;
import java.util.Date;

import static com.cse.csenitd.LoginActivity.CONNECTION_TIMEOUT;
import static com.cse.csenitd.LoginActivity.READ_TIMEOUT;

/**
 * Created by lenovo on 23-06-2017.Mohit yadav
 */

public class Notices extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Notices_DATA>>{
    RecyclerView mrecycler;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView.Adapter adapter;
    ProgressDialog progressDialog1;
    private FloatingActionButton ask;
    LinearLayout addnotice;
    Button post,cancel;
    EditText content;

    public static final String nurl="https://nitd.000webhostapp.com/cse%20nitd/mohit/getNotices.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

         progressDialog1= ProgressDialog.show(Notices.this, "Loading Notices", "Please wait...",false,false);
        ArrayList<String> dta =new ArrayList<>();
//        dta.add("No need to parse string colors in your code If you want to hardcode color values in your ");
//        dta.add("So you should check which kind ");
//
//        dta.add("The color tool helps you create, share, and apply color palettes to your UI, as well as measure the accessibility level of any color combination.");
//        dta.add(" and lighter variations of your \n" +
//                "primary and secondary colors.");
//        dta.add("Check if text is accessible on different-colored backgrounds, \n" +
//                "as measured using the Web Content Accessibility Guidelines \n" +
//                "legibility standards.\n" +
//                "mohit yadav");



        mrecycler=(RecyclerView)findViewById(R.id.noticeRecycleView);
        ask=(FloatingActionButton)findViewById(R.id.add);
        addnotice=(LinearLayout)findViewById(R.id.addnotice);
        post=(Button)findViewById(R.id.post);
        cancel=(Button)findViewById(R.id.cancel);
        content=(EditText) findViewById(R.id.addcontent);


        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecycler.setLayoutManager(staggeredGridLayoutManager);

        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 mrecycler.setVisibility(View.GONE);
                ask.setVisibility(View.GONE);
                addnotice.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle("Add Notice");
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mrecycler.setVisibility(View.VISIBLE);
                ask.setVisibility(View.VISIBLE);
                addnotice.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Notices");
            }
        });


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String data=content.getText().toString();
                if (data.equals("")) {

                    Toast.makeText(Notices.this, "Don't leave Content field empty!", Toast.LENGTH_LONG).show();

                }
                else
                {
                    new addnotice().execute(data, openingActivity.ps.getString("username","n/a"));
                }
            }
        });
        getLoaderManager().initLoader(0,null,this);
    }



    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        if((addnotice.getVisibility()==View.VISIBLE))
        {
            mrecycler.setVisibility(View.VISIBLE);
            ask.setVisibility(View.VISIBLE);
            addnotice.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Notices");
        }
        else
        {

        }
    }

    @Override
    public Loader<ArrayList<Notices_DATA>> onCreateLoader(int i, Bundle bundle) {
        return new Notice_Display(this,nurl);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Notices_DATA>> loader, ArrayList<Notices_DATA> data) {
        if (data != null && !data.isEmpty()) {
//            listAdaptr.addAll(earthquakes);
            //pgbar.setVisibility(View.INVISIBLE);
            updateUi(data);
        }
    }
    private void updateUi(ArrayList<Notices_DATA> data) {
        adapter=new adapter_notice(this,data);
        mrecycler.setAdapter(adapter);
        progressDialog1.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Notices_DATA>> loader) {

    }

    class addnotice extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             progressDialog = ProgressDialog.show(Notices.this, "Posting Notice", "Please wait...",false,false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {

                // Enter URL address where your php file resides
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/mohit/addnotice.php");

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
                        .appendQueryParameter("content", params[0])
                        .appendQueryParameter("username", params[1]);
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

                Toast.makeText(Notices.this, "OOPs! Unable to post notice.", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("negative"))
            {
                Toast.makeText(Notices.this, "Sorry! you are not authorized to post notice.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(Notices.this, "Notice posted successfully", Toast.LENGTH_LONG).show();
                Intent in=new Intent(Notices.this,Notices.class);
                startActivity(in);

            }
        }

        @Override
        protected void onCancelled() {

            progressDialog.dismiss();
        }
    }
}
