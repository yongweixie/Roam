package com.example.xieyo.roam;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;


import com.example.xieyo.roam.baseinfo.BaseInfo;
import com.example.xieyo.roam.launch.LoginActivity;
import com.example.xieyo.roam.tools.DateBaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;


/**
 * Created by xie on 2017/12/23.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Bmob.initialize(this, "5027e730f0cb38e808583e897e871b00");
        initPremission();


        //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
        //        WindowManager.LayoutParams. FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        DateBaseUtils dateBaseUtils=new DateBaseUtils(this);
        BaseInfo.account=DateBaseUtils.getUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);

    }

    private void initPremission(){
        List<String> permissionList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SYSTEM_ALERT_WINDOW)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
        }


        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SET_ALARM)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.SET_ALARM);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_CONTACTS);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.LOCATION_HARDWARE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.LOCATION_HARDWARE);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_CALENDAR);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_CALENDAR);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String [permissionList.size()]);
            ActivityCompat.requestPermissions(BaseActivity.this,permissions,1);
        }


    }

}
