package com.cse.csenitd.NoticeBoard;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.cse.csenitd.Adapters.adapter_acheivement;
import com.cse.csenitd.Adapters.adapter_notice;
import com.cse.csenitd.Data.Acheivements_DATA;
import com.cse.csenitd.Data.Notices_DATA;
import com.cse.csenitd.DbHelper.Notice_Display;
import com.cse.csenitd.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 23-06-2017.Mohit yadav
 */

public class Notices extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Notices_DATA>>{
    RecyclerView mrecycler;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView.Adapter adapter;
    public static final String nurl="https://nitd.000webhostapp.com/cse%20nitd/mohit/getNotices.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ArrayList<String> dta =new ArrayList<>();
//        dta.add("No need to parse string colors in your code If you want to hardcode color values in your ");
//        dta.add("So you should check which kind ");
//
//        dta.add("The color tool helps you create, share, and apply color palettes to your UI, as well as measure the accessibility level of any color combination.");
//        dta.add(" and lighter variations of your \n" +
//                "primary and secondary colors.");
//        dta.add("Check if text is accessible on different-colored backgrounds, \n" +
//                "as measured using the Web Content Accessibility Guidelines \n" +
//                "legibility standards.\n" +
//                "mohit yadav");
        getLoaderManager().initLoader(0,null,this);
        mrecycler=(RecyclerView)findViewById(R.id.noticeRecycleView);

        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecycler.setLayoutManager(staggeredGridLayoutManager);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public Loader<ArrayList<Notices_DATA>> onCreateLoader(int i, Bundle bundle) {
        return new Notice_Display(this,nurl);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Notices_DATA>> loader, ArrayList<Notices_DATA> data) {
        if (data != null && !data.isEmpty()) {
//            listAdaptr.addAll(earthquakes);
            //pgbar.setVisibility(View.INVISIBLE);
            updateUi(data);
        }
    }
    private void updateUi(ArrayList<Notices_DATA> data) {
        adapter=new adapter_notice(this,data);
        mrecycler.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Notices_DATA>> loader) {

    }
}
