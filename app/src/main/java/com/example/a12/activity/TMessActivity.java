package com.example.a12.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a12.R;
import com.example.a12.event.Api;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Response;

public class TMessActivity extends AppCompatActivity {
    long dur;
    Button go;
    CheckBox check;
    TextView tv1,time1,time2,duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmess);
        tv1=findViewById(R.id.tv_class);
        time1=findViewById(R.id.tv_time01);
        time2=findViewById(R.id.tv_time02);
        duration=findViewById(R.id.tv_last);
        check = findViewById(R.id.tmess_check);
        go =findViewById(R.id.go);
        init();
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.isChecked()){
                    Intent intent=new Intent(TMessActivity.this,TestActivity.class);
                    intent.putExtra("end_time",dur);
                    startActivity(intent);
                }else {
                    Toast.makeText(TMessActivity.this,"请勾选协议",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = Api.post_getQes(37,1);
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data").getJSONObject("exam");

                    dur =jsonObject1.getLong("duration");
                    String title =jsonObject1.getString("title") +jsonObject1.getString("subtitle");
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd mm:ss",
                            Locale.getDefault());
                    long t1 =jsonObject1.getLong("earliestStartTime");
                    String time11 = sdf.format(t1);
                    long t2 =jsonObject1.getLong("latestStartTime");
                    String time22 = sdf.format(t2);
                    duration.post(new Runnable() {
                        @Override
                        public void run() {
                            duration.setText(dur/1000/60 +"分钟");
                        }
                    });
                    tv1.post(new Runnable() {
                        @Override
                        public void run() {
                            tv1.setText(title);
                        }
                    });
                    time1.post(new Runnable() {
                        @Override
                        public void run() {
                            time1.setText(time11);
                        }
                    });
                    time2.post(new Runnable() {
                        @Override
                        public void run() {
                            time2.setText(time22);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}