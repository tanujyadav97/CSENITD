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
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.zip.Inflater;

import static android.R.attr.bitmap;
import static com.cse.csenitd.LoginActivity.CONNECTION_TIMEOUT;
import static com.cse.csenitd.LoginActivity.READ_TIMEOUT;


/**
 * Created by lenovo on 20-06-2017. Mohit yadav
 */

public class Add_achievement extends AppCompatActivity {
    private EditText mPtext,mPtitle;
    private ImageView mpImg;
    private Button mbutton;
    public String edes,bitstr,tm,etitle;
    public static final int ACHIEVEMENT_LOADER_ID=1;
    public static final String furl="https://nitd.000webhostapp.com/cse%20nitd/mohit/insertachievement.php";
    public static final int  PICK_IMAGE_REQUEST=1;
    private Bitmap bitmap;
    private Uri filePath;
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_achievements);
        mpImg=(ImageView)findViewById(R.id.pimg);
        mPtext=(EditText)findViewById(R.id.ptext);
        mPtitle=(EditText)findViewById(R.id.ptitle);
        mbutton=(Button)findViewById(R.id.buttonImg);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        // progressBar=(ProgressBar)findViewById(R.id.progress);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showFileChooser();
            }
        });
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
            bitstr=getStringImage(bitmap);
            // Toast.makeText(this, bitstr, Toast.LENGTH_SHORT).show();
            long time= System.currentTimeMillis();
            tm=Long.toString(time);
            Toast.makeText(this, tm, Toast.LENGTH_SHORT).show();
            new insert().execute(edes,bitstr,tm,etitle);

        }
        return super.onOptionsItemSelected(item);
    }
    public class insert extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
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



            Toast.makeText(Add_achievement.this, result, Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);

            if(result.equals("true"))
            {

                Toast.makeText(Add_achievement.this, "Image updated successfully", Toast.LENGTH_LONG).show();

            } else if (result.equals("false")||result.equals("exception")||result.equals("unsuccessful") ) {

                //set old image to profile
                Toast.makeText(Add_achievement.this, "OOPs! Error updating image.", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {


        }
    }



}