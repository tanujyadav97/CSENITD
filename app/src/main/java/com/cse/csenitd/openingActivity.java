package com.cse.csenitd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.cse.csenitd.ACHIEVEMENTS.Acheivements;
import com.cse.csenitd.ACHIEVEMENTS.Add_achievement;
import com.cse.csenitd.NoticeBoard.Notices;
import com.cse.csenitd.Timeline.Timeline;
import com.cse.csenitd.question.questionsActivity;


public class openingActivity extends AppCompatActivity {
        public static SharedPreferences ps;
        public static SharedPreferences.Editor pe;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ps=getPreferences(0);
            pe=ps.edit();
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


            Intent in=new Intent(openingActivity.this,questionsActivity.class);
            startActivity(in);
        }
    }