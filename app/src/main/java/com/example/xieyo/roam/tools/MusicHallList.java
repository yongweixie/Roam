package com.example.xieyo.roam.tools;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MusicHallList implements MultiItemEntity {

    public String title;
    // 后期可加入Glide加载网络图片Url
    public String imageUri;
    public String playCount;
    public String id;
    public int from;
    public int type;//标题栏&歌单
    //必须重写的方法，开发工具会提示你的
    @Override
    public int getItemType() {
        return type;
    }
}
