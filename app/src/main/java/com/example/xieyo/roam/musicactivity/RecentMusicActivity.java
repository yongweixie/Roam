package com.example.xieyo.roam.musicactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.xieyo.roam.R;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class RecentMusicActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
        JzvdStd jzvdStd = (JzvdStd) findViewById(R.id.videoplayer);
        jzvdStd.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子闭眼睛" , Jzvd.SCREEN_WINDOW_NORMAL);

    }
//    @Override
//    public void onBackPressed() {
//        if (Jzvd.backPress()) {
//            return;
//        }
//        super.onBackPressed();
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Jzvd.releaseAllVideos();
//    }
}
