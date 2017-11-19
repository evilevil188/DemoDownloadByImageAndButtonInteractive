package com.demoapp.keane.demodownloadbyimageandbuttoninteractive;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button button;
    private Bitmap bitmap = null; 

    private DownloadTask loadPic;
    private Handler mHandler;
    private ProgressBar progressBar;
    private final static String url = "https://cfl.dropboxstatic.com/static/images/illustration_catalog/sickbox-illo_m1.png";


    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        button = (Button) findViewById(R.id.button);


    }

    public void startDownLoad(View v) {
        progressBar.setVisibility(findViewById(R.id.progressBar).VISIBLE);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        progressBar.setVisibility(View.GONE);
                        imageView.setImageBitmap(getBitmap());
                        break;

                }
                super.handleMessage(msg);
            }
        };
        handWebPic(url, mHandler);
    }

    public void handWebPic(final String url, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadPic = new DownloadTask();
                try {
                    String resultStr = loadPic.execute(url).get();
                    System.out.println("resultStr = " + resultStr);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Message m = new Message();
                m.what = 1;
                handler.sendMessage(m);
            }
        }).start();
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                HttpURLConnection httpUrlConnec = (HttpURLConnection) url.openConnection();
                httpUrlConnec.connect();
                InputStream in = httpUrlConnec.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int length = httpUrlConnec.getContentLength();
                int tempLength = 512;
                int readLen = 0;
                int desPos = 0;
                byte[] img = new byte[length];
                byte[] temp = new byte[tempLength];
                if (length != -1) {
                    while ((readLen = in.read(temp)) > 0) {
                        System.arraycopy(temp, 0, img, desPos, readLen);
                        desPos += readLen;
                    }
                    bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                    if (desPos != length) {
                        throw new IOException("Only read" + desPos + "bytes");
                    }
                }
                httpUrlConnec.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Download ok";
        }
    }

}
