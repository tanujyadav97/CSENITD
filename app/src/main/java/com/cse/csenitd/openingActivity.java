package com.cse.csenitd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cse.csenitd.ACHIEVEMENTS.Acheivements;
import com.cse.csenitd.ACHIEVEMENTS.Add_achievement;
import com.cse.csenitd.NoticeBoard.Notices;
import com.cse.csenitd.Timeline.Timeline;


public class openingActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            Intent intent = new Intent(this, Timeline.class);
            startActivity(intent);
            finish();
        }
    }