package com.cse.csenitd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cse.csenitd.home.homeActivity;


public class openingActivity extends AppCompatActivity {
    public static SharedPreferences ps;
    public static SharedPreferences.Editor pe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ps = getPreferences(MODE_PRIVATE);
        pe = ps.edit();
        if (ps.getString("username", "n/a").equals("n/a")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            //start with username
            Intent in = new Intent(openingActivity.this, homeActivity.class);
            startActivity(in);
        }

    }
}