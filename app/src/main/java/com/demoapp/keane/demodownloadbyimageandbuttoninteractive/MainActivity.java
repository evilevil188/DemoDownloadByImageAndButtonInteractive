package com.demoapp.keane.demodownloadbyimageandbuttoninteractive;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ImageView downloadImg;
    private Button button;
    private Bitmap bitmap = null;
    private ProgressBar progressBar;
    private final static String url = "https://upload.wikimedia.org/wikipedia/sh/6/65/Bart_Simpson.png";
    //"https://cfl.dropboxstatic.com/static/images/illustration_catalog/sickbox-illo_m1.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadImg = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        button = (Button) findViewById(R.id.button);
    }

    public void startDownLoad(View v) {
        ImageDownload task = new ImageDownload();
        try {
            bitmap = task.execute(url).get();
            downloadImg.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Interaction", "Button Tapped");
    }


    //download and RETURN Bitmap use BitmapFactory.decodeStream
    public class ImageDownload extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                HttpURLConnection httpUrlConnec = (HttpURLConnection) url.openConnection();
                httpUrlConnec.connect();
                InputStream in = httpUrlConnec.getInputStream();
                Bitmap downImage = BitmapFactory.decodeStream(in);
                httpUrlConnec.disconnect();
                return downImage;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
