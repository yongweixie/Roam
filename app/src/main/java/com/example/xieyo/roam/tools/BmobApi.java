package com.example.xieyo.roam.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.xieyo.roam.baseinfo.BaseInfo;
import com.example.xieyo.roam.baseinfo.BookBaseInfo;
import com.example.xieyo.roam.baseinfo.MovieBaseInfo;
import com.example.xieyo.roam.bookactivity.BookDataActivity;
import com.example.xieyo.roam.musicbean.Music;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BmobApi {

    public static void UpLoadData(String data, String type,Context con) {
        DataObject dataObject = new DataObject();
        dataObject.setObjectdata(data);
        dataObject.setObjecttype(type);

        BmobQuery query = new BmobQuery("u" + BaseInfo.account);
        query.addWhereEqualTo("data", data);
        query.setLimit(2);
        query.order("createdAt");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    if (ary.toString().length() == 2) {

                        if(type.equals("book"))
                        {
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    // TODO: http request.
                                    DateBaseUtils dateBaseUtils=new DateBaseUtils(con);
                                    DateBaseUtils.setReBook(BookBaseInfo.booklink);
                                }
                            };
                            new Thread(runnable).start();
                        }
                        if(type.equals("movie"))
                        {
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    // TODO: http request.
                                    DateBaseUtils dateBaseUtils=new DateBaseUtils(con);
                                    DateBaseUtils.setReMovie(MovieBaseInfo.movielink);
                                }
                            };
                            new Thread(runnable).start();
                        }
                        dataObject.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {

                                    //Toast.makeText(BmobApi.this, "发布信息成功",Toast.LENGTH_LONG).show();
                                } else {
                                    // Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                    else
                    {

                    }
                    //   Log.i("123456", "done: "+ary.toString());

                } else {

                }
            }
        });
    }

    public static List<Music> getFavMusic() {
        List<Music> mlist = new ArrayList<>();

        return mlist;
    }

}
