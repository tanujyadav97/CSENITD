package com.cse.csenitd.question;

/**
 * Created by 15121 on 6/22/2017.
 */

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse.csenitd.R;
import com.cse.csenitd.openingActivity;
import com.cse.csenitd.question.quesdetail.questiondetailActivity;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Belal on 10/29/2015.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    List<ListItem> items;

    public CardAdapter(String[] time, String[] vote,String[] topic,String[] ques,String[] tag,String[] username,String[] accepted){
        super();
        items = new ArrayList<ListItem>();
        for(int i =0; i<time.length; i++){
            ListItem item = new ListItem();
            item.settime(time[i]);
            item.setvotes(vote[i]);
            item.settopic(topic[i]);
            item.setques(ques[i]);
            item.settags(tag[i]);
            item.setusername(username[i]);
            item.setaccepted(accepted[i]);
            items.add(item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem list =  items.get(position);
        holder.votes.setText(list.getvotes());
        holder.ques.setText(list.gettopic());
        holder.tag.setText(list.gettags());
        holder.timee.setText(getDate(Long.parseLong(list.gettime())));
        holder.id.setText(list.gettime()+"~~~"+list.getusername());
        if(list.getaccepted().equals("1")) {
            holder.votes.setTextColor(Color.parseColor("#FFFFFF"));
            holder.votes.setBackgroundResource(R.drawable.votes1);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public Button votes;
        public TextView ques;
        public TextView tag;
        public TextView timee;
        public TextView id;

        public ViewHolder(final View itemView) {
            super(itemView);

            votes = (Button) itemView.findViewById(R.id.vote);
            timee = (TextView) itemView.findViewById(R.id.time);
            ques = (TextView) itemView.findViewById(R.id.ques);
            tag = (TextView) itemView.findViewById(R.id.tag);
            id = (TextView) itemView.findViewById(R.id.timestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String s[];
                    s=id.getText().toString().split("~~~");
                    openingActivity.pe.putString("quesid",s[0]);
                    openingActivity.pe.commit();

                    openingActivity.pe.putString("quesusername",s[1]);
                    openingActivity.pe.commit();

                    v.getContext().startActivity(new Intent(v.getContext(),questiondetailActivity.class));
                }
            });

        }

    }
    public  String getDate(long timestamp) {
        //     Toast.makeText(questiondetailActivity.this, ""+timestamp, Toast.LENGTH_LONG).show();
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
            sdf.setTimeZone(tz);


            return sdf.format(new Date(timestamp * 1000));
        }catch (Exception e) {
        }
        return "";
    }

}
