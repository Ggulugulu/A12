package com.example.a12.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a12.R;
import com.example.a12.event.Api;
import com.example.a12.utils.SessionUtilTools;

import org.json.JSONObject;

import okhttp3.Response;

public class LoginActivity extends Activity {
    Button login;
    EditText act,pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.bt_login);
        act =findViewById(R.id.et_1);
        pwd = findViewById(R.id.et_2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            Response response = Api.post_login(act.getText().toString(),pwd.getText().toString());
                            Response response = Api.post_login("admin1","123");
                            SessionUtilTools.getSession(response.headers());// 获取响应中的cookie
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            String errCode = jsonObject.getString("errCode");
                            if(errCode.equals("0")){
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            }else{
                                Looper.prepare();
                                Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}