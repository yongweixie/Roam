package com.example.xieyo.roam.musicfragment;


import android.content.Context;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.example.xieyo.roam.CustomView.MyJzvdStd;
import com.example.xieyo.roam.LazyFragment;
import com.example.xieyo.roam.R;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class MusicDiscoveryFragment  extends LazyFragment {
    public int setContentView() {
        return R.layout.musicdiscovery_fragment;
    }


    public void init() {
        JzvdStd jzvdStd = (JzvdStd)rootView. findViewById(R.id.videoplayer);
        jzvdStd.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子闭眼睛" , Jzvd.SCREEN_WINDOW_NORMAL);
    }
    @Override
    public void lazyLoad() {
//        MyJzvdStd jzvdStd = (MyJzvdStd)rootView. findViewById(R.id.videoplayer);
//        jzvdStd.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
//                , "饺子快长大", JzvdStd.SCREEN_WINDOW_NORMAL);
//        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(jzvdStd.thumbImageView);
//
}

}