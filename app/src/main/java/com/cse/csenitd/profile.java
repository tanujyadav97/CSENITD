package com.cse.csenitd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class profile extends AppCompatActivity {
    //profile
    String a[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        a= getResources().getStringArray(R.array.desig);
        ListView listView = (ListView)findViewById( R.id.knowntech );
        //final ArrayList<String> listItems = new ArrayList<String>();
        //listItems.add
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, a );
        listView.setAdapter( adapter );

    }
}
