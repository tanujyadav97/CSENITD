package com.cse.csenitd.ACHIEVEMENTS;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cse.csenitd.DbHelper.Achievements_Insert;
import com.cse.csenitd.R;

import java.util.zip.Inflater;

/**
 * Created by lenovo on 20-06-2017. Mohit yadav
 */

public class Add_achievement extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private EditText mPtext;
    private ImageView mpImg;
    private Button mbutton;
    public String edes;
    public static final int ACHIEVEMENT_LOADER_ID=1;
    public static final String furl="https://danrist.000webhostapp.com/app/insert.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_achievements);
        mpImg=(ImageView)findViewById(R.id.pimg);
        mPtext=(EditText)findViewById(R.id.ptext);
        mbutton=(Button)findViewById(R.id.buttonImg);
       // progressBar=(ProgressBar)findViewById(R.id.progress);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_achievements,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.post)
        {   edes=mPtext.getText().toString().trim();
            getLoaderManager().initLoader(ACHIEVEMENT_LOADER_ID,null,this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        return new Achievements_Insert(this,furl,edes);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        if(!s.isEmpty())
        {
            finish();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
