package com.example.xieyo.roam.launch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.MainActivity;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.User;
import com.example.xieyo.roam.tools.DateBaseUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends BaseActivity {
    private EditText UserAcountText,UserPasswordText;
    private SharedPreferences mSpSettings,pSpSettings=null;//声明一个sharedPreferences用于保存数据
    private static final String PREPS_NAME="NamePwd";
    private CheckBox isRmenberpassword,isRememberstate;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button UserloginButton;
        UserloginButton=findViewById(R.id.user_login_button);
        UserAcountText=findViewById((R.id.user_login_acount));
        UserPasswordText=findViewById(R.id.user_login_password);



        UserloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account=UserAcountText.getText().toString();
                final String password=UserPasswordText.getText().toString();

                final User user = new User();
                //此处替换为你的用户名
                user.setMobilePhoneNumber(account);
                //此处替换为你的密码
                user.setPassword(password);
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User bmobUser, BmobException e) {
                        if (e == null) {
                            DateBaseUtils dateBaseUtils=new DateBaseUtils(LoginActivity.this);
                            DateBaseUtils.setLoginState(true);
                            DateBaseUtils.setUserInfo(account);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                          //  Snackbar.make(view, "登录成功：" + user.getUsername(), Snackbar.LENGTH_LONG).show();
                        } else {
                          //  Snackbar.make(view, "登录失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });


    }

}
