package com.example.xieyo.roam.musicfragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.xieyo.roam.LazyFragment;
import com.example.xieyo.roam.MyAdapter.MusicHallAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.tools.MusicApi;
import com.example.xieyo.roam.tools.MusicHallList;
import com.example.xieyo.roam.view.SpacesItemDecoration;

import java.net.URL;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MusicDiscoveryFragment  extends LazyFragment {
    public int setContentView() {
        return R.layout.musicdiscovery_fragment;
    }

    public void init() {


    }
    @Override
    public void lazyLoad() {

        JzvdStd jzvdStd = (JzvdStd) rootView.findViewById(R.id.videoplayer);
        jzvdStd.setUp("https://api.bzqll.com/music/tencent/mvUrl?key=579621905&id=m00238resnh&r=4", "饺子闭眼睛" , Jzvd.SCREEN_WINDOW_NORMAL);

        Glide.with(getContext()).load("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640")
                .into(jzvdStd.thumbImageView);
        jzvdStd.startVideo();
    }

}