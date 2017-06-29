package com.cse.csenitd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.cse.csenitd.ACHIEVEMENTS.Acheivements;
import com.cse.csenitd.ACHIEVEMENTS.Add_achievement;
import com.cse.csenitd.NoticeBoard.Notices;
import com.cse.csenitd.Timeline.Add_Timeline;
import com.cse.csenitd.Timeline.TimelineP;
import com.cse.csenitd.question.questionsActivity;
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


public class openingActivity extends AppCompatActivity {
    public static SharedPreferences ps;
    public static SharedPreferences.Editor pe;
    boolean playWhenReady = false;
    int currentWindow;
    long playbackPosition;
    SimpleExoPlayerView simplePro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ps = getPreferences(0);
        pe = ps.edit();
           /* if (ps.getString("username", "n/a").equals("n/a")) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                //start with username
                Intent in=new Intent(openingActivity.this,profile1.class);
                startActivity(in);
            }*/


        Intent in = new Intent(openingActivity.this, Acheivements.class);
        startActivity(in);
        finish();
//            simplePro=new SimpleExoPlayerView(this);
//            simplePro.setVisibility(View.VISIBLE);
//            SimpleExoPlayer player= ExoPlayerFactory.newSimpleInstance(
//                    new DefaultRenderersFactory(this),
//                    new DefaultTrackSelector(), new DefaultLoadControl());
//            simplePro.setPlayer(player);
//
//
//            player.setPlayWhenReady(playWhenReady);
//
//
//            player.seekTo(currentWindow, playbackPosition);
//            MediaSource mediaSource = buildMediaSource(Uri.parse(""));
//            player.prepare(mediaSource, true, false);
//
//        }
//    private MediaSource buildMediaSource(Uri uri) {
//        return new ExtractorMediaSource(uri, new DefaultHttpDataSourceFactory("exoplayer-codelab"),
//                new DefaultExtractorsFactory(), null, null);
//    }
//    @SuppressLint("InlinedApi")
//    private void hideSystemUi() {
//        simplePro.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//    }
    }
}