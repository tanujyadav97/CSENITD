package com.cse.csenitd.Timeline;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cse.csenitd.Adapters.adapter_acheivement;
import com.cse.csenitd.Adapters.adapter_timeline;
import com.cse.csenitd.Data.Acheivements_DATA;
import com.cse.csenitd.Data.Timeline_DATA;
import com.cse.csenitd.DbHelper.Timeline_Display;
import com.cse.csenitd.R;

import java.util.ArrayList;


/**
 * Created by lenovo on 27-06-2017.Mohit yadav
 */

public class TimelineP extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Timeline_DATA>>{
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private RecyclerView.Adapter madapter;
    private static final String JSON_RESPONSE="https://nitd.000webhostapp.com/cse%20nitd/mohit/getTimeline.php";
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Timeline_DATA> rst;
    FloatingActionButton add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        recyclerView=(RecyclerView)findViewById(R.id.timeline_recyclerView);
        add=(FloatingActionButton)findViewById(R.id.add);
        manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        getLoaderManager().initLoader(0,null,this);
        rst=new ArrayList<>();
        mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(TimelineP.this,Add_Timeline.class);
                startActivity(in);
            }
        });

    }

    void refreshItems() {
        // Load items
        // ...
        madapter=new adapter_timeline(this,rst);
        madapter.notifyDataSetChanged();
        recyclerView.setAdapter(madapter);
        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public Loader<ArrayList<Timeline_DATA>> onCreateLoader(int i, Bundle bundle) {
        return new Timeline_Display(this,JSON_RESPONSE);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Timeline_DATA>> loader, ArrayList<Timeline_DATA> data) {
        if (data != null && !data.isEmpty()) {
//            listAdaptr.addAll(earthquakes);
            //pgbar.setVisibility(View.INVISIBLE);
            updateUi(data);
        }
    }
    private void updateUi(ArrayList<Timeline_DATA> data) {
        madapter=new adapter_timeline(this,data);
        madapter.notifyDataSetChanged();
        rst=data;
        recyclerView.setAdapter(madapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Timeline_DATA>> loader) {

    }
}
