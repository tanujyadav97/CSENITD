package com.cse.csenitd.ACHIEVEMENTS;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.cse.csenitd.Adapters.adapter_acheivement;
import com.cse.csenitd.Data.Acheivements_DATA;
import com.cse.csenitd.DbHelper.Achievements_Display;
import com.cse.csenitd.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 19-06-2017.Mohit yadav
 */

public class Acheivements extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Acheivements_DATA>>{
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private static final String JSON_RESPONSE="https://nitd.000webhostapp.com/cse%20nitd/mohit/retrieveAchievements.php";
    ProgressBar pgbar;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
         mRecyclerView=(RecyclerView)findViewById(R.id.acheivemnts_recycler_view);
        mLinearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        getLoaderManager().initLoader(0,null,this);

    }

    @Override
    public Loader<ArrayList<Acheivements_DATA>> onCreateLoader(int i, Bundle bundle) {
        return new Achievements_Display(this,JSON_RESPONSE);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Acheivements_DATA>> loader, ArrayList<Acheivements_DATA> data) {
        if (data != null && !data.isEmpty()) {
//            listAdaptr.addAll(earthquakes);
            //pgbar.setVisibility(View.INVISIBLE);
            updateUi(data);
        }
    }

    private void updateUi(ArrayList<Acheivements_DATA> data) {
        mAdapter=new adapter_acheivement(this,data);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Acheivements_DATA>> loader) {

    }
}
