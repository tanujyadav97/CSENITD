package com.cse.csenitd.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cse.csenitd.ACHIEVEMENTS.Acheivements;
import com.cse.csenitd.Data.Acheivements_DATA;
import com.cse.csenitd.DbHelper.ImageLoader;
import com.cse.csenitd.R;
import com.cse.csenitd.openingActivity;
import com.cse.csenitd.profile1;

import org.mortbay.jetty.servlet.Holder;

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
import java.util.ArrayList;

import static com.cse.csenitd.LoginActivity.CONNECTION_TIMEOUT;
import static com.cse.csenitd.LoginActivity.READ_TIMEOUT;

/**
 * Created by lenovo on 19-06-2017. Mohit yadav
 */

public class adapter_acheivement extends RecyclerView.Adapter<adapter_acheivement.ItemrowHolder> {
    private ArrayList<Acheivements_DATA> DataList_ach;
    public Context mContext;
    ImageLoader imageLoader;
    public ItemrowHolder useholder;
    boolean isImageFitToScreen=false;


    public adapter_acheivement(Context context, ArrayList<Acheivements_DATA> list) {
        this.mContext = context;
        this.DataList_ach = list;
        imageLoader=new ImageLoader(mContext);
    }

    @Override
    public ItemrowHolder onCreateViewHolder(ViewGroup  parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_achievements,parent, false);
        return new ItemrowHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(final ItemrowHolder holder, int position) {
        final Acheivements_DATA obj;
        final int usepos;
        final String id;
        obj = DataList_ach.get(position);
        usepos=position;
        holder.desciption.setText(obj.get_des());
        holder.Likes.setText(Integer.valueOf(obj.get_likes()).toString());
        holder.postby.setText(obj.get_UserName());
        holder.datetime.setText(obj.getDate());
        imageLoader.DisplayImage(obj.get_urlString(), holder.imageView);
        holder.usernm.setText(obj.get_UserName());
        holder.postby.setText(obj.get_name());
        //holder.rep.setText(Integer.valueOf(obj.get_rep()).toString());
        imageLoader.DisplayImage(obj.getUserImag(),holder.UserImg);
        holder.title.setText(obj.get_title());

        String arr[]=obj.getliked().split(" ");
        String liked=arr[0];
        id=arr[1];
        if(liked.equals("1"))
            holder.likebutton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.liked));

        holder.UserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openingActivity.pe.putString("useusername",holder.usernm.getText().toString());
                openingActivity.pe.commit();
                Intent in=new Intent(mContext,profile1.class);
                mContext.startActivity(in);
            }
        });
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openingActivity.pe.putString("useusername",holder.usernm.getText().toString());
                openingActivity.pe.commit();
                Intent in=new Intent(mContext,profile1.class);
                mContext.startActivity(in);
            }
        });

        holder.likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idd=id;
                String user=openingActivity.ps.getString("username","n/a");
                    useholder=holder;
               // Toast.makeText(mContext, idd, Toast.LENGTH_LONG).show();
                castlike(idd,user,usepos,obj);


            }
        });
    }

    @Override
    public int getItemCount() {
        return DataList_ach.size();
    }

    public class ItemrowHolder extends RecyclerView.ViewHolder {
        public TextView desciption, datetime, postby, Likes, title,usernm;
        public ImageView imageView, UserImg;
        public ImageButton likebutton;
        LinearLayout info;
        public ItemrowHolder(View itemView) {
            super(itemView);
            desciption = (TextView) itemView.findViewById(R.id.des);
           imageView = (ImageView) itemView.findViewById(R.id.imageView);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
            postby = (TextView) itemView.findViewById(R.id.postby);
            Likes = (TextView) itemView.findViewById(R.id.noOfLikes);
            UserImg = (ImageView) itemView.findViewById(R.id.poster);
            title=(TextView)itemView.findViewById(R.id.title);
            usernm=(TextView)itemView.findViewById(R.id.usrnm);
            info=(LinearLayout)itemView.findViewById(R.id.info);
            likebutton=(ImageButton)itemView.findViewById(R.id.likebutton);
        }
    }

    public  void castlike(final String idd,String user,final int usepos,final Acheivements_DATA obj) {
        class likeTask extends AsyncTask<String, String, String> {


            HttpURLConnection conn;
            URL url = null;


            @Override
            protected String doInBackground(String... params) {
                // TODO: attempt authentication against a network service.

                try {

                    // Enter URL address where your php file resides
                    url = new URL("https://nitd.000webhostapp.com/cse%20nitd/mohit/likeachievement.php");
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
                            .appendQueryParameter("id", params[0])
                            .appendQueryParameter("user", params[1]);
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

                if (result.equals("false") || result.equals("exception") || result.equals("unsuccessful")) {

                    Toast.makeText(mContext, "OOPs! Unable to like the post.", Toast.LENGTH_LONG).show();
                } else {
               /*  case 1: one -  liked
                   case 2: two-   canceled like
                */
                    if (result.equals("one")) {
                  //Toast.makeText(mContext, "Liked", Toast.LENGTH_LONG).show();
                        int curlikes = Integer.parseInt(useholder.Likes.getText().toString());
                        String newvote = "" + (curlikes + 1);
                        useholder.Likes.setText(newvote);
                        obj.setliked("1 " + idd);
                        obj.set_likes(curlikes + 1);
                        DataList_ach.set(usepos, obj);
                        useholder.likebutton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.liked));
                    } else if (result.equals("two")) {
                      //  Toast.makeText(mContext, "Unliked", Toast.LENGTH_LONG).show();
                        int curlikes = Integer.parseInt(useholder.Likes.getText().toString());
                        String newvote = "" + (curlikes - 1);
                        useholder.Likes.setText(newvote);
                        obj.setliked("0" + idd);
                        obj.set_likes(curlikes - 1);
                        DataList_ach.set(usepos, obj);
                        useholder.likebutton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.like));
                    }
                }
            }

            @Override
            protected void onCancelled() {

                //progressDialog.dismiss();
            }
        }

        new likeTask().execute(idd,user);
    }


}
