package com.cse.csenitd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cse.csenitd.Behaviours.DynamicImageView;
import com.cse.csenitd.Data.Comment_DATA;
import com.cse.csenitd.DbHelper.ImageLoader;
import com.cse.csenitd.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 30-06-2017.Moh
 */

public class adapter_comment extends RecyclerView.Adapter<adapter_comment.commentRowHolder> {
    ArrayList<Comment_DATA> mlist;
    Context context;
    ImageLoader imageLoader;
    public adapter_comment( Context context,ArrayList<Comment_DATA> mlist) {
        this.mlist = mlist;
        this.context = context;
        imageLoader=new ImageLoader(context);
    }

    @Override
    public adapter_comment.commentRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comments,parent, false);
        return new adapter_comment.commentRowHolder(v);
    }

    @Override
    public void onBindViewHolder(adapter_comment.commentRowHolder holder, int position) {
        Comment_DATA obj=mlist.get(position);
        holder.name.setText(obj.getCommenterName());
        holder.comment.setText(obj.getComment());
        holder.date.setText(obj.getDate());
        imageLoader.DisplayImage(obj.getCommenterImg(),holder.commenterimg);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    public class commentRowHolder extends RecyclerView.ViewHolder{
        TextView name,comment,date;
        ImageView commenterimg;
        public commentRowHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.dp);
            comment=(TextView)itemView.findViewById(R.id.posttext);
            date=(TextView)itemView.findViewById(R.id.time);
            commenterimg=(ImageView)itemView.findViewById(R.id.userimg);
        }
    }
}
