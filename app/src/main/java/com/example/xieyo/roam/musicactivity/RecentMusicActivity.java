package com.example.xieyo.roam.musicactivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.xieyo.roam.R;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class RecentMusicActivity extends BaseMusicActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
        JzvdStd jzvdStd = (JzvdStd) findViewById(R.id.videoplayer);
        jzvdStd.setUp("https://api.bzqll.com/music/tencent/mvUrl?key=579621905&id=m00238resnh&r=4", "饺子闭眼睛" , Jzvd.SCREEN_WINDOW_NORMAL);

        Glide.with(this).load("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640")
                .into(jzvdStd.thumbImageView);
        jzvdStd.startVideo();
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
