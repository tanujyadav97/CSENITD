package com.cse.csenitd.ACHIEVEMENTS;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cse.csenitd.DbHelper.Achievements_Insert;
import com.cse.csenitd.R;

import java.io.IOException;
import java.util.zip.Inflater;

import static android.R.attr.bitmap;
import static com.cse.csenitd.R.id.imageView;

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
    public static final int  PICK_IMAGE_REQUEST=1;
    private Bitmap bitmap;
    private Uri filePath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_achievements);
        mpImg=(ImageView)findViewById(R.id.pimg);
        mPtext=(EditText)findViewById(R.id.ptext);
        mbutton=(Button)findViewById(R.id.buttonImg);
       // progressBar=(ProgressBar)findViewById(R.id.progress);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showFileChooser();
            }
        });
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mpImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        {
            edes=mPtext.getText().toString().trim();

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
