package com.example.xieyo.roam.launch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.User;
import com.example.xieyo.roam.view.PhoneCode;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class SmsCodeActivity extends BaseActivity {
    PhoneCode pc_1;
    Button next;
    String phone ;
    String password ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        Intent intent = getIntent();
        phone = intent.getStringExtra("extra_data1");
        password = intent.getStringExtra("extra_data2");

        pc_1 = (PhoneCode) findViewById(R.id.pc_1);
        next=findViewById(R.id.next);
        //注册事件回调（根据实际需要，可写，可不写）
        pc_1.setOnInputListener(new PhoneCode.OnInputListener() {
            @Override
            public void onSucess(String code) {
                //TODO: 例如底部【下一步】按钮可点击
                next.setClickable(true);
            }

            @Override
            public void onInput() {
                //TODO:例如底部【下一步】按钮不可点击
                next.setClickable(false);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Init();
            }
        });

    }
    private void Init(){

        String phoneCode = pc_1.getPhoneCode();
        BmobSMS.verifySmsCode(phone, phoneCode, new UpdateListener() {
            @Override
            public void done(BmobException ex) {
                User user=new User();
                if (ex==null){
                    user.setMobilePhoneNumber(phone);
                    user.setPassword(password);
                    user.setUsername("user"+phone);
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null) {
                                Toast.makeText(getApplication(), "注册成功，请登录", Toast.LENGTH_LONG).show();

                            } else {
                                //Toast.makeText(getApplication(), "注册失败"+e.toString(), Toast.LENGTH_LONG).show();
                                Log.i("123456", "done: "+e.toString());
                            }
                        }
                    });
                    finish();
                    Intent intent = new Intent(SmsCodeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SmsCodeActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
