package com.cse.csenitd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cse.csenitd.Data.Acheivements_DATA;
import com.cse.csenitd.DbHelper.ImageLoader;
import com.cse.csenitd.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 19-06-2017. Mohit yadav
 */

public class adapter_acheivement extends RecyclerView.Adapter<adapter_acheivement.ItemrowHolder> {
    private ArrayList<Acheivements_DATA> DataList_ach;
    public Context mContext;
    ImageLoader imageLoader;
    public adapter_acheivement(Context context, ArrayList<Acheivements_DATA> list) {
        this.mContext = context;
        this.DataList_ach = list;
        imageLoader=new ImageLoader(mContext);
    }

    @Override
    public ItemrowHolder onCreateViewHolder(ViewGroup  parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_achievements,parent, false);
        return new ItemrowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemrowHolder holder, int position) {
        Acheivements_DATA obj = DataList_ach.get(position);
        holder.desciption.setText(obj.get_des());
//        Bitmap bts//=convertInBitmap(obj.get_urlString());
//        holder.imageView.setImageResource();
//        Bitmap usr=convertInBitmap(obj.get_UserName());
//        holder.UserImg.setImageBitmap(usr);
        holder.Likes.setText(Integer.valueOf(obj.get_likes()).toString());
        holder.postby.setText(obj.get_UserName());
        holder.datetime.setText(obj.getDate());
        imageLoader.DisplayImage(obj.get_urlString(), holder.imageView);
        holder.usernm.setText(obj.get_UserName());
        holder.postby.setText(obj.get_name());
        holder.rep.setText(Integer.valueOf(obj.get_rep()).toString());
        imageLoader.DisplayImage(obj.getUserImag(),holder.UserImg);
        holder.title.setText(obj.get_title());
    }

    @Override
    public int getItemCount() {
        return DataList_ach.size();
    }

    public class ItemrowHolder extends RecyclerView.ViewHolder {
        public TextView desciption, datetime, postby, Likes, title,usernm,rep;
        public ImageView imageView, UserImg;

        public ItemrowHolder(View itemView) {
            super(itemView);
            desciption = (TextView) itemView.findViewById(R.id.des);
           imageView = (ImageView) itemView.findViewById(R.id.imageView);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
            postby = (TextView) itemView.findViewById(R.id.postby);
            Likes = (TextView) itemView.findViewById(R.id.noOfLikes);
            UserImg = (ImageView) itemView.findViewById(R.id.poster);
            title=(TextView)itemView.findViewById(R.id.title);
            usernm=(TextView)itemView.findViewById(R.id.usrnm);
            rep=(TextView)itemView.findViewById(R.id.rep);
        }
    }
}
