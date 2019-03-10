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
        jzvdStd.setUp("http://upos-hz-mirrorwcsu.acgvideo.com/upgcxcode/95/43/53064395/53064395-1-208.mp4?e=ig8euxZM2rNcNbeHhbUVhoMHnWNBhwdEto8g5X10ugNcXBlqNCNEto8g5gNvNE3DN0B5tZlqNxTEto8BTrNvN05fqx6S5ahE9IMvXBvEuENvNCImNEVEua6m2jIxux0CkF6s2JZv5x0DQJZY2F8SkXKE9IB5QK==&ua=tvproj&deadline=1551965524&gen=playurl&nbs=1&oi=2501663261&os=wcsu&trid=9ff0e52c159a48fab4f1db818221c085&uipk=5&upsig=feb49fadb4a457fcfbcfda7d6d3f6355&uparams=e,ua,deadline,gen,nbs,oi,os,trid,uipk"
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
