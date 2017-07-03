package com.cse.csenitd.question;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.cse.csenitd.R;
import com.cse.csenitd.openingActivity;
import com.cse.csenitd.profile1;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class questionsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Config config;
    private FloatingActionButton ask;
    private LinearLayout askques;
    private Button cancel,post;
    private EditText addqueshead,addquestext,addqueslink,addquestags;
    public Menu menuu;
    public SearchView search;
    int searched;
    TextView noques;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
searched=0;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        ask=(FloatingActionButton)findViewById(R.id.askque);
        askques=(LinearLayout)findViewById(R.id.askques);
        cancel=(Button)findViewById(R.id.cancel);
        post=(Button)findViewById(R.id.addques);
        addqueshead=(EditText)findViewById(R.id.addqueshead);
        addquestext=(EditText)findViewById(R.id.addquestext);
        addqueslink=(EditText)findViewById(R.id.addqueslink);
        addquestags=(EditText)findViewById(R.id.addquestags);
        search=(SearchView)findViewById(R.id.searchview);
        noques=(TextView)findViewById(R.id.noques);


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.equals(""))
                {

                }
                else
                {
                    handle_search(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               recyclerView.setVisibility(View.GONE);
                ask.setVisibility(View.GONE);
                askques.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle("Ask Question");
                menuu.findItem(R.id.refresh).setVisible(false);
                menuu.findItem(R.id.search).setVisible(false);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String head=addqueshead.getText().toString();
                String text=addquestext.getText().toString();
                String link=addqueslink.getText().toString();
                String tags=addquestags.getText().toString();

                if(head.equals("")||text.equals(""))
                {
                    Toast.makeText(questionsActivity.this, "Don't leave topic and question fields empty!", Toast.LENGTH_LONG).show();
                }
                else {
                  //  recyclerView.setVisibility(View.VISIBLE);
                  //  ask.setVisibility(View.VISIBLE);
                  //  askques.setVisibility(View.GONE);

                    addquestion(head,text,link,tags);
                    //getData(); call only if successful
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                recyclerView.setVisibility(View.VISIBLE);
                ask.setVisibility(View.VISIBLE);
                askques.setVisibility(View.GONE);
                menuu.findItem(R.id.refresh).setVisible(true);
                menuu.findItem(R.id.search).setVisible(true);
                getSupportActionBar().setTitle("Questions");
            }
        });

        getData("");
    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
        if((askques.getVisibility()==View.VISIBLE))
        {
            recyclerView.setVisibility(View.VISIBLE);
            ask.setVisibility(View.VISIBLE);
            askques.setVisibility(View.GONE);
            menuu.findItem(R.id.refresh).setVisible(true);
            menuu.findItem(R.id.search).setVisible(true);
            getSupportActionBar().setTitle("Questions");
        }
        else {


            if (search.getVisibility() == View.VISIBLE) {
                search.setVisibility(View.GONE);
                ask.setVisibility(View.VISIBLE);
                if (searched == 1) {
                    getSupportActionBar().setTitle("Questions");
                    searched = 0;
                    getData("");
                }
            }
            else
            {  super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menuu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            search.setVisibility(View.GONE);
            ask.setVisibility(View.VISIBLE);
            if(searched==1)
            {
                getSupportActionBar().setTitle("Questions");
                searched=0;

            }
            getData("");
            return true;
        }
        else if (id == R.id.search) {
            //getData();
            if (search.getVisibility()==View.GONE) {
                search.setVisibility(View.VISIBLE);
                ask.setVisibility(View.GONE);
            }
                else {
                search.setVisibility(View.GONE);
                ask.setVisibility(View.VISIBLE);
                if (searched==1)
                {
                    getSupportActionBar().setTitle("Questions");
                    searched=0;
                    getData("");

                }
            }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void addquestion(String a,String b,String c,String d)
    {
        class addquesTask extends AsyncTask<String, String, String> {


            HttpURLConnection conn;
            URL url = null;
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(questionsActivity.this, "Posting Question", "Please wait...",false,false);
            }

            @Override
            protected String doInBackground(String... params) {
                // TODO: attempt authentication against a network service.

                try {

                    // Enter URL address where your php file resides
                    url = new URL("https://nitd.000webhostapp.com/cse%20nitd/addquestion.php");

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
                            .appendQueryParameter("head", params[0])
                            .appendQueryParameter("text", params[1])
                            .appendQueryParameter("link", params[2])
                            .appendQueryParameter("tags", params[3])
                            .appendQueryParameter("quesby", params[4]);
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

                    Toast.makeText(questionsActivity.this, "OOPs! Unable to post question.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(questionsActivity.this, "Question added successfully", Toast.LENGTH_LONG).show();
                    addquestext.setText("");
                    addqueslink.setText("");
                    addqueshead.setText("");
                    addquestags.setText("");
                    recyclerView.setVisibility(View.VISIBLE);
                    ask.setVisibility(View.VISIBLE);
                    askques.setVisibility(View.GONE);
                    getSupportActionBar().setTitle("Questions");
                    menuu.findItem(R.id.refresh).setVisible(true);
                    menuu.findItem(R.id.search).setVisible(true);
                    getData("");

                }
            }

            @Override
            protected void onCancelled() {

                progressDialog.dismiss();
            }
        }
        new addquesTask().execute(a,b,c,d, openingActivity.ps.getString("username","n/a"));
    }


    private void getData(final String ss){
        noques.setVisibility(View.GONE);
        class GetData extends AsyncTask<String,String,String>{
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(questionsActivity.this, "Fetching Data", "Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                if(!ss.equals(""))
                    getSupportActionBar().setTitle("Search Results");
                parseJSON(s);
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
                            .appendQueryParameter("query", params[0]);
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

            }
        }
        GetData gd = new GetData();
        gd.execute(ss);
    }

    public void showData(){
        adapter = new CardAdapter(Config.times, Config.votess,Config.topics,Config.quess,Config.tagss,Config.usernames,Config.accepteds);
        recyclerView.setAdapter(adapter);
        if(Config.times.length==0)
            noques.setVisibility(View.VISIBLE);
       // Toast.makeText(questionsActivity.this, ""+adapter.getItemCount()+"", Toast.LENGTH_LONG).show();
    }

    private void parseJSON(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            config = new Config(array.length());
            for(int i=0; i<array.length(); i++){
                JSONObject j = array.getJSONObject(i);

                Config.times[i] = getTime(j);
                Config.votess[i] = getVote(j);
                Config.topics[i] = getTopic(j);
                Config.quess[i] = getQues(j);
                Config.tagss[i] = getTags(j);
                Config.usernames[i] = getUser(j);
                Config.accepteds[i] = getAccep(j);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        showData();
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
    private String getTopic(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_TOPIC);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }
    private String getQues(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_QUES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }
    private String getTags(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_TAGS);
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

    private String getUser(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_USERNAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private void handle_search(String query)
    {
        searched=1;
        //Toast.makeText(questionsActivity.this, "searched", Toast.LENGTH_LONG).show();
        getData(query);
    }
}
