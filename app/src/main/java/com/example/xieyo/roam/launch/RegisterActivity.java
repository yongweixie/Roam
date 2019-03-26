package com.example.xieyo.roam.launch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.R;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


public class RegisterActivity extends BaseActivity {
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
                BmobSMS.requestSMSCode(account, "Roam", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer smsId, BmobException e) {
                        if (e == null) {
                            // mTvInfo.append("发送验证码成功，短信ID：" + smsId + "\n");
                            Toast.makeText(getApplication(), "发送验证码成功", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplication(), "发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                finish();
                Intent intent = new Intent(RegisterActivity.this, SmsCodeActivity.class);
                intent.putExtra("extra_data1", account);
                intent.putExtra("extra_data2", password);
                //启动Intent
                startActivity(intent);
//注意：不能用save方法进行注册
            }
        });

    }
}
