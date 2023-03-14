package com.example.a12.activity;

import android.os.Bundle;

import com.example.a12.R;
import com.example.a12.view.RoundView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    RoundView round01, round02, round03, round04, round05, round06;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        round01 = findViewById(R.id.round_01);
        round02 = findViewById(R.id.round_02);
        round03 = findViewById(R.id.round_03);
        round04 = findViewById(R.id.round_04);
        round05 = findViewById(R.id.round_05);
        round06 = findViewById(R.id.round_06);

        round01.setPercent(65);
        round02.setPercent(74);
        round03.setPercent(50);
        round04.setPercent(60);
        round05.setPercent(63);
        round06.setPercent(82);
    }
}