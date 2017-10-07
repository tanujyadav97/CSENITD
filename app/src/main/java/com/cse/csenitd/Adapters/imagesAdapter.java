package com.cse.csenitd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cse.csenitd.DbHelper.ImageLoader;
import com.cse.csenitd.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 09-07-2017.Mohit yadav
 */

public class imagesAdapter extends RecyclerView.Adapter<imagesAdapter.gridholder> {
    private Context mContext;
    private ArrayList<String> listimages;
    ImageLoader imageLoader;
    public imagesAdapter(Context mContext, ArrayList<String> listimages) {
        this.mContext = mContext;
        this.listimages = listimages;
        imageLoader=new ImageLoader(mContext);
    }

    @Override
    public imagesAdapter.gridholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_row,parent, false);
        return new imagesAdapter.gridholder(v);
    }

    @Override
    public void onBindViewHolder(imagesAdapter.gridholder holder, int position) {
            imageLoader.DisplayImage(listimages.get(position),holder.imageview);
    }

    @Override
    public int getItemCount() {
        return listimages.size();
    }
    public class gridholder extends RecyclerView.ViewHolder{
        ImageView imageview;
        public gridholder(View itemView) {
            super(itemView);
            imageview=(ImageView)itemView.findViewById(R.id.img);
        }
    }
}
