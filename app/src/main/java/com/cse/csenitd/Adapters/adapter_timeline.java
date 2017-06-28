package com.cse.csenitd.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cse.csenitd.Behaviours.DynamicImageView;
import com.cse.csenitd.Data.Acheivements_DATA;
import com.cse.csenitd.Data.Timeline_DATA;
import com.cse.csenitd.DbHelper.ImageLoader;
import com.cse.csenitd.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 28-06-2017.Mohit yadav
 */

public class adapter_timeline extends RecyclerView.Adapter<adapter_timeline.timelineitemrow_holder> {
    private ArrayList<Timeline_DATA> DataList_timeline;
    public Context mContext;
    ImageLoader imageLoader;
    @Override
    public timelineitemrow_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_timeline,parent, false);
        return new adapter_timeline.timelineitemrow_holder(v);
    }

    public adapter_timeline(Context context,ArrayList<Timeline_DATA> list) {
        this.DataList_timeline=list;
        this.mContext=context;
        imageLoader=new ImageLoader(mContext);
    }

    @Override
    public void onBindViewHolder(timelineitemrow_holder holder, int position) {
        Timeline_DATA obj=DataList_timeline.get(position);
        holder.name.setText(obj.getName());
        holder.like.setText(Integer.valueOf(obj.getLikes()).toString());
        holder.des.setText(obj.getPtext());
        int id=0;
        if(!obj.getImg5().isEmpty()) id=5;
        else if(!obj.getImg4().isEmpty()) id=4;
        else if(!obj.getImg3().isEmpty()) id=3;
        else if(!obj.getImg2().isEmpty()) id=2;
        else if(!obj.getImg1().isEmpty()) id=1;
        else id=0;
        DynamicImageView imageView1=new DynamicImageView(mContext);
        DynamicImageView imageView2=new DynamicImageView(mContext);
        DynamicImageView imageView3=new DynamicImageView(mContext);
        DynamicImageView imageView4=new DynamicImageView(mContext);
        holder.frameLayout.removeAllViews();
        if(id==1)
        {


            imageLoader.DisplayImage(obj.getImg1(),imageView1);
            imageView1.setPadding(0, 5, 0, 0);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.frameLayout.addView(imageView1);
        }
        if(id==2){
            imageLoader.DisplayImage(obj.getImg1(),imageView1);
            imageLoader.DisplayImage(obj.getImg2(),imageView2);
            imageView1.setPadding(0, 5, 0, 0);
            imageView2.setX(holder.frameLayout.getWidth() / 2);
            imageView2.setPadding(5, 5, 0, 0);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth()/2,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth()/2,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.frameLayout.addView(imageView1);
            holder.frameLayout.addView(imageView2);
        }
        if(id==3){
            imageLoader.DisplayImage(obj.getImg1(),imageView1);
            imageLoader.DisplayImage(obj.getImg2(),imageView2);
            imageLoader.DisplayImage(obj.getImg3(),imageView3);
            imageView1.setPadding(0, 5, 0, 0);
            imageView2.setPadding(5, 5, 0, 0);
            imageView3.setPadding(5, 5, 0, 0);
            imageView2.setX(holder.frameLayout.getWidth() / 2);
            imageView3.setX(holder.frameLayout.getWidth() / 2);
            imageView3.setY(holder.frameLayout.getHeight() / 2);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView3.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.frameLayout.addView(imageView1);
            holder.frameLayout.addView(imageView2);
            holder.frameLayout.addView(imageView3);
        }
        if(id>=4) {
            imageLoader.DisplayImage(obj.getImg1(),imageView1);
            imageLoader.DisplayImage(obj.getImg2(),imageView2);
            imageLoader.DisplayImage(obj.getImg3(),imageView3);
            imageLoader.DisplayImage(obj.getImg4(),imageView4);
            imageView1.setPadding(0, 5, 0, 0);
            imageView2.setPadding(2, 5, 0, 0);
            imageView3.setPadding(2, 5, 0, 0);
            imageView4.setPadding(2, 5, 0, 0);
            imageView2.setX(holder.frameLayout.getWidth() / 2);
            imageView3.setX(holder.frameLayout.getWidth() / 2);
            imageView3.setY(holder.frameLayout.getHeight() / 3);
            imageView4.setX(holder.frameLayout.getWidth() / 2);
            imageView1.setAdjustViewBounds(true);
            imageView2.setAdjustViewBounds(true);
            imageView3.setAdjustViewBounds(true);
            imageView4.setAdjustViewBounds(true);
            imageView4.setY((holder.frameLayout.getHeight() / 3 + holder.frameLayout.getHeight() / 3));
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView4.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                    imageView1.getHeight()/3));
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                    imageView1.getHeight()/3));
            imageView3.setLayoutParams(new FrameLayout.LayoutParams(holder.frameLayout.getWidth() / 2,
                    imageView1.getHeight()/3));
            holder.frameLayout.addView(imageView1);
            holder.frameLayout.addView(imageView2);
            holder.frameLayout.addView(imageView3);
            holder.frameLayout.addView(imageView4);
        }

    }

    @Override
    public int getItemCount() {
        return DataList_timeline.size();
    }
    public class timelineitemrow_holder extends RecyclerView.ViewHolder {
        TextView des,like,comment,name;
        FrameLayout frameLayout;
        ImageView userimg;
        public timelineitemrow_holder(View itemView) {
            super(itemView);
            des=(TextView)itemView.findViewById(R.id.posttext);
            like=(TextView)itemView.findViewById(R.id.likes);
            comment=(TextView)itemView.findViewById(R.id.comments);
            frameLayout=(FrameLayout)itemView.findViewById(R.id.frame);
            userimg=(ImageView)itemView.findViewById(R.id.userimg);
            name=(TextView)itemView.findViewById(R.id.dp);
        }
    }


}
