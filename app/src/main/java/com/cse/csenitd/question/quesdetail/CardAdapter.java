package com.cse.csenitd.question.quesdetail;

/**
 * Created by 15121 on 6/24/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.cse.csenitd.profile1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.cse.csenitd.LoginActivity.CONNECTION_TIMEOUT;
import static com.cse.csenitd.LoginActivity.READ_TIMEOUT;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    List<ListItem> items;
    Context context;
    Integer acceptedindex=-1;

    public CardAdapter(Context context,String[] time, String[] vote, String[] quesid, String[] ans, String[] ansid, String[] ansby, String[] ansbyname, String[] ansbyrepo, String[] link, String[] accepted, Bitmap[] dp,String[] voted){
        super();
        this.context=context;
        acceptedindex=-1;
        items = new ArrayList<ListItem>();
        for(int i =0; i<time.length; i++){
            ListItem item = new ListItem();
            item.settime(time[i]);
            item.setvotes(vote[i]);
            item.setquesid(quesid[i]);
            item.setans(ans[i]);
            item.setansid(ansid[i]);
            item.setansby(ansby[i]);
            item.setansbyname(ansbyname[i]);
            item.setansbyrepo(ansbyrepo[i]);
            item.setlink(link[i]);
            item.setaccepted(accepted[i]);
            item.setdp(dp[i]);
            item.setvoted(voted[i]);
            items.add(item);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answer_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ListItem list =  items.get(position);
        holder.votes.setText(list.getvotes());
        holder.ans.setText(list.getans());
        holder.link.setText(list.getlink());
        holder.timee.setText("Answered on "+getDate(Long.parseLong(list.gettime())));
        holder.ansid.setText(list.getansid());
        holder.ansby.setText(list.getansby());
        holder.ansbyname.setText(list.getansbyname());
        holder.ansbyrepo.setText(list.getansbyrepo());

        if(list.getvoted().equals("1"))
            holder.upvote.setBackgroundResource(R.drawable.ic_up);
        else if(list.getvoted().equals("-1"))
            holder.downvote.setBackgroundResource(R.drawable.ic_down);

        if(list.getdp()!=null)
        holder.dp.setImageBitmap(list.getdp());
        String quid=openingActivity.ps.getString("quesusername","n/a");
        String uid=openingActivity.ps.getString("username","n/a");
        if(quid.equals(uid)) {
            if (list.getaccepted().equals("1")) {
                acceptedindex=position;
                holder.accepted.setVisibility(View.VISIBLE);
                holder.accept.setVisibility(View.GONE);
            }
            if (list.getaccepted().equals("0")) {
                holder.accept.setVisibility(View.VISIBLE);
                holder.accepted.setVisibility(View.GONE);
            }
        }
        else
        {
            holder.accepted.setVisibility(View.GONE);
            holder.accept.setVisibility(View.GONE);
        }


        holder.dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                openingActivity.pe.putString("useusername",holder.ansby.getText().toString());
                openingActivity.pe.commit();
                Intent in=new Intent(context,profile1.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);

            }
        });

        holder.ansbyname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openingActivity.pe.putString("useusername",holder.ansby.getText().toString());
                openingActivity.pe.commit();
                Intent in=new Intent(context,profile1.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);

            }
        });

        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String anid=holder.ansid.getText().toString();
                String uid=openingActivity.ps.getString("username","n/a");
                String auid=holder.ansby.getText().toString();

                if(uid.equals(auid))
                {   Toast.makeText(context, "You cant vote your own answer!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    castvote(context,holder,anid,uid,auid,"1");
                 //   new ansvoteTask().execute(anid,uid,auid,"1");   // first one forupvote
                    //also handle repo in this task, dont update repo in layout.
                }
            }
        });

        holder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String anid=holder.ansid.getText().toString();
                String uid=openingActivity.ps.getString("username","n/a");
                String auid=holder.ansby.getText().toString();

                if(uid.equals(auid))
                {   Toast.makeText(context, "You cant vote your own answer!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    castvote(context,holder,anid,uid,auid,"-1");
                    //new ansvoteTask().execute(anid,uid,auid,"-1");   // first one forupvote
                    //also handle repo in this task, dont update repo in layout.
                }
            }
        });

        holder.accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String anid=holder.ansid.getText().toString();
                String auid=holder.ansby.getText().toString();

                accept(context,holder,anid,auid,"1",position);
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String anid=holder.ansid.getText().toString();
                String auid=holder.ansby.getText().toString();

                accept(context,holder,anid,auid,"2",position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView votes;
        public TextView ans;
        public TextView link;
        public TextView timee;
        public TextView ansby;
        public TextView ansbyname;
        public TextView ansbyrepo;
        public ImageView dp;
        public TextView ansid;
        public Button upvote;
        public Button downvote;
        public Button accept;
        public Button accepted;


        public ViewHolder(final View itemView) {
            super(itemView);

            link=(TextView)itemView.findViewById(R.id.anslink);
            dp=(ImageView)itemView.findViewById(R.id.dp);
            votes=(TextView)itemView.findViewById(R.id.votes);
            ans=(TextView)itemView.findViewById(R.id.answer);
            ansby=(TextView)itemView.findViewById(R.id.username);
            ansbyname=(TextView)itemView.findViewById(R.id.name);
            ansbyrepo=(TextView)itemView.findViewById(R.id.repo);
            ansid=(TextView)itemView.findViewById(R.id.ansid);
            upvote=(Button)itemView.findViewById(R.id.upvote);
            downvote=(Button)itemView.findViewById(R.id.downvote);
            accept=(Button)itemView.findViewById(R.id.accept);
            accepted=(Button)itemView.findViewById(R.id.accepted);
            timee=(TextView)itemView.findViewById(R.id.date);

          /*  itemView.setOnClickListener(new View.OnClickListener() {
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
            });*/

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


     public void castvote(final Context cont,final ViewHolder h,String a,String b,String c,String d)
     {
         class ansvoteTask extends AsyncTask<String, String, String> {


             HttpURLConnection conn;
             URL url = null;


             @Override
             protected String doInBackground(String... params) {
                 // TODO: attempt authentication against a network service.

                 try {

                     // Enter URL address where your php file resides
                     url = new URL("https://nitd.000webhostapp.com/cse%20nitd/vote.php");  //use same script for both ques and ans

                 } catch (MalformedURLException e) {
                     // TODO Auto-generated catch block

                     e.printStackTrace();
                     return "exception";
                 }
                 try {
                     // Setup HttpURLConnection class to send and receive data from php and mysql
                     conn = (HttpURLConnection)url.openConnection();
                     conn.setReadTimeout(READ_TIMEOUT);
                     conn.setConnectTimeout(CONNECTION_TIMEOUT);
                     conn.setRequestMethod("POST");

                     // setDoInput and setDoOutput method depict handling of both send and receive
                     conn.setDoInput(true);
                     conn.setDoOutput(true);

                     // Append parameters to URL
                     Uri.Builder builder = new Uri.Builder()
                             .appendQueryParameter("id", params[0])
                             .appendQueryParameter("luid", params[1])
                             .appendQueryParameter("puid", params[2])
                             .appendQueryParameter("vote", params[3])
                             .appendQueryParameter("isques", "0");
                     String query = builder.build().getEncodedQuery();

                     // Open connection for sending data
                     OutputStream os = conn.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(
                             new OutputStreamWriter(os, "UTF-8"));
                     writer.write(query);
                     writer.flush();
                     writer.close();
                     os.close();
                     conn.connect();

                 } catch (IOException e1) {
                     // TODO Auto-generated catch block
                     e1.printStackTrace();
                     return "exception";
                 }

                 try {

                     int response_code = conn.getResponseCode();

                     // Check if successful connection made
                     if (response_code == HttpURLConnection.HTTP_OK) {

                         // Read data sent from server
                         InputStream input = conn.getInputStream();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                         StringBuilder result = new StringBuilder();
                         String line;

                         while ((line = reader.readLine()) != null) {
                             result.append(line);
                         }

                         // Pass data to onPostExecute method
                         return(result.toString());

                     }else{

                         return("unsuccessful");
                     }

                 } catch (IOException e) {
                     e.printStackTrace();
                     return "exception";
                 } finally {
                     conn.disconnect();
                 }
             }

             @Override
             protected void onPostExecute(String result) {


                 if (result.equals("false")||result.equals("exception")||result.equals("unsuccessful") ) {

                   Toast.makeText(cont, "OOPs! Unable to cast vote.", Toast.LENGTH_LONG).show();
                 }
                 else
                 {
               /*  case 1: one -  upvoting first time
                   case 2: two-   downvoting first time
                   case 3: three- upvoting again(cancelling)
                   case 4: four-  downvoting again(cancelling)
                   case 5: five-  upvoting after downvoting
                   case 6: six-   downvoting after upvoting
                */
                     if(result.equals("one"))
                     {
                         Toast.makeText(cont, "upvoted", Toast.LENGTH_LONG).show();
                         int curvote=Integer.parseInt(h.votes.getText().toString());
                         String newvote=""+(curvote+1);
                         h.votes.setText(newvote);
                         h.upvote.setBackgroundResource(R.drawable.ic_up);
                     }
                     else if(result.equals("two"))
                     {
                         Toast.makeText(cont, "Downvoted", Toast.LENGTH_LONG).show();
                         int curvote=Integer.parseInt(h.votes.getText().toString());
                         String newvote=""+(curvote-1);
                         h.votes.setText(newvote);
                         h.downvote.setBackgroundResource(R.drawable.ic_down);
                     }
                     else if(result.equals("three"))
                     {
                         Toast.makeText(cont, "Vote cancelled.", Toast.LENGTH_LONG).show();
                         int curvote=Integer.parseInt(h.votes.getText().toString());
                         String newvote=""+(curvote-1);
                         h.votes.setText(newvote);
                         h.upvote.setBackgroundResource(R.drawable.ic_caret_arrow_up);
                     }
                     else if(result.equals("four"))
                     {
                         Toast.makeText(cont, "Vote cancelled.", Toast.LENGTH_LONG).show();
                         int curvote=Integer.parseInt(h.votes.getText().toString());
                         String newvote=""+(curvote+1);
                         h.votes.setText(newvote);
                         h.downvote.setBackgroundResource(R.drawable.ic_caret_down);
                     }
                     else if(result.equals("five"))
                     {
                         Toast.makeText(cont, "upvoted", Toast.LENGTH_LONG).show();
                         int curvote=Integer.parseInt(h.votes.getText().toString());
                         String newvote=""+(curvote+2);
                         h.votes.setText(newvote);
                         h.upvote.setBackgroundResource(R.drawable.ic_up);
                         h.downvote.setBackgroundResource(R.drawable.ic_caret_down);
                     }
                     else if(result.equals("six"))
                     {
                         Toast.makeText(cont, "downvoted", Toast.LENGTH_LONG).show();
                         int curvote=Integer.parseInt(h.votes.getText().toString());
                         String newvote=""+(curvote-2);
                         h.votes.setText(newvote);
                         h.upvote.setBackgroundResource(R.drawable.ic_caret_arrow_up);
                         h.downvote.setBackgroundResource(R.drawable.ic_down);
                     }
                 }

             }

             @Override
             protected void onCancelled() {

                 //progressDialog.dismiss();
             }
         }
         new ansvoteTask().execute(a,b,c,d);

     }


    public void accept(final Context cont,final ViewHolder h,String a,String b,final String c,final int index)
    {
        class acceptTask extends AsyncTask<String, String, String> {


            HttpURLConnection conn;
            URL url = null;


            @Override
            protected String doInBackground(String... params) {
                // TODO: attempt authentication against a network service.

                try {

                    // Enter URL address where your php file resides
                    url = new URL("https://nitd.000webhostapp.com/cse%20nitd/accept.php");  //use same script for both ques and ans

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block

                    e.printStackTrace();
                    return "exception";
                }
                try {
                    // Setup HttpURLConnection class to send and receive data from php and mysql
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setRequestMethod("POST");

                    // setDoInput and setDoOutput method depict handling of both send and receive
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    // Append parameters to URL
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("id", params[0])
                            .appendQueryParameter("puid", params[1])
                            .appendQueryParameter("type", params[2])
                            .appendQueryParameter("quesid", params[3]);
                    String query = builder.build().getEncodedQuery();

                    // Open connection for sending data
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.connect();

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return "exception";
                }

                try {

                    int response_code = conn.getResponseCode();

                    // Check if successful connection made
                    if (response_code == HttpURLConnection.HTTP_OK) {

                        // Read data sent from server
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        // Pass data to onPostExecute method
                        return(result.toString());

                    }else{

                        return("unsuccessful");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return "exception";
                } finally {
                    conn.disconnect();
                }
            }

            @Override
            protected void onPostExecute(String result) {


                if (result.equals("false")||result.equals("exception")||result.equals("unsuccessful") ) {

                    Toast.makeText(cont, "OOPs! Unable to accept.", Toast.LENGTH_LONG).show();
                }
                else
                {

                    if(c.equals("1"))
                    {
                        Toast.makeText(cont, "Accept cancelled", Toast.LENGTH_LONG).show();
                        h.accepted.setVisibility(View.GONE);
                        h.accept.setVisibility(View.VISIBLE);
                        Config.accepteds[index]="0";
                    }
                    else
                    {
                        Toast.makeText(cont, "Accepted", Toast.LENGTH_LONG).show();
                        h.accepted.setVisibility(View.VISIBLE);
                        h.accept.setVisibility(View.GONE);
                        h.accepted.setVisibility(View.VISIBLE);

                        if(acceptedindex!=-1)
                        {
                            Config.accepteds[acceptedindex]="0";
                            Config.accepteds[index]="1";
                            questiondetailActivity.adapter = new CardAdapter(cont,Config.times, Config.votess,Config.quesids,Config.anss,Config.ansids,Config.ansbys,Config.ansbynames,Config.ansbyrepos,Config.links,Config.accepteds,Config.dps,Config.voteds);
                            questiondetailActivity.recyclerView.setAdapter(questiondetailActivity.adapter);

                        }
                        acceptedindex=index;
                        Config.accepteds[index]="1";
                    }
                }

            }

            @Override
            protected void onCancelled() {

                //progressDialog.dismiss();
            }
        }
        new acceptTask().execute(a,b,c,openingActivity.ps.getString("quesid","n/a"));

    }


}

