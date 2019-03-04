package com.example.xieyo.roam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




public class RegisterActivity extends BaseActivity{
    private EditText UserAcountText,UserPasswordText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button UserRegisterButton;
        UserRegisterButton=findViewById(R.id.user_register_button);
        UserAcountText=findViewById((R.id.user_register_acount));
        UserPasswordText=findViewById(R.id.user_register_password);

        UserRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account=UserAcountText.getText().toString();
                final String password=UserPasswordText.getText().toString();

//注意：不能用save方法进行注册
            }
        });

    }
}
