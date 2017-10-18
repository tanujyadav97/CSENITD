package com.cse.csenitd.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse.csenitd.Behaviours.DynamicImageView;
import com.cse.csenitd.Data.Timeline_DATA;
import com.cse.csenitd.DbHelper.App;
import com.cse.csenitd.DbHelper.ImageLoader;
import com.cse.csenitd.R;
import com.cse.csenitd.Timeline.postDetail;
import com.cse.csenitd.openingActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

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
import java.util.HashMap;

import static com.cse.csenitd.LoginActivity.CONNECTION_TIMEOUT;
import static com.cse.csenitd.LoginActivity.READ_TIMEOUT;

/**
 * Created by lenovo on 28-06-2017.Mohit yadav
 */

public class adapter_timeline extends RecyclerView.Adapter<adapter_timeline.timelineitemrow_holder> {
    private ArrayList<Timeline_DATA> DataList_timeline;
    public Context mContext;
    ImageLoader imageLoader;
    boolean playWhenReady = false;
    int currentWindow;
    long playbackPosition;
    private SimpleExoPlayer player = null;
    HashMap<String, String> hs;
    Point size;
    StaggeredGridLayoutManager staggeredlayout;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private App app;
    public static int k = 0;
    public timelineitemrow_holder useholder = null;
    imagesAdapter imagesAdaptr;

    @Override
    public timelineitemrow_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_timeline, parent, false);
        return new adapter_timeline.timelineitemrow_holder(v);
    }

    public adapter_timeline(Context context, ArrayList<Timeline_DATA> list) {
        this.DataList_timeline = list;
        this.mContext = context;
        imageLoader = new ImageLoader(mContext);
        hs = new HashMap<>();
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri, new DefaultHttpDataSourceFactory("exoplayer-codelab"),
                new DefaultExtractorsFactory(), null, null);
    }


    private void initializePlayer(timelineitemrow_holder holder, int po) {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(mContext),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            holder.simple.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
        }
        //HttpProxyCacheServer proxy = app.getProxy(mContext);
        //String proxyUrl = proxy.getProxyUrl(String.valueOf(Uri.parse(url)));
        MediaSource mediaSource = buildMediaSource(Uri.parse(hs.get("img" + po)));
        player.prepare(mediaSource, true, false);
        holder.simple.setPadding(5, 5, 0, 0);
        holder.simple.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 400));
        holder.frameLayout.addView(holder.simple);
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return position;
    }

    @Override
    public void onBindViewHolder(final timelineitemrow_holder holder, int pos) {
        int position = getItemViewType(pos);
        final Integer usepos = pos;

        final Timeline_DATA obj = DataList_timeline.get(pos);
        holder.name.setText(obj.getName());
        holder.like.setText(Integer.valueOf(obj.getLikes()).toString());
        holder.comment.setText("comments " + Integer.valueOf(obj.getNocmts()).toString());
        holder.des.setText(obj.getPtext());
        imageLoader.DisplayImage(obj.getUserimg(), holder.userimg);
        String a1[] = obj.getPostId().toString().split(" ");

        holder.Id.setText(a1[0]);

        if (a1[1].equals("1")) {
            holder.thumbup.setImageDrawable(mContext.getResources().getDrawable(R.drawable.liked));
        }

        holder.thumbup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = holder.Id.getText().toString();
                String user = openingActivity.ps.getString("username", "p/a");
                useholder = holder;
                castlike(id, user, usepos, obj);
            }
        });

        int id = 0;
        ArrayList<String> images = new ArrayList<>();
        if (!obj.getImg5().isEmpty()) id = 5;
        else if (!obj.getImg4().isEmpty()) id = 4;
        else if (!obj.getImg3().isEmpty()) id = 3;
        else if (!obj.getImg2().isEmpty()) id = 2;
        else if (!obj.getImg1().isEmpty()) id = 1;
        else id = 0;
        if (!obj.getImg1().isEmpty()) images.add(obj.getImg1());
        if (!obj.getImg2().isEmpty()) images.add(obj.getImg2());
        if (!obj.getImg3().isEmpty()) images.add(obj.getImg3());
        if (!obj.getImg4().isEmpty()) images.add(obj.getImg4());
        if (!obj.getImg5().isEmpty()) images.add(obj.getImg5());
        //Exo Player View Show your magic
        //Pro Skills
        // Toast.makeText(mContext, id+" "+holder.Id.getText(), Toast.LENGTH_LONG).show();

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if (!obj.getVideo().isEmpty() && !obj.getVideo().equals("null")) {
            holder.frameLayout.removeAllViews();

            holder.simple.getVideoSurfaceView();
            //app=new App();
            hs.put("img" + position, obj.getVideo());

            initializePlayer(holder, position);

        } else {

            holder.frameLayout.removeAllViews();
            if (id == 1) {


                holder.imageView1.setPadding(0, 5, 0, 0);
                holder.imageView1.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageLoader.DisplayImage(obj.getImg1(), holder.imageView1);
                holder.frameLayout.addView(holder.imageView1);
                // imageLoader.DisplayImage(obj.getImg1(), holder.imageView1);

            }
            if (id == 2) {


                holder.imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2 - 5, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.imageView2.setX(width / 2 - 5);
                holder.imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.imageView1.setPadding(0, 5, 0, 0);
                holder.imageView2.setPadding(5, 5, 0, 0);
                imageLoader.DisplayImage(obj.getImg1(), holder.imageView1);
                holder.frameLayout.addView(holder.imageView1);
                imageLoader.DisplayImage(obj.getImg2(), holder.imageView2);
                holder.frameLayout.addView(holder.imageView2);
//                imageLoader.DisplayImage(obj.getImg1(), holder.imageView1);
//                imageLoader.DisplayImage(obj.getImg2(), holder.imageView2);


            }
            if (id == 3) {

                holder.imageView1.setPadding(0, 5, 0, 0);
                holder.imageView2.setPadding(5, 5, 0, 0);
                holder.imageView3.setPadding(5, 5, 0, 0);


                holder.imageView2.setX(width / 2 - 5);

                imageLoader.DisplayImage(obj.getImg1(), holder.imageView1);


                holder.imageView3.setX(width / 2 - 5);


                holder.imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2 - 5,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2 - 5,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                imageLoader.DisplayImage(obj.getImg2(), holder.imageView2);
                holder.imageView3.setY(holder.imageView1.getHeight() / 2);
                holder.imageView3.setLayoutParams(new FrameLayout.LayoutParams(width / 2 - 5,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                imageLoader.DisplayImage(obj.getImg3(), holder.imageView3);
                //   Toast.makeText(mContext, Integer.toString(holder.imageView1.getHeight()), Toast.LENGTH_SHORT).show();
                holder.frameLayout.addView(holder.imageView1);
                holder.frameLayout.addView(holder.imageView2);
                holder.frameLayout.addView(holder.imageView3);

                imageLoader.DisplayImage(obj.getImg1(), holder.imageView1);
                imageLoader.DisplayImage(obj.getImg2(), holder.imageView2);
                imageLoader.DisplayImage(obj.getImg3(), holder.imageView3);

            }
            if (id >= 4) {

                holder.imageView1.setPadding(0, 5, 0, 0);
                holder.imageView2.setPadding(2, 5, 0, 0);
                holder.imageView3.setPadding(2, 5, 0, 0);
                holder.imageView4.setPadding(2, 5, 0, 0);
                holder.imageView2.setX(width / 2 - 5);

                holder.imageView1.setAdjustViewBounds(true);
                holder.imageView2.setAdjustViewBounds(true);
                holder.imageView3.setAdjustViewBounds(true);
                holder.imageView4.setAdjustViewBounds(true);

                holder.imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2 - 5,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.imageView3.setY(holder.imageView1.getHeight() / 3);
                holder.imageView4.setY(holder.imageView1.getHeight() * 2 / 3);
                holder.imageView3.setX(width / 2);

                holder.imageView4.setX(width / 2);
                holder.imageView4.setLayoutParams(new FrameLayout.LayoutParams(width / 2 - 5,
                        holder.imageView1.getHeight() / 3));
                holder.imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2 - 5,
                        holder.imageView1.getHeight() / 3));
                holder.imageView3.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                        holder.imageView1.getHeight() / 3));
                imageLoader.DisplayImage(obj.getImg1(), holder.imageView1);
                imageLoader.DisplayImage(obj.getImg2(), holder.imageView2);
                imageLoader.DisplayImage(obj.getImg3(), holder.imageView3);
                imageLoader.DisplayImage(obj.getImg4(), holder.imageView4);

                holder.frameLayout.addView(holder.imageView1);
                holder.frameLayout.addView(holder.imageView2);
                holder.frameLayout.addView(holder.imageView3);
                holder.frameLayout.addView(holder.imageView4);


            }

        }

    }


    @Override
    public int getItemCount() {
        return DataList_timeline.size();
    }

    public class timelineitemrow_holder extends RecyclerView.ViewHolder {
        TextView des, like, comment, name, Id;
        FrameLayout frameLayout;
        ImageView userimg;
        DynamicImageView imageView1, imageView2, imageView3, imageView4;
        SimpleExoPlayerView simple;
        ImageButton thumbup;

        RecyclerView stgg;

        public timelineitemrow_holder(View itemView) {
            super(itemView);
            des = (TextView) itemView.findViewById(R.id.posttext);
            like = (TextView) itemView.findViewById(R.id.likes);
            comment = (TextView) itemView.findViewById(R.id.comments);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.frame);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);
            name = (TextView) itemView.findViewById(R.id.dp);
            thumbup = (ImageButton) itemView.findViewById(R.id.like);
            stgg = new RecyclerView(mContext);
            imageView1 = new DynamicImageView(mContext);
            imageView2 = new DynamicImageView(mContext);
            imageView3 = new DynamicImageView(mContext);
            imageView4 = new DynamicImageView(mContext);
            simple = new SimpleExoPlayerView(mContext);
            Id = new TextView(mContext);
            imageView1.setDrawingCacheEnabled(true);
            imageView2.setDrawingCacheEnabled(true);
            imageView3.setDrawingCacheEnabled(true);
            imageView4.setDrawingCacheEnabled(true);
            //simpleExoPlayerView=(SimpleExoPlayerView)itemView.findViewById(R.id.exopro);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int k = Integer.parseInt(Id.getText().toString());

                    Intent i = new Intent(view.getContext(), postDetail.class);
                    i.putExtra("postid", k);
                    view.getContext().startActivity(i);

                }
            });
        }
    }


    public void castlike(final String idd, String user, final int usepos, final Timeline_DATA obj) {
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
                        //      Toast.makeText(mContext, "Liked", Toast.LENGTH_LONG).show();
                        int curlikes = Integer.parseInt(useholder.like.getText().toString());
                        String newvote = "" + (curlikes + 1);
                        useholder.like.setText(newvote);
                        obj.setPostId(idd + " 1");
                        obj.setLikes(curlikes + 1);
                        DataList_timeline.set(usepos, obj);
                        useholder.thumbup.setImageDrawable(mContext.getResources().getDrawable(R.drawable.liked));
                    } else if (result.equals("two")) {
                        //    Toast.makeText(mContext, "Unliked", Toast.LENGTH_LONG).show();
                        int curlikes = Integer.parseInt(useholder.like.getText().toString());
                        String newvote = "" + (curlikes - 1);
                        useholder.like.setText(newvote);
                        obj.setPostId(idd + "0");
                        obj.setLikes(curlikes - 1);
                        DataList_timeline.set(usepos, obj);
                        useholder.thumbup.setImageDrawable(mContext.getResources().getDrawable(R.drawable.like));
                    }
                }
            }

            @Override
            protected void onCancelled() {

                //progressDialog.dismiss();
            }
        }

        new likeTask().execute(idd, user);
    }


}
