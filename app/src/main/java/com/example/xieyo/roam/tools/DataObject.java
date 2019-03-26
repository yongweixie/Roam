package com.example.xieyo.roam.tools;

import com.example.xieyo.roam.baseinfo.BaseInfo;

import cn.bmob.v3.BmobObject;

public class DataObject extends BmobObject {
    private String type;
    private String data;

    public DataObject() {
        this.setTableName("u"+BaseInfo.account);
    }
    public void setObjecttype(String type){this.type=type;}
    public void setObjectdata(String data){this.data=data;}
    public String getObjecttype() {
        return type;
    }
    public String getObjectdata() {
        return data;
    }
}
