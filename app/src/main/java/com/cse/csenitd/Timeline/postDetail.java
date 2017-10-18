package com.cse.csenitd.Timeline;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cse.csenitd.Adapters.adapter_comment;
import com.cse.csenitd.Behaviours.DynamicImageView;
import com.cse.csenitd.Data.Comment_DATA;
import com.cse.csenitd.DbHelper.Comment_display;
import com.cse.csenitd.DbHelper.ImageLoader;
import com.cse.csenitd.R;
import com.cse.csenitd.openingActivity;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

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
import static com.cse.csenitd.openingActivity.pe;
import static com.cse.csenitd.openingActivity.ps;

/**
 * Created by lenovo on 29-06-2017.Mohit yadav
 */

public class postDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Comment_DATA>>{
    ViewPager mViewPager;
    CustomPagerAdapter  mCustomPagerAdapter;
    private int size=0;
   // ArrayList<Bitmap> mResources;
    public int newInt;
    TextView des1, like, comment, name,date;
    FrameLayout frameLayout;
    ImageView userimg;
    ImageButton likebtb;
   ImageView ownerimg;
    EditText cmntedittext;
    Button postbtn;
    ArrayList<String> bt;
    String videourl;
    ImageLoader imageloader;
    RecyclerView cmtrc;
    SharedPreferences sharedPref;
    private final String furl="https://nitd.000webhostapp.com/cse%20nitd/mohit/postdetail.php";
    private final String cmturl="https://nitd.000webhostapp.com/cse%20nitd/mohit/getcomments.php";
    private final String posturl="https://nitd.000webhostapp.com/cse%20nitd/mohit/addcomments.php";
    RecyclerView.Adapter madpter;
    LinearLayoutManager manager;
    FrameLayout frm;
    private SimpleExoPlayer player=null;
    private SimpleExoPlayerView playerView;
    String id;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetail);
        des1 = (TextView) findViewById(R.id.posttxt);
        like = (TextView) findViewById(R.id.likes);
        comment = (TextView)findViewById(R.id.comments);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        userimg = (ImageView) findViewById(R.id.userimg);
        name = (TextView) findViewById(R.id.dp);
        date=(TextView)findViewById(R.id.date);
        likebtb=(ImageButton)findViewById(R.id.likebutton);
        ownerimg=(ImageView)findViewById(R.id.ownerimg);
        cmntedittext=(EditText)findViewById(R.id.cmtedittext);
        postbtn=(Button)findViewById(R.id.cmtpost);
        imageloader=new ImageLoader(this);
        cmtrc=(RecyclerView)findViewById(R.id.cmtrecyclerview);
       manager=new LinearLayoutManager(this);
        playerView=new SimpleExoPlayerView(postDetail.this);
       cmtrc.setLayoutManager(manager);
        frm=(FrameLayout)findViewById(R.id.frame);

        likebtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                castlike(id);
            }
        });

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=cmntedittext.getText().toString();
                String tt=s;
                if(tt.replace(" ","").length()==0)
                {
                    Toast.makeText(postDetail.this,"Can't leave empty comment", Toast.LENGTH_SHORT).show();
                }
                else {
                    InsertComments(s);
                }

            }
        });
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newInt= 0;
            } else {
                newInt= extras.getInt("postid");
                sharedPref = getSharedPreferences("mypostid",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("pid",newInt);
                editor.commit();
            }
        } else {
            newInt= (int) savedInstanceState.getSerializable("postid");
        }
//        Toast.makeText(this,Integer.valueOf(newInt).toString(), Toast.LENGTH_SHORT).show();
        bt =new ArrayList<>();
        //mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager=new ViewPager(this);

        id=newInt+"";
        new getpostJson().execute(Integer.toString(newInt));

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void InsertComments(String s)
    {
        new PostCommments().execute(s,Integer.toString(newInt));
    }
    private void loadComments(){
        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        SharedPreferences prefs = this.getSharedPreferences("mypostid", MODE_PRIVATE);
       int restoredText = prefs.getInt("pid", 0);
        if (restoredText != 0) {
//            Toast.makeText(this, Integer.toString(restoredText), Toast.LENGTH_SHORT).show();
            return new Comment_display(this,cmturl,restoredText);
             }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Comment_DATA>> loader, ArrayList<Comment_DATA> data) {
        if(data!=null)
        {
            updateUi(data);
        }
        else {
//            Toast.makeText(this, "bll", Toast.LENGTH_SHORT).show();
        }
    }
private  void updateUi(ArrayList<Comment_DATA> da)
{
    madpter=new adapter_comment(this,da);
    cmtrc.setAdapter(madpter);
}


    @Override
    public void onLoaderReset(Loader loader) {

    }
    public class PostCommments extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // Enter URL address where your php file resides
                url = new URL(posturl);

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


                long unixTime = System.currentTimeMillis();
                String time=Long.toString(unixTime);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("comment",params[0])
                        .appendQueryParameter("postid", params[1])
                        .appendQueryParameter("username", openingActivity.ps.getString("username","n/a"))
                        .appendQueryParameter("time",time);
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

          if(result.equals("true"))
          {
              cmntedittext.setText("");
              getLoaderManager().restartLoader(0,null,postDetail.this);
          }
          else
          {
              Toast.makeText(postDetail.this, "Unable to post comment", Toast.LENGTH_LONG).show();
          }
           // Toast.makeText(postDetail.this, result, Toast.LENGTH_LONG).show();


        }

        @Override
        protected void onCancelled() {

            //     $name.'___'.$row['text'].'___'.$row['img1'].'___'.$row['img2'].'___'.$row['img3'].'___'.$row['img4'].
            //           '___'.$row['img5'].'___'.$row['datetime'].'___'.$row['video'].'___'.$row['likes'].'___'.$userimg.'___'.$usern;

        }
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (Util.SDK_INT > 23) {
//            initializePlayer();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //hideSystemUi();
//        if (Util.SDK_INT <= 23 || player == null) {
//            initializePlayer();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (Util.SDK_INT <= 23) {
//            releasePlayer();
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (Util.SDK_INT > 23) {
//            releasePlayer();
//        }
//    }
//
//    private void initializePlayer() {
//
//    }
//
//    private void releasePlayer() {
//        if (player != null) {
//            playbackPosition = player.getCurrentPosition();
//            currentWindow = player.getCurrentWindowIndex();
//            playWhenReady = player.getPlayWhenReady();
//            player.release();
//            player = null;
//        }
//    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri, new DefaultHttpDataSourceFactory("exoplayer-codelab"),
                new DefaultExtractorsFactory(), null, null);
    }
    public class getpostJson extends AsyncTask<String, String, String> {


        HttpURLConnection conn;
        URL url = null;


        @Override
        protected void onPreExecute() {

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


                 // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("postid",params[0])
                        .appendQueryParameter("username", openingActivity.ps.getString("username","n/a"));
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

           // Toast.makeText(postDetail.this, result, Toast.LENGTH_LONG).show();
            String s[];
            s=result.trim().split("___");
            // Toast.makeText(questiondetailActivity.this, ""+s.length, Toast.LENGTH_LONG).show();
            name.setText(s[0]);
            //Toast.makeText(postDetail.this, s[1], Toast.LENGTH_SHORT).show();
           des1.setText(s[1]);
            date.setText(s[7]);
            like.setText(s[9]);
            if(!s[2].isEmpty())
                bt.add(s[2]);
            if(!s[3].isEmpty())
                bt.add(s[3]);
            if(!s[4].isEmpty())
                bt.add(s[4]);
            if(!s[5].isEmpty())
                bt.add(s[5]);
            if(!s[6].isEmpty())
                bt.add(s[6]);
            videourl=s[8];


            if(s[12].equals("1"))
            {
                likebtb.setImageDrawable(getResources().getDrawable(R.drawable.liked));;
            }

            imageloader.DisplayImage(s[10], ownerimg);
            imageloader.DisplayImage(s[13],userimg);
           // Toast.makeText(postDetail.this, s[10], Toast.LENGTH_SHORT).show();
//            Toast.makeText(postDetail.this, videourl, Toast.LENGTH_SHORT).show();
//            Toast.makeText(postDetail.this, Integer.toString(bt.size()), Toast.LENGTH_SHORT).show();
          if(bt.size()==0&&videourl.equals("null")){
//                Toast.makeText(postDetail.this, "0", Toast.LENGTH_SHORT).show();
                frm.removeAllViews();
            }

          else if(!videourl.isEmpty()) {
              Toast.makeText(postDetail.this,videourl, Toast.LENGTH_SHORT).show();
              frm.removeAllViews();
              initializePlayer();
//                frm.addView(playerView);
          }
            else {
                //Toast.makeText(postDetail.this, "h", Toast.LENGTH_SHORT).show();
                mViewPager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
                mViewPager.setBackgroundColor(Color.parseColor("#000000"));
                frm.removeAllViews();
                mCustomPagerAdapter = new CustomPagerAdapter(postDetail.this, bt);
                mCustomPagerAdapter.notifyDataSetChanged();
                mViewPager.setAdapter(mCustomPagerAdapter);
                frm.addView(mViewPager);

            }

            loadComments();
        }

        @Override
        protected void onCancelled() {

        }
    }



    private void initializePlayer( ) {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(), new DefaultLoadControl());
           playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
        }
        //HttpProxyCacheServer proxy = app.getProxy(mContext);
        //String proxyUrl = proxy.getProxyUrl(String.valueOf(Uri.parse(url)));
        MediaSource mediaSource = buildMediaSource(Uri.parse(videourl));
        player.prepare(mediaSource, true, false);
        playerView.setPadding(5, 5, 0, 0);
        playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,400));
        frm.addView(playerView);
    }
    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<String> urls;

        public CustomPagerAdapter(Context context,ArrayList<String> url) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.urls=url;
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            //imageloader.DisplayImage(urls.get(position),imageView);
            //Picasso.with(mContext).load(bt.get(position)).into(imageView);
//            Toast.makeText(mContext, urls.get(position), Toast.LENGTH_SHORT).show();
            imageloader.DisplayImage(urls.get(position),imageView);
            container.addView(itemView);

            return itemView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }


    public  void castlike(final String idd) {
        class likeTask extends AsyncTask<String, String, String> {


            HttpURLConnection conn;
            URL url = null;


            @Override
            protected String doInBackground(String... params) {
                // TODO: attempt authentication against a network service.

                try {

                    // Enter URL address where your php file resides
                    url = new URL("https://nitd.000webhostapp.com/cse%20nitd/mohit/liketimeline.php");
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
                            .appendQueryParameter("user", openingActivity.ps.getString("username","n/a"));
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

              //      Toast.makeText(postDetail.this, "OOPs! Unable to like the post.", Toast.LENGTH_LONG).show();
                } else {
               /*  case 1: one -  liked
                   case 2: two-   canceled like
                */
                    if (result.equals("one")) {
//                        Toast.makeText(postDetail.this, "Liked", Toast.LENGTH_LONG).show();
                        int curlikes = Integer.parseInt(like.getText().toString());
                        String newvote = "" + (curlikes + 1);
                        like.setText(newvote);
                        likebtb.setImageDrawable(getResources().getDrawable(R.drawable.liked));
                    } else if (result.equals("two")) {
//                        Toast.makeText(postDetail.this, "Unliked", Toast.LENGTH_LONG).show();
                        int curlikes = Integer.parseInt(like.getText().toString());
                        String newvote = "" + (curlikes - 1);
                        like.setText(newvote);
                        likebtb.setImageDrawable(getResources().getDrawable(R.drawable.like));
                    }
                }
            }

            @Override
            protected void onCancelled() {

                //progressDialog.dismiss();
            }
        }

        new likeTask().execute(idd);
    }

}
