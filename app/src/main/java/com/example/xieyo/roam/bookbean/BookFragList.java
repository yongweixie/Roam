package com.example.xieyo.roam.bookbean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class BookFragList implements MultiItemEntity
{
    public String booklink;
    public String coveruri;
    public String artist;
    public String name;

    public int type;//标题栏&书单,布局种类
    //必须重写的方法，开发工具会提示你的
    public int from;//书来源。新书或是榜单书
    public String rating;
    public String classification;
    public  String reviews;
    public String sellindex;
    public String numraters;
    public String num_digest;
    public String part_digest;
    @Override
    public int getItemType() {
        return type;
    }
}
