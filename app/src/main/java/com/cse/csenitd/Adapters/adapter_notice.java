package com.cse.csenitd.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse.csenitd.Data.Notices_DATA;
import com.cse.csenitd.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lenovo on 23-06-2017.Mohit yadav
 */

public class adapter_notice extends RecyclerView.Adapter<adapter_notice.gridRowHolder> {
    private Context mContext;

    private ArrayList<Notices_DATA> data;
    public adapter_notice(Context context,ArrayList<Notices_DATA> Data) {
        this.data=Data;
        this.mContext=context;
    }

    @Override
    public adapter_notice.gridRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notice,parent, false);

        return new adapter_notice.gridRowHolder(v);
    }
    protected int getRandomIntInRange(int max, int min){
        Random r = new Random();
        int i1 = r.nextInt(max-min)+min;
        return i1;
    }
    @Override
    public void onBindViewHolder(adapter_notice.gridRowHolder holder, int position) {
        String c=chooseColor();
        int cl= Color.parseColor(c);
        holder.notice.setBackgroundColor(cl);
        holder.notice.setText(data.get(position).get_post());
        holder.notice.setTextColor(Color.WHITE);
        holder.name.setText(data.get(position).get_name());
        holder.name.setBackgroundColor(cl);
        holder.date.setText(data.get(position).get_dtm());
        holder.date.setBackgroundColor(cl);
        holder.ll.setBackgroundColor(cl);
    }

    private String chooseColor() {
       String [] color={"#FF6E40","#607D8B","#FF5722","#757575","#00ACC1","#009688","#8E24AA"};
        Random r = new Random();
        int i1 = r.nextInt(7);
        return color[i1];
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class gridRowHolder extends RecyclerView.ViewHolder {
        TextView notice,name,date;
        LinearLayout ll;
        public gridRowHolder(View itemView) {
            super(itemView);
            notice=(TextView) (itemView).findViewById(R.id.notice);
            name=(TextView)itemView.findViewById(R.id.user);
            date=(TextView)itemView.findViewById(R.id.date);
            ll =(LinearLayout)itemView.findViewById(R.id.ll);
        }
    }
}
