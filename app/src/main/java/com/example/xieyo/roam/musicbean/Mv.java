package com.example.xieyo.roam.musicbean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class Mv implements MultiItemEntity {
    public String title;
    public String Uri;
    public String coveruri;
    public String artist;
    public int type;//标题栏&歌单
    public int titletype;
    public int getItemType() {
        return type;
    }

}
