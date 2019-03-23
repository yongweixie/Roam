package com.example.xieyo.roam.musicbean;


import com.chad.library.adapter.base.entity.MultiItemEntity;

public class Music implements MultiItemEntity
{
    public int Musicindex;
    public String title;
   public String artist;
   public String musicid;//音乐在QQ音乐对应的唯一ID
    public String  path;
    public String musicbmpUri;//Glide加载的url
    public int from;
    public int type;//标题栏&歌单
    //必须重写的方法，开发工具会提示你的
    @Override
    public int getItemType() {
        return type;
    }
}