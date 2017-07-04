package com.cse.csenitd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;
import com.cse.csenitd.Data.User_DATA;
import com.cse.csenitd.DbHelper.ImageLoader;
import com.cse.csenitd.R;
import com.cse.csenitd.home.Image;

import java.util.ArrayList;

/**
 * Created by lenovo on 02-07-2017.Mohit yadaav
 */

public class adapter_users extends RecyclerView.Adapter<adapter_users.rowholder> implements Filterable{
    private Context mcontext;
    private ArrayList<User_DATA> mList;
    private ArrayList<User_DATA> mFilteredList;

    private ImageLoader imageLoader;
    public adapter_users(Context mcontext, ArrayList<User_DATA> mList) {
        this.mcontext = mcontext;
        this.mList = mList;
        imageLoader=new ImageLoader(mcontext);
        this.mFilteredList=mList;
    }

    @Override
    public adapter_users.rowholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_user,parent, false);
        return new rowholder(v);
    }

    @Override
    public void onBindViewHolder(adapter_users.rowholder holder, int position) {
    User_DATA obj=mFilteredList.get(position);
        holder.name.setText(obj.getName());
        holder.city.setText(obj.getUsername());
        holder.rep.setText(Integer.toString(obj.getRep()));
        imageLoader.DisplayImage(obj.getImg(),holder.userimg);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mList;
                } else {

                    ArrayList<User_DATA> filteredList = new ArrayList<>();

                    for (User_DATA obj : mList) {

                        if (obj.getName().toLowerCase().contains(charString) ||obj.getUsername().toLowerCase().contains(charString)) {

                            filteredList.add(obj);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<User_DATA>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public class rowholder extends RecyclerView.ViewHolder {
        TextView name,city,rep;
        ImageView userimg;
        public rowholder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            city=(TextView)itemView.findViewById(R.id.city);
            rep=(TextView)itemView.findViewById(R.id.rep);
            userimg=(ImageView)itemView.findViewById(R.id.userimg);
        }
    }
}
