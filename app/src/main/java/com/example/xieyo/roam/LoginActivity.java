package com.example.xieyo.roam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends BaseActivity{
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

        mSpSettings=getSharedPreferences(PREPS_NAME,MODE_PRIVATE);
        pSpSettings=getSharedPreferences(PREPS_NAME,MODE_PRIVATE);
        if (pSpSettings.getBoolean("isRP",true))
        {
            UserAcountText.setText(pSpSettings.getString("useraccount",""));
            UserPasswordText.setText(pSpSettings.getString("password",""));
        }

        UserloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account=UserAcountText.getText().toString();
                final String password=UserPasswordText.getText().toString();



            }
        });


    }

}
