package com.cse.csenitd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cse.csenitd.ACHIEVEMENTS.Add_achievement;


public class openingActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Intent intent = new Intent(this, Add_achievement.class);
            startActivity(intent);
            finish();
        }
    }