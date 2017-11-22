package com.demoapp.keane.braintraningbygrassimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.demoapp.keane.demodownloadbyimageandbuttoninteractive.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity {
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    //    private Button smallButton;
//    private Button bigButton;
    private TextView ansTextView;
    private ImageView imageView;

    private ImageDownload imageDownload = new ImageDownload();
    private StringBuilder urlSb = new StringBuilder(1024);
    private static Logger log = LoggerFactory.getLogger(MainActivity.class);
//    private boolean isGetWebData = false;

    private String[] altArr = new String[100];
    private String[] srcArr = new String[100];
    private int ansIndex = 0;
    private int[] ansIndexArr = new int[4];
    private int tureANS = 0;
    private Random random = new Random();
    private boolean one = true;
    private String ss = "";
    private Bitmap bb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        UrlDownload urlTask = new UrlDownload();
        try {
            ss= urlTask.execute("http://www.posh24.se/kandisar").get();
            processUrlStrUsePattern();
            ansIndex = random.nextInt(100);
//            setNewQueention(srcArr[1]);
            bb = imageDownload.execute(srcArr[ansIndex]).get();
            imageView.setImageBitmap(bb);
            setAns();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        if (one) {
//            getWebData();
////            processUrlStrUsePattern();

//        }
        one = false;


    }

    public void setNewQueention(String url) {
        log.debug("enter setQueenTion");
        try {
            ImageDownload imageDownload = new ImageDownload();
            bb = imageDownload.execute(url).get();
            imageView.setImageBitmap(bb);
            setAns();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void setAns() {
        tureANS = random.nextInt(4);// 0～3 隨機數| ans.nextInt(4)+1; 取0～4 隨機數
        log.debug("tureAns = " + tureANS);
        int falseIndex = 0;
        //取答案隨機的index
        for (int i = 0; i < 4; i++) {
            if (i == tureANS) {
                ansIndexArr[i] = ansIndex;
            } else {
                falseIndex = random.nextInt(100);// 0～99 隨機數
                while (ansIndex == falseIndex) {
                    falseIndex = random.nextInt(100);
                }
                ansIndexArr[i] = falseIndex;
            }
        }
        setButtonStr(ansIndexArr);
    }

    public void setButtonStr(int[] ansIndexArray) {
        button0.setText(altArr[ansIndexArray[0]]);
        button1.setText(altArr[ansIndexArray[1]]);
        button2.setText(altArr[ansIndexArray[2]]);
        button3.setText(altArr[ansIndexArray[3]]);
    }

    public void setView() {
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        imageView = (ImageView) findViewById(R.id.imageView);
        ansTextView = (TextView) findViewById(R.id.ansTextView);
    }

    public class ImageDownload extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url;
            HttpURLConnection httpURLConnection = null;
            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream in = httpURLConnection.getInputStream();
                Bitmap downloadBitmap = BitmapFactory.decodeStream(in);
                httpURLConnection.disconnect();
                return downloadBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void sendAnd(View view) {
        ansTextView.setVisibility(findViewById(R.id.ansTextView).VISIBLE);
        StringBuilder sb = new StringBuilder(36);
        if (view.getTag().toString().equals(Integer.toString(tureANS))) {
            log.debug("答對了");
            ansTextView.setText("Current!!");
        } else {
            log.debug("答錯了");
            sb.append("Current is").append(" ").append(altArr[ansIndex]);
            ansTextView.setText(sb.toString());
        }
        ansIndex = random.nextInt(100);
        setNewQueention(srcArr[ansIndex]);
//        ansTextView.setVisibility(findViewById(R.id.ansTextView).INVISIBLE);
    }

//    public void getWebData() {
//        try {
//           //http://www.posh24.se/kandisar
////            System.out.println("download is ok? = " + urlTask.execute("http://www.posh24.se/kandisar").get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        URL downloadURL;
        HttpURLConnection httpURLConnection = null;

        @Override
        protected Bitmap doInBackground(String... imageUrls) {
            try {
                downloadURL = new URL(imageUrls[0]);
                httpURLConnection = (HttpURLConnection) downloadURL.openConnection();
                httpURLConnection.connect();
                InputStream in = httpURLConnection.getInputStream();
                httpURLConnection.disconnect();
                return BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    public void receiveStartSymbol(){

    }
    public class UrlDownload extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            char ch;
            try {
                //連結url
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.connect();
                //建立url的輸入流，讀取來自url的輸入流
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                StringBuilder sb = new StringBuilder(256);
                int packet = reader.read();
//                int flowControl = 0;
//                int addIndex = 0;
//                boolean srcBoolean = false;
////                <img src="http://cdn.posh24.se/images/:profile/02edba3ff2cc8920f072a256aec577e86" alt="Selena Gomez"/>
//                byte[] srcByte = {0x3C,0x69,0x6D,0x67, 0x20 , 0x75, 0x72 , 0x63, 0x3D, 0x22};
                while (packet != -1) {
//                    switch (flowControl) {
//                        case 0: //接收src
//                            switch(packet){
//                                case 0x3C:// <
//                                    urlSb.append(packet);
//                                    break;
//                                case 0x69://i
//                                    urlSb.append(packet);
//                                    break;
//                                case 0x6D: //m
//                                    urlSb.append(packet);
//                                    break;
//                                case 0x67:  //g
//                                    urlSb.append(packet);
//                                    break;
//                                case 0x20:// ""
//                                    urlSb.append(packet);
//                                    break;
//                                case 0x75://s
//                                    urlSb.append(packet);
//                                    break;
//                                case 0x72://r
//                                    urlSb.append(packet);
//                                    break;
//                                case 0x63://c
//                                    urlSb.append(packet);
//                                    break;
//                                case 0x3D://=
//                                    urlSb.append(packet);
//                                    break;
//                                case 0x22://"
//                                    if(srcBoolean){
//                                        if(urlSb.length()>10){
//                                            srcArr[addIndex] = urlSb.toString();
//                                        }
//                                        log.debug("now " + addIndex + " = " + srcArr[addIndex]);
//                                        srcBoolean = false;
//                                    }else{
//                                        urlSb.append(packet);
//                                        if("<img src=\"".equals(urlSb.toString()) && urlSb.length() == 10){
//                                            log.debug("get " + urlSb.toString());
//                                            srcBoolean = true;
//                                        }
//                                    }
//                                    urlSb.setLength(0);
//                                    break;
//                                default:
//                                    if(srcBoolean){
//                                        urlSb.append(packet);
//                                    }
//                                    break;
//
//                            }
//
////                        break;
////                        case 1: //接收alt
////                            break;
////                        case 2:
////                            break;
////                        case 3:
//                        default:
//                            break;
//                    }
                    ch = (char) packet;
                    sb.append(ch);
//                    urlSb.append(ch);// not work
                    packet = reader.read();
                }
//                return  sb.toString();
//                urlConnection.disconnect();
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }
    }

    public void processUrlStrUsePattern() {
//        Map map = new HashMap<String, String>();
        log.debug("processUrlStr");
        Pattern srcP = Pattern.compile("<img src=\"(.*?)\"");
        Matcher srcM = srcP.matcher(ss.toString());
        Pattern altP = Pattern.compile("alt=\"(.*?)\"");
        Matcher altM = altP.matcher(ss.toString());
        int index = 0;
        //因為有src= 不一定有alt。 但有alt的，前面一定有src。
        //所以就當alt有找到下一個alt時，且當src也有找到下一個時，就是我要的
        //map(key , value ) ==> map( alt字串 , src字串)
        //避免src數目與alt數目不符，還是互相對應的問題，也就不用寫程式再去解決了
        while (srcM.find() && altM.find()) {
//            map.put(altM.group(1), srcM.group(1));
            altArr[index] = altM.group(1);
            srcArr[index] = srcM.group(1);

            index++;
        }
        log.debug("show get " + Arrays.toString(srcArr) + "\n" + Arrays.toString(altArr));
    }
}
//    public void processUrlStrUseIndexOf() {
//        log.debug("processUrlStr");
//        String findStart = "<img src=";
//        String findEnd = "/>";
//        int altIndex = 0;
//        int startFind = 0;      //find  <img src="   +9
//        int endFind = 0;        //find  "/>
//        int count = 0;
//        ArrayList<String> ImageUrlArr = new ArrayList<String>();
//        while ((startFind = urlSb.indexOf(findStart, endFind)) != -1) {
//            if ((endFind = urlSb.indexOf(findEnd, startFind)) != -1) {
//                altIndex = urlSb.indexOf("alt=", startFind);
//                if (altIndex != -1 && altIndex < endFind) {
//                    ImageUrlArr.add(urlSb.toString().substring(startFind + 10, endFind));
//                }
//            }
//        }
//        log.debug("size = " + ImageUrlArr.size());
//        Iterator<String> t = ImageUrlArr.iterator();
//        while (t.hasNext()) {
//            log.debug("getString = " + t.next());
//            // http://cdn.posh24.se/images/:profile/05848d068cb5ee404e44d7d4c5d5c598d" alt="Demi Moore"
//        }
//    }


//    public void small(View view) { //not work
//
//        //imageView轉Bitmap
//        imageView.buildDrawingCache();
//        Bitmap bmp = imageView.getDrawingCache();
//
//        //轉換為圖片指定大小
//        //獲得圖片的寬高
//        int width = bmp.getWidth();
//        int height = bmp.getHeight();
//
//        //縮小為0.8倍
//        float scaleWidth = (float) 0.8;
//        float scaleHeight = (float) 0.8;
//
//        // 取得想要缩放的matrix參數
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        // 得到新的圖片
//        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
//
//        //重新載入 imageView
//        imageView.setImageBitmap(newbm);
//    }
//
//    public void big(View view) {//not work
//
//        //imageView轉Bitmap
//        imageView.buildDrawingCache();
//        Bitmap bmp = imageView.getDrawingCache();
//
//        //轉換為圖片指定大小
//        //獲得圖片的寬高
//        int width = bmp.getWidth();
//        int height = bmp.getHeight();
//
//        //放大為1.2倍
//        float scaleWidth = (float) 1.2;
//        float scaleHeight = (float) 1.2;
//
//        // 取得想要缩放的matrix參數
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        // 得到新的圖片
//        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
//
//        //重新載入 imageView
//        imageView.setImageBitmap(newbm);
//    }


//
//public class UrlDownload2 extends AsyncTask<String, Void, String> {
//    @Override
//    protected String doInBackground(String... webUrls) {
//        URL url;
//        HttpURLConnection httpURLConnection = null;
//        char ch;
//        try {
//            url = new URL(webUrls[0]);
//            httpURLConnection = (HttpURLConnection) url.openConnection();
//
//            InputStream in = httpURLConnection.getInputStream();
//            InputStreamReader r = new InputStreamReader(in);
////                char ch = null;
//            int packet = r.read();
//            while (packet != -1) {
//                ch = (char) packet;
//                urlSb.append(ch);
//            }
////                return "now the download ok ^o^123";
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return "now the download ok ^o^";
//    }
//}


//    /**
//     * 从map中随机取得一个key
//     * @param map
//     * @return
//     */
//    public static <K, V> K getRandomKeyFromMap(Map<K, V> map) {
//        int rn = getRandomInt(map.size());
//        int i = 0;
//        for (K key : map.keySet()) {
//            if(i==rn){
//                return key;
//            }
//            i++;
//        }
//        return null;
//    }
//
//    /**
//     * 获得一个[min, max]之间的随机整数
//     * @param min
//     * @param max
//     * @return
//     */
//    public static int getRandomInt(int min, int max) {
//        return getRandom().nextInt(max-min+1) + min;
//    }
//
//    /**
//     * 获得一个[0,max)之间的随机整数。
//     * @param max
//     * @return
//     */
//    public static int getRandomInt(int max) {
//        return getRandom().nextInt(max);
//    }
//
//    //双重校验锁获取一个Random单例
//    public static ThreadLocalRandom getRandom() {
//        return ThreadLocalRandom.current();
//
//    }



 /*------------------------------------------------------------
use Matcher.find() method
--------------------------------------------------------------*/
//int matcherNumber = 0;
//        Pattern p = Pattern.compile("<img src=\"(.*?)\"");
//        Matcher m = p.matcher(urlSb.toString());
//        while(m.find()){
//            log.debug("matcher findStr = " + m.group(1));
//matcherNumber ++;
//        }
//        log.debug("mathcer src number = " +matcherNumber);
//
//matcherNumber =0;
//        Pattern altP = Pattern.compile("alt=\"(.*?)\"");
//        Matcher altM = altP.matcher(urlSb.toString());
//        while(altM.find()){
//            log.debug("matcher alt str = " + altM.group(1));
//        //Rooney Mara
//            matcherNumber++;
//        }
//        log.debug("mathcer alt number = " +matcherNumber);


//        之前在寫 java 時一直有個困擾，那就是在宣告 List 或 Map 類別時，不知該如何順便將其初始化，
//        所以往往要先宣告後再將要放入 List 或 Map 的物件一個一個的放入。
//        不過今天在追 Struts 2 的原始碼時，發現了一個寫法，可以在宣告的同時順便初始化。寫法如下：
//
//        List list = new ArrayList () { { add ("123"); } };
//
//        Map map = new HashMap () { { put ("key", "value"); } };

//印出map所有值
//        for (Object key : map.keySet()) {
//            log.debug(key + " : " + map.get(key));
//        }

//取隨機
//        Iterator it = map.keySet().iterator();
//        String ss = (String) it.next();
//        log.debug("ss = " + ss);
//        String gg = (String) it.next();
//        log.debug("ss value= " + map.get(ss));


