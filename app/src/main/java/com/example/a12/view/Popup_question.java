package com.example.a12.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a12.R;
import com.example.a12.utils.CommonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Popup_question extends Activity implements OnClickListener {
    private LinearLayout layout,dy_num;
    List<String> numlist = Arrays.asList("1", "2", "3","1", "2", "3","1","1", "2", "3","1", "2", "3","1");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_question);

        layout=findViewById(R.id.pop_layout);
        dy_num =findViewById(R.id.dynamic_single_choice);

        dynamicCreate();
    }


    private void dynamicCreate() {
        int size = numlist.size();
        //每行的水平LinearLayout
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,0,0);
        ArrayList<TextView> childs = new ArrayList<>();
        //记录加载的个数
        int totoalNum = 0;
        //WindowManager方法获取屏幕的宽度
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        Log.d("width", String.valueOf(width));
        //动态创建
        for (int i = 0;i < size ;i++){
            String item = numlist.get(i);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView child = (TextView) LayoutInflater.from(this).inflate(R.layout.dynamic_num,null);
            itemParams.width = CommonUtil.dp2px(this,29);
            itemParams.height = CommonUtil.dp2px(this,29);
            itemParams.topMargin = CommonUtil.dp2px(this,7);
            itemParams.bottomMargin = CommonUtil.dp2px(this,7);
            itemParams.rightMargin = CommonUtil.dp2px(this,14);
            itemParams.gravity= Gravity.CENTER;

            child.setText(item);
            child.setTextSize(12);
            child.setLayoutParams(itemParams);

            totoalNum++;

            //题号点击
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =(Integer)v.getTag();
                    Intent intent = new Intent();
                    intent.putExtra("result", position);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            //添加tag值
            child.setTag(i);

            childs.add(child);
            //换行
            if (totoalNum > 7){
                LinearLayout horizLL = new LinearLayout(this);
                horizLL.setOrientation(LinearLayout.HORIZONTAL);
                horizLL.setLayoutParams(layoutParams);
                for (TextView addBtn:childs){
                    horizLL.addView(addBtn);
                }
                dy_num.addView(horizLL);
                childs.clear();
                totoalNum = 0;
            }
        }
        //最后一行添加一下
        if (!childs.isEmpty()){
            LinearLayout horizLL = new LinearLayout(this);
            horizLL.setOrientation(LinearLayout.HORIZONTAL);
            horizLL.setLayoutParams(layoutParams);
            for (TextView addBtn:childs){
                horizLL.addView(addBtn);
            }
            dy_num.addView(horizLL);
            childs.clear();
            totoalNum = 0;
        }
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();//如果不想点击外部关闭弹窗去掉此行代码即可
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
        finish();
    }


}