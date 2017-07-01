package com.cse.csenitd.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.cse.csenitd.question.quesdetail.questiondetailActivity;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

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
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private App app;
    public static int k = 0;

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
    public void onBindViewHolder(timelineitemrow_holder holder, int pos) {
        int position = getItemViewType(pos);
        Timeline_DATA obj = DataList_timeline.get(position);
        holder.name.setText(obj.getName());
        holder.like.setText(Integer.valueOf(obj.getLikes()).toString());
        holder.des.setText(obj.getPtext());
        holder.Id.setText(Integer.valueOf(obj.getPostId()).toString());
        int id = 0;
        if (!obj.getImg5().isEmpty()) id = 5;
        else if (!obj.getImg4().isEmpty()) id = 4;
        else if (!obj.getImg3().isEmpty()) id = 3;
        else if (!obj.getImg2().isEmpty()) id = 2;
        else if (!obj.getImg1().isEmpty()) id = 1;
        else id = 0;
        //Exo Player View Show your magic
        //Pro Skills
        if (id == 0) {
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
//                Picasso.with(mContext)
//                        .load(obj.getImg1())
//                        .placeholder(R.drawable.backgroundsplash)
//                        .noFade()
//                        .into(holder.imageView1);
                imageLoader.DisplayImage(obj.getImg1(),holder.imageView1);
                holder.frameLayout.addView(holder.imageView1);
                // imageLoader.DisplayImage(obj.getImg1(), holder.imageView1);

            }
            if (id == 2) {

                holder.imageView1.setPadding(0, 5, 0, 0);
                holder.imageView2.setX(holder.frameLayout.getWidth() / 2);
                holder.imageView2.setPadding(5, 5, 0, 0);
                holder.imageView1.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                        400));
                holder.imageView2.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                        400));
                imageLoader.DisplayImage(obj.getImg1(),holder.imageView1);
                imageLoader.DisplayImage(obj.getImg2(),holder.imageView2);
                holder.frameLayout.addView(holder.imageView1);
                holder.frameLayout.addView(holder.imageView2);
//                imageLoader.DisplayImage(obj.getImg1(), holder.imageView1);
//                imageLoader.DisplayImage(obj.getImg2(), holder.imageView2);


            }
            if (id == 3) {

                holder.imageView1.setPadding(0, 5, 0, 0);
                holder.imageView2.setPadding(5, 5, 0, 0);
                holder.imageView3.setPadding(5, 5, 0, 0);
                holder.imageView2.setX(holder.frameLayout.getWidth() / 2);
                holder.imageView3.setX(holder.frameLayout.getWidth() / 2);
                holder.imageView3.setY(holder.frameLayout.getHeight() / 2);
                imageLoader.DisplayImage(obj.getImg1(),holder.imageView1);

                imageLoader.DisplayImage(obj.getImg2(),holder.imageView2);

                imageLoader.DisplayImage(obj.getImg3(),holder.imageView3);

                holder.imageView1.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.imageView2.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.imageView3.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.frameLayout.addView(holder.imageView1);
                holder.frameLayout.addView(holder.imageView2);
                holder.frameLayout.addView(holder.imageView3);
//                imageLoader.DisplayImage(obj.getImg1(), holder.imageView1);
//                imageLoader.DisplayImage(obj.getImg2(), holder.imageView2);
//                imageLoader.DisplayImage(obj.getImg3(), holder.imageView3);

            }
            if (id >= 4) {

                holder.imageView1.setPadding(0, 5, 0, 0);
                holder.imageView2.setPadding(2, 5, 0, 0);
                holder.imageView3.setPadding(2, 5, 0, 0);
                holder.imageView4.setPadding(2, 5, 0, 0);
                holder.imageView2.setX(holder.frameLayout.getWidth() / 2);
                holder.imageView3.setX(holder.frameLayout.getWidth() / 2);
                holder.imageView3.setY(holder.frameLayout.getHeight() / 3);
                holder.imageView4.setX(holder.frameLayout.getWidth() / 2);
                holder.imageView1.setAdjustViewBounds(true);
                holder.imageView2.setAdjustViewBounds(true);
                holder.imageView3.setAdjustViewBounds(true);
                holder.imageView4.setAdjustViewBounds(true);
                holder.imageView4.setY((holder.frameLayout.getHeight() / 3 + holder.frameLayout.getHeight() / 3));
                holder.imageView1.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.imageView4.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                        holder.imageView1.getHeight() / 3));
                holder.imageView2.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                        holder.imageView1.getHeight() / 3));
                holder.imageView3.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                        holder.imageView1.getHeight() / 3));
                imageLoader.DisplayImage(obj.getImg1(),holder.imageView1);
                imageLoader.DisplayImage(obj.getImg2(),holder.imageView2);
                imageLoader.DisplayImage(obj.getImg3(),holder.imageView3);
                imageLoader.DisplayImage(obj.getImg4(),holder.imageView4);

                holder.frameLayout.addView(holder.imageView1);
                holder.frameLayout.addView(holder.imageView2);
                holder.frameLayout.addView(holder.imageView3);
                holder.frameLayout.addView(holder.imageView4);
//                imageLoader.DisplayImage(obj.getImg1(), holder.imageView1);
//                imageLoader.DisplayImage(obj.getImg2(), imageView2);
//                imageLoader.DisplayImage(obj.getImg3(), imageView3);
//                imageLoader.DisplayImage(obj.getImg4(), holder.imageView4);

            }
        }

    }

    @Override
    public int getItemCount() {
        return DataList_timeline.size();
    }

    public class timelineitemrow_holder extends RecyclerView.ViewHolder {
        TextView des, like, comment, name,Id;
        FrameLayout frameLayout;
        ImageView userimg;
        DynamicImageView imageView1, imageView2, imageView3, imageView4;
        SimpleExoPlayerView simple;

        public timelineitemrow_holder(View itemView) {
            super(itemView);
            des = (TextView) itemView.findViewById(R.id.posttext);
            like = (TextView) itemView.findViewById(R.id.likes);
            comment = (TextView) itemView.findViewById(R.id.comments);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.frame);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);
            name = (TextView) itemView.findViewById(R.id.dp);
            imageView1 = new DynamicImageView(mContext);
            imageView2 = new DynamicImageView(mContext);
            imageView3 = new DynamicImageView(mContext);
            imageView4 = new DynamicImageView(mContext);
            simple=new SimpleExoPlayerView(mContext);
            Id=new TextView(mContext);
            imageView1.setDrawingCacheEnabled(true);
            imageView2.setDrawingCacheEnabled(true);
            imageView3.setDrawingCacheEnabled(true);
            imageView4.setDrawingCacheEnabled(true);
            //simpleExoPlayerView=(SimpleExoPlayerView)itemView.findViewById(R.id.exopro);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int k=Integer.parseInt(Id.getText().toString());

                   Intent i= new Intent(view.getContext(),postDetail.class);
                    i.putExtra("postid",k);
                    view.getContext().startActivity(i);

                }
            });
        }
    }


}
