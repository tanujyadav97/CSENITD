package com.cse.csenitd.Timeline;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cocosw.bottomsheet.BottomSheet;
import com.cse.csenitd.DbHelper.Upload;
import com.cse.csenitd.R;
import com.cse.csenitd.openingActivity;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import org.xmlpull.v1.XmlPullParserException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import static com.cse.csenitd.LoginActivity.CONNECTION_TIMEOUT;
import static com.cse.csenitd.LoginActivity.READ_TIMEOUT;

/**
 * Created by lenovo on 24-06-2017.Mohit yadav
 */

public class Add_Timeline extends AppCompatActivity {
    Button upBtn;
    ArrayList<Image> images, result;
    ImageView img1, img2, img3, img4;
    Display display;
    Point size;
    int width, sst;
    boolean isPlaying = false;
    FrameLayout frameLayout;
    VideoView videoView;
    Bundle saved;
    EditText eds;
    String vdreturnedPath = null;
    boolean isImageSelected = false, isVideoselected = false;
    ImageButton imageButton;

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timeline);
        upBtn = (Button) findViewById(R.id.upload_button);
        videoView = (VideoView) findViewById(R.id.videoview);
        eds = (EditText) findViewById(R.id.timeline_post);
        imgstrs = new ArrayList<>();
        saved = savedInstanceState;
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.INVISIBLE);

        if (videoView.isPlaying()) saved.putInt("pos", videoView.getCurrentPosition());

        result = new ArrayList<>();
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        width = size.x;
        sst = width - 20;
        Log.d("Width is", String.valueOf(sst));
        Log.d("Width is", String.valueOf(width));
        int i = 3;

        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomsheet();
            }
        });
    }

    public void showBottomsheet() {
        new BottomSheet.Builder(this).title("Upload").sheet(R.menu.menu_timeline_bottomsheet).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.upload_image:
                        images = new ArrayList<>();

                        chooseImage();
                        break;
                    case R.id.upload_video:
                        chooseVideos();
                        break;
                    case R.id.upload_gif:
                        chooseGif();
                        break;
                    case R.id.insert_link:

                        break;
                }
            }
        }).show();
    }

    private void chooseGif() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

    public void chooseVideos() {
        new VideoPicker.Builder(Add_Timeline.this)
                .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                .directory(VideoPicker.Directory.DEFAULT)
                .build();
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void chooseImage() {
        imgstrs.clear();
        result.clear();
        if(imgstrs.isEmpty()&&result.isEmpty())
            Toast.makeText(this, "dsf", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ImagePickerActivity.class);

        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_MODE, true);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_MODE, ImagePickerActivity.MODE_MULTIPLE);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_LIMIT, 10);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SHOW_CAMERA, true);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES, images);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_TITLE, "Album");
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_TITLE, "Tap to select images");
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_DIRECTORY, "Camera");

        startActivityForResult(intent, 0);

    }

    public void showImages() {
        imageButton.setVisibility(View.VISIBLE);

        frameLayout = (FrameLayout) findViewById(R.id.imgframe);
        int id = result.size();
        ImageView imageView1 = null, imageView2 = null, imageView3, imageView4;
        imageView1 = new ImageView(this);
        imageView2 = new ImageView(this);
        imageView3 = new ImageView(this);
        imageView4 = new ImageView(this);
        imageView1.setImageDrawable(null);
        imageView1.setImageResource(0);
        imageView2.setImageResource(0);
        imageView2.setImageDrawable(null);
        frameLayout.removeAllViews();

        if (id == 1) {


            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeFile(result.get(0).getPath());
            imageView1.setImageBitmap(bitmap);
            imageView1.setPadding(0, 5, 0, 0);
            imageView1.setScaleType(ImageView.ScaleType.FIT_XY);

            imageView1.setLayoutParams(new FrameLayout.LayoutParams(frameLayout.getWidth(), frameLayout.getHeight()));
            frameLayout.addView(imageView1);
            frameLayout.addView(imageButton);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeViews();
                }
            });
        }

        if (id == 2) {

            Bitmap bitmap = null;
            Bitmap bitmap1 = null;

            bitmap = BitmapFactory.decodeFile(result.get(0).getPath());
            bitmap1 = BitmapFactory.decodeFile(result.get(1).getPath());


            imageView1.setImageBitmap(bitmap);
            imageView1.setPadding(0, 5, 0, 0);
            imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(frameLayout.getWidth() / 2,
                    frameLayout.getHeight()));

            imageView2.setImageBitmap(bitmap1);
            imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(frameLayout.getWidth() / 2,
                    frameLayout.getHeight()));
            imageView1.setX(width / 2);
            imageView1.setPadding(5, 5, 0, 0);
            frameLayout.addView(imageView2);
            frameLayout.addView(imageView1);
            frameLayout.setOverScrollMode(1);
            frameLayout.addView(imageButton);


            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeViews();
                }
            });
        }
        if (id == 3) {

            Bitmap bitmap = null;
            Bitmap bitmap1 = null;
            Bitmap bitmap2 = null;

            bitmap = BitmapFactory.decodeFile(result.get(0).getPath());
            bitmap1 = BitmapFactory.decodeFile(result.get(1).getPath());
            bitmap2 = BitmapFactory.decodeFile(result.get(2).getPath());


            imageView1.setImageBitmap(bitmap);
            imageView1.setPadding(0, 5, 0, 0);
            imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(frameLayout.getWidth() / 2,
                    frameLayout.getHeight()));
            imageView2.setImageBitmap(bitmap1);
            imageView2.setX(frameLayout.getWidth() / 2);
            imageView2.setPadding(5, 5, 0, 0);
            imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(frameLayout.getWidth() / 2,
                    frameLayout.getHeight() / 2));
            imageView3.setImageBitmap(bitmap2);
            imageView3.setX(frameLayout.getWidth() / 2);
            imageView3.setY(frameLayout.getHeight() / 2);
            imageView3.setPadding(5, 5, 0, 0);
            imageView3.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView3.setLayoutParams(new FrameLayout.LayoutParams(frameLayout.getWidth() / 2,
                    frameLayout.getHeight() / 2));
            frameLayout.addView(imageView1);
            frameLayout.addView(imageView2);
            frameLayout.addView(imageView3);
            frameLayout.addView(imageButton);


            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeViews();
                }
            });
        }
        if (id >= 4) {
            //x=0,y=0
            Bitmap bitmap = null;
            Bitmap bitmap1 = null;
            Bitmap bitmap2 = null;
            Bitmap bitmap3 = null;


            bitmap = BitmapFactory.decodeFile(result.get(0).getPath());
            bitmap1 = BitmapFactory.decodeFile(result.get(1).getPath());
            bitmap2 = BitmapFactory.decodeFile(result.get(2).getPath());
            bitmap3 = BitmapFactory.decodeFile(result.get(3).getPath());


            imageView1.setImageBitmap(bitmap);
            imageView1.setPadding(0, 5, 0, 0);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(frameLayout.getWidth() / 2,
                    frameLayout.getHeight()));
            imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
            //x=200,y==0

            imageView2.setImageBitmap(bitmap1);
            imageView2.setX(frameLayout.getWidth() / 2);
            imageView2.setPadding(2, 5, 0, 0);
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(frameLayout.getWidth() / 2,
                    frameLayout.getHeight() / 3));
            imageView2.setScaleType(ImageView.ScaleType.FIT_XY);

            imageView3.setImageBitmap(bitmap2);
            imageView3.setX(frameLayout.getWidth() / 2);
            imageView3.setY(frameLayout.getHeight() / 3);
            imageView3.setPadding(2, 5, 0, 0);
            imageView3.setLayoutParams(new FrameLayout.LayoutParams(frameLayout.getWidth() / 2,
                    frameLayout.getHeight() / 3));
            imageView3.setScaleType(ImageView.ScaleType.FIT_XY);

            imageView4.setImageBitmap(bitmap3);
            imageView4.setX(frameLayout.getWidth() / 2);
            imageView4.setY((frameLayout.getHeight() / 3 + frameLayout.getHeight() / 3));
            imageView4.setPadding(2, 5, 0, 0);
            imageView4.setLayoutParams(new FrameLayout.LayoutParams(frameLayout.getWidth() / 2,
                    frameLayout.getHeight() / 3));
            imageView4.setScaleType(ImageView.ScaleType.FIT_XY);


            TextView textView = new TextView(this);
            if (id > 4) {
                textView.setText("+ " + (result.size() - 4));
                textView.setX(sst / 2);
                textView.setTextColor(Color.parseColor("#424242"));
                textView.setTypeface(null, Typeface.BOLD);
                textView.setY(400 / 3 + 400 / 3);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(20);
                textView.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 400 / 3));
                textView.setBackgroundColor(Color.parseColor("#E0E0E0"));
                textView.setAlpha(0.5f);

            }

            frameLayout.addView(imageView1);
            frameLayout.addView(imageView2);
            frameLayout.addView(imageView3);
            frameLayout.addView(imageView4);
            frameLayout.addView(textView);
            frameLayout.addView(imageButton);


            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeViews();
                }
            });
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void removeViews() {
        frameLayout.removeAllViews();
        imgstrs.clear();
        result.clear();
        if(imgstrs.isEmpty()) Toast.makeText(this, "img", Toast.LENGTH_SHORT).show();
        if(result.isEmpty()) Toast.makeText(this, "res", Toast.LENGTH_SHORT).show();
        isVideoselected = false;
        isImageSelected = false;
    }

    public String pth;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            String mPath = (String) data.getSerializableExtra(VideoPicker.EXTRA_VIDEO_PATH);
            //Your Code
            Toast.makeText(this, "cx", Toast.LENGTH_SHORT).show();
            try {
                pth = mPath;
                showVideo(mPath);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            result.clear();
            result = images;
            isImageSelected = true;
            isVideoselected = false;
            showImages();

        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            try {
                String value = null;
                String returnUri = data.getData().toString();
                path = getRealPathFromURI(returnUri);
                boolean temp = false;
                File file = new File(path);
                float length = file.length();
                length /= 1024;
                if (length < 5000) {
                    isImageSelected = false;
                    isVideoselected = true;
                    showVideo(path);
                } else {
                    Toast.makeText(this, "File Size is Greater then 5 MB", Toast.LENGTH_SHORT).show();
                    isImageSelected = false;
                    isVideoselected = true;
                    path = null;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //showGif();
        }

    }

    public String path = null;

    public String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            if (Build.VERSION.SDK_INT > 19) {
                // Will return "image:x*"
                String wholeID = DocumentsContract.getDocumentId(contentUri);
                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];
                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                cursor = getApplicationContext().getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, sel, new String[]{id}, null);
            } else {
                cursor = getApplicationContext().getContentResolver().query(contentUri,
                        projection, null, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String path = null;
        try {
            int column_index = cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index).toString();
            cursor.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return path;
    }

    public void showVideo(String path) throws XmlPullParserException {
        frameLayout = (FrameLayout) findViewById(R.id.imgframe);
        frameLayout.removeAllViews();
        videoView.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.VISIBLE);

        videoView.setVideoPath(path);
        MediaController mediaController = new MediaController(this);


        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.start();

        int pos = 0;
        if (saved != null) {
            pos = saved.getInt("pos");
            videoView.seekTo(pos);
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeViews();
            }
        });
        frameLayout.addView(videoView);
        frameLayout.addView(imageButton);


    }


    //  Sample java code for videos.insert

    private static final String APPLICATION_NAME = "CseNitd";
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/java-youtube-api-tests");
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT;
    private static final Collection<String> SCOPES = Arrays.asList("YouTubeScopes.https://www.googleapis.com/auth/youtube.force-ssl YouTubeScopes.https://www.googleapis.com/auth/youtubepartner");

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
//            System.exit(1);
        }
    }

    public static void main(String s) throws IOException {
        YouTube youtube = getYouTubeService();
        try {

            String mime_type = "video/*";
            String media_filename = s;
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("part", "snippet,status");

            Video video = new Video();
            VideoSnippet snippet = new VideoSnippet();
            snippet.set("categoryId", "22");
            snippet.set("description", "Description of uploaded video.");
            snippet.set("title", "Test video upload");
            VideoStatus status = new VideoStatus();
            status.set("privacyStatus", "private");

            video.setSnippet(snippet);
            video.setStatus(status);

            InputStreamContent mediaContent = new InputStreamContent(mime_type,
                    Add_Timeline.class.getResourceAsStream(media_filename));
            YouTube.Videos.Insert videosInsertRequest = youtube.videos().insert(parameters.get("part").toString(), video, mediaContent);
            MediaHttpUploader uploader = videosInsertRequest.getMediaHttpUploader();


            uploader.setDirectUploadEnabled(false);

            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    switch (uploader.getUploadState()) {
                        case INITIATION_STARTED:
                            System.out.println("Initiation Started");
                            break;
                        case INITIATION_COMPLETE:
                            System.out.println("Initiation Completed");
                            break;
                        case MEDIA_IN_PROGRESS:
                            System.out.println("Upload in progress");
                            System.out.println("Upload percentage: " + uploader.getProgress());
                            break;
                        case MEDIA_COMPLETE:
                            System.out.println("Upload Completed!");
                            break;
                        case NOT_STARTED:
                            System.out.println("Upload Not Started!");
                            break;
                    }
                }
            };
            uploader.setProgressListener(progressListener);
            Video response = videosInsertRequest.execute();
            System.out.println(response);

        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    public static YouTube getYouTubeService() throws IOException {
        Credential credential = authorize();
        return new YouTube.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = Add_Timeline.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.timeline_post) {
//            try {
//                main(pth);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            ep = eds.getText().toString().trim();
            if (isImageSelected) {
                ArrayList<Bitmap> arr = new ArrayList<>();
                Bitmap bm;
                for (int i = 0; i < result.size(); i++) {
                    bm = BitmapFactory.decodeFile(result.get(i).getPath());
                    arr.add(i, bm);

                }
                for (int i = 0; i < result.size(); i++) {
                    imgstrs.add(i,getStringImage(arr.get(i)));
                }
                Log.d("img", imgstrs.get(0));
                insert i = new insert(imgstrs);
                i.execute(ep, video, gif);
            } else if (isVideoselected) {
                if (path == null) {
                    Toast.makeText(this, "Select A Correct Size video", Toast.LENGTH_SHORT).show();

                } else {
                    UploadVideo uv = new UploadVideo();
                    uv.execute();

                }
            } else {
                uploadText ut = new uploadText();
                ut.execute(ep, null);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    class uploadText extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;
        ProgressDialog uploading;

        @Override
        protected void onPreExecute() {
            uploading = ProgressDialog.show(Add_Timeline.this, "Uploading File", "Please wait...", false, false);

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // Enter URL address where your php file resides
                url = new URL(murl);

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


                String timeStamp = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(new Date());
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("text", params[0])
                        .appendQueryParameter("video", params[1])
                        .appendQueryParameter("datetime", timeStamp)
                        .appendQueryParameter("username", openingActivity.ps.getString("username","n/a"));
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
                    //return params[0]+"/"+params[1];
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


            Toast.makeText(Add_Timeline.this, result, Toast.LENGTH_LONG).show();
            uploading.dismiss();
            if (result.equals("true")) {

                Toast.makeText(Add_Timeline.this, "Posted successfully", Toast.LENGTH_LONG).show();
                Intent in=new Intent(Add_Timeline.this,TimelineP.class);
                startActivity(in);

            } else if (result.equals("false") || result.equals("exception") || result.equals("unsuccessful")) {

                //set old image to profile
                Toast.makeText(Add_Timeline.this, "OOPs! Error Posting.", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {


        }
    }


    class UploadVideo extends AsyncTask<Void, Void, String> {

        ProgressDialog uploading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            uploading = ProgressDialog.show(Add_Timeline.this, "Uploading File", "Please wait...", false, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Add_Timeline.this, s, Toast.LENGTH_SHORT).show();
            vdreturnedPath = s;
            Toast.makeText(Add_Timeline.this, vdreturnedPath, Toast.LENGTH_SHORT).show();
            uploadText ut = new uploadText();
            ut.execute(ep, vdreturnedPath);
            uploading.dismiss();
            //  textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
            //textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @Override
        protected String doInBackground(Void... params) {
            Upload u = new Upload();
            String msg = u.uploadVideo(path);
            return msg;
        }
    }

    ArrayList<String> imgstrs;
    String video = null, ep;
    String gif = null;
    String murl = "https://nitd.000webhostapp.com/cse%20nitd/mohit/addTimeline.php";

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public class insert extends AsyncTask<String, String, String> {
        ArrayList<String> img = new ArrayList<>();

        public insert(ArrayList<String> st) {
            this.img = st;
        }

        HttpURLConnection conn;
        URL url = null;
        ProgressDialog uploading;

        @Override
        protected void onPreExecute() {
            uploading = ProgressDialog.show(Add_Timeline.this, "Uploading File", "Please wait...", false, false);

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // Enter URL address where your php file resides

                url = new URL(murl);

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


                String timeStamp = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(new Date());
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("text", params[0])
                        .appendQueryParameter("datetime", timeStamp)
                        .appendQueryParameter("username", openingActivity.ps.getString("username","n/a"));

                for (int i = 0; i < img.size(); i++) {
                    int k = i + 1;
                    builder.appendQueryParameter("img" + k, img.get(i));
                }
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
                    //return params[0]+"/"+params[1];
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
            uploading.dismiss();

            Toast.makeText(Add_Timeline.this, result, Toast.LENGTH_LONG).show();


            if (result.equals("true")) {

                Toast.makeText(Add_Timeline.this, "Posted successfully", Toast.LENGTH_LONG).show();
                Intent in=new Intent(Add_Timeline.this,TimelineP.class);
                startActivity(in);

            } else if (result.equals("false") || result.equals("exception") || result.equals("unsuccessful")) {

                //set old image to profile
                Toast.makeText(Add_Timeline.this, "OOPs! Error Posting.", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {


        }

    }

}
