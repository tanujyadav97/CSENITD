package com.cse.csenitd.ACHIEVEMENTS;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cse.csenitd.Adapters.adapter_acheivement;
import com.cse.csenitd.Data.Acheivements_DATA;
import com.cse.csenitd.DbHelper.Achievements_Display;
import com.cse.csenitd.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
 * Created by lenovo on 19-06-2017.Mohit yadav
 */

public class Acheivements extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Acheivements_DATA>>{
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private static final String JSON_RESPONSE="https://nitd.000webhostapp.com/cse%20nitd/mohit/retrieveAchievements.php";
    ProgressBar pgbar;
    private RecyclerView.Adapter mAdapter;

    private EditText mPtext,mPtitle;
    private ImageView mpImg;
    private Button mbutton,cancel;
    public String edes,bitstr,tm,etitle;
    public static final int ACHIEVEMENT_LOADER_ID=1;
    public static final String furl="https://nitd.000webhostapp.com/cse%20nitd/mohit/insertachievement.php";
    public static final int  PICK_IMAGE_REQUEST=1;
    private Bitmap bitmap;
    private Uri filePath;
    public int flag=0;
    Menu menuu;
    ProgressDialog progressDialog,progressDialog1;
FloatingActionButton add;
    RelativeLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        mpImg=(ImageView)findViewById(R.id.pimg);
        mPtext=(EditText)findViewById(R.id.ptext);
        mPtitle=(EditText)findViewById(R.id.ptitle);
        mbutton=(Button)findViewById(R.id.buttonImg);
        cancel=(Button)findViewById(R.id.cancel);
        // progressBar=(ProgressBar)findViewById(R.id.progress);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showFileChooser();
            }
        });
        add=(FloatingActionButton)findViewById(R.id.add);
        layout=(RelativeLayout)findViewById(R.id.layout);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                mRecyclerView.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle("Add Achievement");
                menuu.findItem(R.id.post).setVisible(true);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                mRecyclerView.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Achievements");
                menuu.findItem(R.id.post).setVisible(false);
            }
        });

         mRecyclerView=(RecyclerView)findViewById(R.id.acheivemnts_recycler_view);
        mLinearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        progressDialog1 = ProgressDialog.show(Acheivements.this, "Loading Acheivements", "Please wait...",false,false);

        getLoaderManager().initLoader(0,null,this);

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        if((layout.getVisibility()==View.VISIBLE))
        {
            mRecyclerView.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Achievements");
            menuu.findItem(R.id.post).setVisible(false);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            flag=1;
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mpImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_achievements,menu);
        menuu=menu;

        menuu.findItem(R.id.post).setVisible(false);
        return super.onCreateOptionsMenu(menu);

    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.post)
        {
            edes=mPtext.getText().toString().trim();
            etitle=mPtitle.getText().toString().trim();
            String a1=edes,a2=etitle;

            if(a1.replace(" ","").length()==0||a2.replace(" ","").length()==0)
            {
                Toast.makeText(this, "Please Don't leave the fields empty!", Toast.LENGTH_LONG).show();
            }
            else {
                if (flag == 1)
                    bitstr = getStringImage(bitmap);
                else
                    bitstr = "n/a";
                // Toast.makeText(this, bitstr, Toast.LENGTH_SHORT).show();
                long time = System.currentTimeMillis();
                tm = Long.toString(time);
                //Toast.makeText(this, tm, Toast.LENGTH_SHORT).show();
                new insert().execute(edes, bitstr, tm, etitle);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class insert extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;


        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(Acheivements.this, "Posting Achievement", "Please wait...",false,false);

        }
        @Override
        protected String doInBackground(String... params) {

            try {

                // Enter URL address where your php file resides
                url = new URL(furl);

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


                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("des", params[0])
                        .appendQueryParameter("image", params[1])
                        .appendQueryParameter("datetime",params[2])
                        .appendQueryParameter("title",params[3])
                        .appendQueryParameter("imgname",timeStamp)
                        .appendQueryParameter("username","abc");
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
                    //return params[0]+"/"+params[1];
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


            progressDialog.dismiss();
            Toast.makeText(Acheivements.this, result, Toast.LENGTH_LONG).show();

            if(result.equals("true"))
            {

                Toast.makeText(Acheivements.this, "Post updated successfully", Toast.LENGTH_LONG).show();
                mRecyclerView.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Achievements");
                menuu.findItem(R.id.post).setVisible(false);

                Intent intent=new Intent(Acheivements.this,Acheivements.class);
                startActivity(intent);

            } else if (result.equals("false")||result.equals("exception")||result.equals("unsuccessful") ) {

                //set old image to profile
                Toast.makeText(Acheivements.this, "OOPs! Error posting the post.", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {
progressDialog.dismiss();

        }
    }



    @Override
    public Loader<ArrayList<Acheivements_DATA>> onCreateLoader(int i, Bundle bundle) {
        return new Achievements_Display(this,JSON_RESPONSE);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Acheivements_DATA>> loader, ArrayList<Acheivements_DATA> data) {
        if (data != null && !data.isEmpty()) {
//            listAdaptr.addAll(earthquakes);
            //pgbar.setVisibility(View.INVISIBLE);
            progressDialog1.dismiss();
            updateUi(data);
        }
    }

    private void updateUi(ArrayList<Acheivements_DATA> data) {
        mAdapter=new adapter_acheivement(this,data);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Acheivements_DATA>> loader) {

    }
}
