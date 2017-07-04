package com.cse.csenitd.Users;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cse.csenitd.Adapters.adapter_users;
import com.cse.csenitd.Data.User_DATA;
import com.cse.csenitd.DbHelper.User_Display;
import com.cse.csenitd.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import mehdi.sakout.dynamicbox.DynamicBox;

/**
 * Created by lenovo on 02-07-2017.Mohit yadaav
 */

public class user extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<User_DATA>>{
    MaterialSearchView searchView;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    private adapter_users adapter;
    DynamicBox box;
    private static final String url="https://nitd.000webhostapp.com/cse%20nitd/mohit/getUsers.php";
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
       // searchView= (MaterialSearchView) findViewById(R.id.search_view);
        recyclerView=(RecyclerView)findViewById(R.id.user_rcy);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Users");
        setSupportActionBar(toolbar);
        gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
       box= new DynamicBox(this,recyclerView);
        box.showLoadingLayout();
        box.setLoadingMessage("Loading Users");


        getLoaderManager().initLoader(0,null,this);
//        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //Do some magic
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //Do some magic
//                return false;
//            }
//        });
//
//        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
//            @Override
//            public void onSearchViewShown() {
//                //Do some magic
//            }
//
//            @Override
//            public void onSearchViewClosed() {
//                //Do some magic
//            }
//        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_users, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        search(searchView);

        return true;
    }
    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

               adapter.getFilter().filter(newText);

                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
    private void updateUi(ArrayList<User_DATA> da){
        adapter=new adapter_users(this,da);
        box.hideAll();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public Loader<ArrayList<User_DATA>> onCreateLoader(int i, Bundle bundle) {
        return new User_Display(this,url);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<User_DATA>> loader, ArrayList<User_DATA> user_datas) {
        if(user_datas!=null){
            updateUi(user_datas);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<User_DATA>> loader) {

    }
}
