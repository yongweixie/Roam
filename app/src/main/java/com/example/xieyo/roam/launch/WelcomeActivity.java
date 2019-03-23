package com.example.xieyo.roam.launch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.MainActivity;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.tools.DateBaseUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class WelcomeActivity extends BaseActivity {

    static TextView Day;
    static TextView month;
    static ImageView Image;
    static TextView type;
    static TextView text;
    static ONE mone;
    static Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        con=getApplicationContext();
        InitView();

        new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
            @Override
            public void run() {
                DateBaseUtils dateBaseUtils=new DateBaseUtils(WelcomeActivity.this);
               if(DateBaseUtils.getLoginState())
               {
                   Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                   startActivity(intent);
                   WelcomeActivity.this.finish();
               }
               else
               {
                   Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                   startActivity(intent);
                   WelcomeActivity.this.finish();
               }

            }
        }, 2000);

    }
    private void InitView()
    {
         Day=findViewById(R.id.day);
         month=findViewById(R.id.month);
         Image=findViewById(R.id.image);
         type=findViewById(R.id.type);
         text=findViewById(R.id.text);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO: http request.
                Message msg = new Message();
                mone=getData();
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();

    }
    private static class Listndler extends Handler {
        WeakReference<WelcomeActivity> mMainActivityWeakReference;

        public Listndler(WelcomeActivity mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Day.setText(mone.Day);
            month.setText(mone.month);
            type.setText(mone.Type);
            text.setText("       "+mone.Text);
            RequestOptions options = new RequestOptions()
                    .optionalTransform(new RoundedCornersTransformation(5, 0, RoundedCornersTransformation.CornerType.ALL))
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(con).load(mone.Image)
                    .apply(options)
                    .into(Image);
        }

    }
    private static WelcomeActivity.Listndler handler = new WelcomeActivity.Listndler(new WelcomeActivity());
    private static  ONE getData()
    {
        ONE one=new ONE();
        String url = "http://wufazhuce.com/";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("div[class=\"carousel-inner\"]").select("div[class=\"item active\"]");
            one.Day=elements.select("p[class=\"dom\"]").text();
            one.Image=elements.select("img[class=\"fp-one-imagen\"]").attr("src");
            one.month=elements.select("p[class=\"may\"]").text();
            one.Type=elements.select("div[class=\"fp-one-imagen-footer\"]").text();
            one.Text=elements.select("div[class=\"fp-one-cita\"]").select("a").text();
        } catch (Exception e) {

        }

        return one;

    }
    private static class ONE
    {
        private String Type;
        private String Image;
        private String Day;
        private String month;
        private String Text;
    }
}
