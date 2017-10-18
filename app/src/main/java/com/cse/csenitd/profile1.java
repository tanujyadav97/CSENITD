package com.cse.csenitd;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.muzakki.ahmad.widget.CollapsingToolbarLayout;

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
import java.util.ArrayList;

import static com.cse.csenitd.LoginActivity.CONNECTION_TIMEOUT;
import static com.cse.csenitd.LoginActivity.READ_TIMEOUT;


public class profile1 extends AppCompatActivity {

    Button addtechb;
    EditText addtech;
    Button save;
    Button logout;
    View profileform;
    View editform;
    CollapsingToolbarLayout tl;
    ImageView dp;
    TextView reputation;
    private View mProgressView;
    Button adddp;
    int flag;
    private Bitmap bitmap;
    private Uri filePath;
    private Drawable prevdp;
    Bitmap retrieveddp=null;

    //editform variables
    TextView etech,eanswer,equestion;
    EditText estatus,ebranch,eorganisation,eemail,ephone,elocation;
    Spinner edesig;

    //profile variables
    TextView tech,answer,question,desig,status,branch,organisation,email,phone,location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        flag=0;

        eanswer=(TextView)findViewById(R.id.eanswer);
        equestion=(TextView)findViewById(R.id.equestion);
        estatus=(EditText)findViewById(R.id.estatus);
        ebranch=(EditText)findViewById(R.id.ebranch);
        eorganisation=(EditText)findViewById(R.id.eorganisation);
        eemail=(EditText)findViewById(R.id.eemail);
        ephone=(EditText)findViewById(R.id.ephone);
        elocation=(EditText)findViewById(R.id.elocation);
        edesig=(Spinner)findViewById(R.id.edesig);
        etech=(TextView) findViewById(R.id.etech);
        addtechb=(Button)findViewById(R.id.addtechb);
        addtech=(EditText) findViewById(R.id.addtech);

        answer=(TextView)findViewById(R.id.answer);
        tech=(TextView)findViewById(R.id.tech);
        question=(TextView)findViewById(R.id.question);
        desig=(TextView)findViewById(R.id.desig);
        status=(TextView)findViewById(R.id.status);
        branch=(TextView)findViewById(R.id.branch);
        organisation=(TextView)findViewById(R.id.organisation);
        email=(TextView)findViewById(R.id.email);
        phone=(TextView)findViewById(R.id.phone);
        location=(TextView)findViewById(R.id.location);

        profileform=findViewById(R.id.profileform);
        editform=findViewById(R.id.editform);
        save=(Button)findViewById(R.id.save);
        logout=(Button)findViewById(R.id.logout);
        tl=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        dp=(ImageView)findViewById(R.id.dp);
        reputation=(TextView)findViewById(R.id.reputation);
        mProgressView = findViewById(R.id.loading);
        adddp=(Button)findViewById(R.id.adddp);

        prevdp=dp.getDrawable();

        addtechb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addtech.getText()!=null)
                {
                    if(etech.getText().toString().equals("n/a"))
                        etech.setText(addtech.getText().toString());
                    else
                        etech.setText(etech.getText().toString()+"\n"+addtech.getText().toString());
                }
                addtech.setText("");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(save.getText().toString().equals("Edit"))
                {
                 save.setText("Save");
                    profileform.setVisibility(View.GONE);
                    editform.setVisibility(View.VISIBLE);
                    adddp.setVisibility(View.VISIBLE);

                    eanswer.setText(answer.getText().toString());
                    equestion.setText(question.getText().toString());
                    estatus.setText(status.getText().toString());
                    ebranch.setText(branch.getText().toString());
                    eorganisation.setText(organisation.getText().toString());
                    eemail.setText(email.getText().toString());
                    ephone.setText(phone.getText().toString());
                    etech.setText(tech.getText().toString());
                    elocation.setText(location.getText().toString());
                    if(desig.getText().toString().equals("1st year B.Tech Student"))
                        edesig.setSelection(1,false);
                    if(desig.getText().toString().equals("2nd year B.Tech Student"))
                        edesig.setSelection(2,false);
                    if(desig.getText().toString().equals("3rd year B.Tech Student"))
                        edesig.setSelection(3,false);
                    if(desig.getText().toString().equals("4th year B.Tech Student"))
                        edesig.setSelection(4,false);
                    if(desig.getText().toString().equals("1st year M.Tech Student"))
                        edesig.setSelection(5,false);
                    if(desig.getText().toString().equals("2nd year M.Tech Student"))
                        edesig.setSelection(6,false);
                    if(desig.getText().toString().equals("Phd Student"))
                        edesig.setSelection(7,false);
                    if(desig.getText().toString().equals("Faculty"))
                        edesig.setSelection(8,false);
                    if(desig.getText().toString().equals("Passed Out"))
                        edesig.setSelection(9,false);

                }
                else
                {
                    adddp.setVisibility(View.GONE);
                    updateprofile();

                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openingActivity.pe.putString("username","n/a");
                openingActivity.pe.commit();
                Intent in=new Intent(profile1.this,LoginActivity.class);
                startActivity(in);
            }
        });

        adddp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             showFileChooser();
            }
        });


        if(openingActivity.ps.getString("useusername","n/a").equals("n/a")) {
            save.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            initializeprofile(openingActivity.ps.getString("username", "n/a"));
        }
            else
        {

            if(!(openingActivity.ps.getString("useusername","n/a").equals(openingActivity.ps.getString("username","n/a")))) {
                save.setVisibility(View.GONE);
                logout.setVisibility(View.GONE);
            }
            initializeprofile(openingActivity.ps.getString("useusername","n/a"));
            openingActivity.pe.putString("useusername","n/a");
            openingActivity.pe.commit();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
           flag=1;
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                prevdp=dp.getDrawable();
                dp.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void initializeprofile(String usernam)
    {
        showProgress(true,profileform);
        showProgress(true,editform);
        // dptask to retrieve image
        new retrievedpTask().execute(usernam);
        new ProfileTask().execute(usernam);
    }

    public void updateprofile()
    {


        if((ephone.getText().toString().length() == 10 || (ephone.getText().toString().length() == 12 && ephone.getText().toString().startsWith("91")) || (ephone.getText().toString().length() == 13 && ephone.getText().toString().startsWith("+91"))))
        {
        if(isEmailValid(eemail.getText().toString())) {
            String a1=estatus.getText().toString(),a2=ebranch.getText().toString(),a3=eorganisation.getText().toString(),a4= etech.getText().toString(),a5=elocation.getText().toString();
            if (a1.replace(" ","").length()==0 || a2.replace(" ","").length()==0 ||a3.replace(" ","").length()==0  ||a4.replace(" ","").length()==0  ||a5.replace(" ","").length()==0 || edesig.getSelectedItem().toString().equals("Choose Designation")) {

                Toast.makeText(profile1.this, "Please fill all the fields.", Toast.LENGTH_LONG).show();

            }
                else
            {
                showProgress(true,editform);

                //if image changed
                if(flag==1)
                {
                    flag=0;
                    String uploadImage = getStringImage(bitmap);
                    new dpTask().execute(openingActivity.ps.getString("username","n/a"),uploadImage);
                }
                new ProfileUpdateTask().execute(estatus.getText().toString(), ebranch.getText().toString(), eorganisation.getText().toString()
                        , eemail.getText().toString(), ephone.getText().toString(), etech.getText().toString(), elocation.getText().toString(), edesig.getSelectedItem().toString());
                save.setText("Edit");
                editform.setVisibility(View.GONE);
                profileform.setVisibility(View.VISIBLE);
            }

        }
        else
        {
            Toast.makeText(profile1.this, "Please enter valid email address.", Toast.LENGTH_LONG).show();
        }
        }
        else
        {
            Toast.makeText(profile1.this, "Please enter valid phone number.", Toast.LENGTH_LONG).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show,View form) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            form.animate().setDuration(shortAnimTime).alpha(
                    show ? Float.parseFloat(new Float(0.5).toString()) : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //      mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class ProfileTask extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;


        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {

                // Enter URL address where your php file resides
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/profile.php");

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

            showProgress(false,profileform);

            //  Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();

            if(result.startsWith("true"))
            {
                //add all data to items
                String arr[];
                arr=result.split("~");
                answer.setText(arr[1]);
                question.setText(arr[2]);
                status.setText(arr[3]);
                branch.setText(arr[4]);
                organisation.setText(arr[5]);
                email.setText(arr[6]);
                phone.setText(arr[7]);

                String arr1[];
                arr1=arr[8].trim().split("%");
                String t=arr1[0];

                for(int i=1;i<arr1.length;i++)
                {
                    t+="\n";
                    t+=arr1[i];
                }

                tech.setText(t);
                location.setText(arr[9]);
                desig.setText(arr[10]);
                reputation.setText(arr[11]);
                tl.setTitle(arr[12]);
                tl.setSubtitle(arr[13]);

       //         Toast.makeText(profile1.this, "Data retrieved successfully", Toast.LENGTH_LONG).show();

            } else if (result.startsWith("false") ) {

                Toast.makeText(profile1.this, "OOPs! Error retrieving data.", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false,profileform);
        }
    }


    public class ProfileUpdateTask extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;


        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {

                // Enter URL address where your php file resides
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/profileupdate.php");

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

                String arr1[]=params[5].split("\n");
                String t=arr1[0];
                for(int i=1;i<arr1.length;i++)
                {
                    t+="%";
                    t+=arr1[i];
                }

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", openingActivity.ps.getString("username","n/a"))
                        .appendQueryParameter("status",params[0] )
                        .appendQueryParameter("branch",params[1] )
                        .appendQueryParameter("organisation", params[2])
                        .appendQueryParameter("email",params[3] )
                        .appendQueryParameter("phone", params[4])
                        .appendQueryParameter("tech",t )
                        .appendQueryParameter("location", params[6])
                        .appendQueryParameter("desig",params[7]);
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

            showProgress(false,editform);

            //  Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();

            if(result.toString().equals("true"))
            {
                //add all data to items

                status.setText(estatus.getText().toString());
                branch.setText(ebranch.getText().toString());
                organisation.setText(eorganisation.getText().toString());
                email.setText(eemail.getText().toString());
                phone.setText(ephone.getText().toString());
                tech.setText(etech.getText().toString());
                location.setText(elocation.getText().toString());
                desig.setText(edesig.getSelectedItem().toString());

       //         Toast.makeText(profile1.this, "Data updated successfully", Toast.LENGTH_LONG).show();

            } else if (result.toString().equals("false") ) {

                Toast.makeText(profile1.this, "Unable to save data! Connection Problem.", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false,editform);
        }
    }

    public class dpTask extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;


        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            try {

                // Enter URL address where your php file resides
                url = new URL("https://nitd.000webhostapp.com/cse%20nitd/dpupload.php");

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
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("image", params[1]);
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

            showProgress(false,profileform);

            //  Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();

            if(result.equals("true"))
            {

          //      Toast.makeText(profile1.this, "Image updated successfully", Toast.LENGTH_LONG).show();

            } else if (result.equals("false")||result.equals("exception")||result.equals("unsuccessful") ) {

                //set old image to profile
                dp.setImageDrawable(prevdp);
                Toast.makeText(profile1.this, "OOPs! Error updating image.", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false,profileform);
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
                showProgress(false,editform);

                Toast.makeText(profile1.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();

            }
            else
            {

             new getdpTask().execute(result);

            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false,editform);
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

            showProgress(false,editform);

              //Toast.makeText(profile1.this, result, Toast.LENGTH_LONG).show();

            if (result.equals("exception") ) {

                //set old image to profile
                dp.setImageDrawable(prevdp);
                Toast.makeText(profile1.this, "OOPs! Error retrieving profile image.", Toast.LENGTH_LONG).show();
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

            showProgress(false,editform);
        }
    }



    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        String a=email;
        return (a.contains("@")&&a.replace(" ","").length()>=3);
    }
}
