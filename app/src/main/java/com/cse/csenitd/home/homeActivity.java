package com.cse.csenitd.home;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cse.csenitd.ACHIEVEMENTS.Acheivements;
import com.cse.csenitd.NoticeBoard.Notices;
import com.cse.csenitd.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cse.csenitd.Timeline.TimelineP;
import com.cse.csenitd.Users.user;
import com.cse.csenitd.host.hostclass;
import com.cse.csenitd.openingActivity;
import com.cse.csenitd.profile1;
import com.cse.csenitd.question.questionsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class homeActivity extends AppCompatActivity {
    private ArrayList<Image> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private static final String endpoint = "https://nitd.000webhostapp.com/cse%20nitd/gallery.json";
    Timer timer;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    int page = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        images = new ArrayList<Image>();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Toast.makeText(homeActivity.this, openingActivity.ps.getString("username","n/a"), Toast.LENGTH_LONG).show();



        fetchImages();
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    private void fetchImages() {


        JsonArrayRequest req = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("home", response.toString());


                        images.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Image image = new Image();
                                image.setName(object.getString("name"));

                                JSONObject url = object.getJSONObject("url");
                                image.setSmall(url.getString("small"));
                                image.setMedium(url.getString("medium"));
                                image.setLarge(url.getString("large"));
                                image.setTimestamp(object.getString("timestamp"));

                                images.add(image);

                            } catch (JSONException e) {
                                Log.e("home", "Json parsing error: " + e.getMessage());
                            }
                        }
                        myViewPagerAdapter = new MyViewPagerAdapter();
                        viewPager.setAdapter(myViewPagerAdapter);
                        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
                        // if(images.size()==0)
                        //    Toast.makeText(getActivity(), "Unable to fetch images", Toast.LENGTH_SHORT).show();

                        viewPager.setCurrentItem(1);
                        pageSwitcher(1);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("home", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);

    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            page=position;
            if(position==0) {
                viewPager.setCurrentItem(images.size()-2);
                 page=images.size()-2;
            }else if(position==images.size()-1) {
                viewPager.setCurrentItem(1);
            page=1;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
          //  Toast.makeText(homeActivity.this, "scrolled."+arg0+" "+arg1, Toast.LENGTH_LONG).show();

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {


        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

           // Toast.makeText(homeActivity.this, "scrolled."+position, Toast.LENGTH_LONG).show();
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            Image image = images.get(position);

            Glide.with(homeActivity.this).load(image.getLarge())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }




    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 1, seconds * 1000); // delay
        // in
        // milliseconds
    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {

                        viewPager.setCurrentItem(page++);

                }
            });

        }
    }

    public void openques(View v)
    {
        Intent in=new Intent(homeActivity.this,questionsActivity.class);
        startActivity(in);
    }

    public void opennotice(View v)
    {
        Intent in=new Intent(homeActivity.this,Notices.class);
        startActivity(in);
    }

    public void openachievement(View v)
    {
        Intent in=new Intent(homeActivity.this,Acheivements.class);
        startActivity(in);
    }

    public void opentimeline(View v)
    {
        Intent in=new Intent(homeActivity.this,TimelineP.class);
        startActivity(in);
    }

    public void openusers(View v)
    {
        Intent in=new Intent(homeActivity.this,user.class);
        startActivity(in);
    }

    public void openhost(View v)
    {
        Intent in=new Intent(homeActivity.this,hostclass.class);
        startActivity(in);
    }

    public void openmyprofile(View v)
    {
        Intent in=new Intent(homeActivity.this,profile1.class);
        startActivity(in);
    }
}
