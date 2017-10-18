package com.cse.csenitd.host;

/**
 * Created by 15121 on 7/1/2017.
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
        import java.sql.Timestamp;
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
   // Integer acceptedindex=-1;

    public CardAdapter(Context context,String[] classid, String[] title, String[] desc, String[] link, String[] tutor, String[] noofpeople,String[] venue,String[] date, String[] postbyname, String[] postbyusername, Bitmap[] dp, String[] attended){
        super();
        this.context=context;
     //   acceptedindex=-1;
        items = new ArrayList<ListItem>();
        for(int i =0; i<classid.length; i++){
            ListItem item = new ListItem();
            item.setclassid(classid[i]);
            item.settitle(title[i]);
            item.setdesc(desc[i]);
            item.setlink(link[i]);
            item.settutor(tutor[i]);
            item.setnoofpeople(noofpeople[i]);
            item.setvenue(venue[i]);
            item.setdate(date[i]);
            item.setpostbyname(postbyname[i]);
            item.setpostbyusername(postbyusername[i]);
            item.setdp(dp[i]);
            item.setattended(attended[i]);
            items.add(item);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hostcard, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ListItem list =  items.get(position);
        holder.title.setText(list.gettitle());
        holder.desc.setText(list.getdesc());
        holder.link.setText(list.getlink());
        holder.date.setText(list.getdate());
        holder.classid.setText(list.getclassid());
        holder.postbyname.setText(list.getpostbyname());
        holder.postbyusername.setText(list.getpostbyusername());
        holder.tutor.setText(list.gettutor());
        holder.noofpeople.setText(list.getnoofpeople());
        holder.venue.setText(list.getvenue());

        if (list.getdate().equals("n/a"))
        {
            holder.attended.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idd=holder.classid.getText().toString();
                    String user=openingActivity.ps.getString("username","n/a");
                    //Toast.makeText(mContext, idd, Toast.LENGTH_LONG).show();
                    castattend(idd,user,position,list,holder);


                }
            });
            if (list.getattended().equals("1")) {
                holder.attended.setBackgroundColor(0xff00dfff);
                holder.attended.setText("ATTENDING");

            }
        }
          else {
            String date[] = list.getdate().split(" ");

            int ho = Integer.parseInt(date[0].split(":")[0]);
            int min = Integer.parseInt(date[0].split(":")[1]);
            int isam = date[1].equals("am") ? 1 : 0;
            int day = Integer.parseInt(date[3].split(",")[0]);
            int year = Integer.parseInt(date[3].split(",")[1]);
            int mon = getmon(date[2]);
            if (isam == 0)
                ho += 12;
            String dd = ho + ":" + min + " " + day + "/" + mon + "/" + year;

            DateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yyyy");
            Date datee = null,curdate;
            curdate=new Date();
            try {
                datee = dateFormat.parse(dd);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            //long time = datee.getTime();
            //Long timestamp = Long.parseLong((new Timestamp(time)).toString());
            //Toast.makeText(context, "" + timestamp, Toast.LENGTH_LONG).show();

            if (datee.after(curdate)) {
                if (list.getattended().equals("1")) {
                    holder.attended.setBackgroundColor(0xff00dfff);
                    holder.attended.setText("ATTENDING");
                    holder.attended.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String idd=holder.classid.getText().toString();
                            String user=openingActivity.ps.getString("username","n/a");
                            //Toast.makeText(mContext, idd, Toast.LENGTH_LONG).show();
                            castattend(idd,user,position,list,holder);


                        }
                    });
                }
            } else {
                holder.attended.setBackgroundColor(0xff808080);
                holder.attended.setClickable(false);
                holder.etattend.setText("PEOPLE ATTENDED :");
            }

        }

        if(list.getdp()!=null)
            holder.dp.setImageBitmap(list.getdp());
        String puid=holder.postbyusername.getText().toString();
        String uid=openingActivity.ps.getString("username","n/a");
        if(puid.equals(uid)) {
            holder.edit.setVisibility(View.VISIBLE);
        }



        holder.dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                openingActivity.pe.putString("useusername",holder.postbyusername.getText().toString());
                openingActivity.pe.commit();
                Intent in=new Intent(context,profile1.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);

            }
        });

        holder.postbyusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openingActivity.pe.putString("useusername",holder.postbyusername.getText().toString());
                openingActivity.pe.commit();
                Intent in=new Intent(context,profile1.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);

            }
        });


        holder.postbyname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openingActivity.pe.putString("useusername",holder.postbyusername.getText().toString());
                openingActivity.pe.commit();
                Intent in=new Intent(context,profile1.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);

            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                hostclass.onedit(list.getclassid(),list.gettitle(),list.getdesc(),list.getlink(),list.gettutor(),list.getdate(),list.getvenue());
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView classid;
        public TextView link;
        public TextView desc;
        public TextView tutor;
        public TextView postbyname;
        public TextView postbyusername;
        public ImageView dp;
        public TextView noofpeople;
        public Button edit;
        public Button attended;
        public TextView date;
        public TextView venue;
        public TextView id,etattend;

        public ViewHolder(final View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.title);
            dp=(ImageView)itemView.findViewById(R.id.dp);
            classid=(TextView)itemView.findViewById(R.id.id);
            desc=(TextView)itemView.findViewById(R.id.des);
            tutor=(TextView)itemView.findViewById(R.id.tutor);
            postbyname=(TextView)itemView.findViewById(R.id.postby);
            postbyusername=(TextView)itemView.findViewById(R.id.username);
            noofpeople=(TextView)itemView.findViewById(R.id.noofstud);
            edit=(Button)itemView.findViewById(R.id.edit);
            attended=(Button)itemView.findViewById(R.id.attendbutton);
            date=(TextView)itemView.findViewById(R.id.datetime);
            venue=(TextView)itemView.findViewById(R.id.venue);
            link=(TextView)itemView.findViewById(R.id.link);
            etattend=(TextView)itemView.findViewById(R.id.etattend);
        }



    }


    public  void castattend(final String idd,String user,final int usepos,final ListItem obj,final ViewHolder holder) {
        class likeTask extends AsyncTask<String, String, String> {


            HttpURLConnection conn;
            URL url = null;


            @Override
            protected String doInBackground(String... params) {
                // TODO: attempt authentication against a network service.

                try {

                    // Enter URL address where your php file resides
                    url = new URL("https://nitd.000webhostapp.com/cse%20nitd/attendclass.php");
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block

                    e.printStackTrace();
                    return "exception";
                }
                try {
                    // Setup HttpURLConnection class to send and receive data from php and mysql
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setRequestMethod("POST");

                    // setDoInput and setDoOutput method depict handling of both send and receive
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    // Append parameters to URL
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("id", params[0])
                            .appendQueryParameter("user", params[1]);
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
                        return (result.toString());

                    } else {

                        return ("unsuccessful");
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

                if (result.equals("false") || result.equals("exception") || result.equals("unsuccessful")) {

                    Toast.makeText(context, "OOPs! Unable to attend.", Toast.LENGTH_LONG).show();
                } else {
               /*  case 1: one -  liked
                   case 2: two-   canceled like
                */
                    if (result.equals("one")) {
                 //       Toast.makeText(context, "Attending", Toast.LENGTH_LONG).show();
                        int cur=Integer.parseInt(holder.noofpeople.getText().toString());
                        cur++;
                        holder.attended.setBackgroundColor(0xff00dfff);
                        holder.attended.setText("ATTENDING");
                        holder.noofpeople.setText(cur+"");
                        obj.setattended("1");
                        obj.setnoofpeople(cur + "");
                        items.set(usepos, obj);

                    } else if (result.equals("two")) {
                //        Toast.makeText(context, "Not attending", Toast.LENGTH_LONG).show();
                        int cur=Integer.parseInt(holder.noofpeople.getText().toString());
                        cur--;
                        holder.attended.setBackgroundColor(0xffcdcec2);
                        holder.attended.setText("ATTEND");
                        holder.noofpeople.setText(cur+"");
                        obj.setattended("0");
                        obj.setnoofpeople(cur + "");
                        items.set(usepos, obj);

                    }
                }
            }

            @Override
            protected void onCancelled() {

                //progressDialog.dismiss();
            }
        }

        new likeTask().execute(idd,user);
    }

int getmon(String s)
{
    switch (s)
    {
        case "Jan":return 1;
        case "Feb":return 2;
        case "Mar":return 3;
        case "Apr":return 4;
        case "May":return 5;
        case "June":return 6;
        case "July":return 7;
        case "Aug":return 8;
        case "Sept":return 9;
        case "Oct":return 10;
        case "Nov":return 11;
        case "Dec":return 12;
    }
    return 1;

}

}

