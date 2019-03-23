package com.example.xieyo.roam.moviebean;


import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MovieFragList implements MultiItemEntity
{
    public String movielink;
    public String coveruri;
    public String artist;
    public String cast;
    public String name;
    public String num_digest;
    public String part_digest;
    public int type;//标题栏&书单,布局种类
    //必须重写的方法，开发工具会提示你的
    public int from;//书来源。新书或是榜单书
    public String rating;
    @Override
    public int getItemType() {
        return type;
    }
}
