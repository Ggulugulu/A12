package com.example.a12.activity;

import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a12.R;
import com.example.a12.adapter.CalAdapter;
import com.example.a12.bean.CalBean;
import com.example.a12.view.CustomCalendarView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalenderActivity extends AppCompatActivity {

    private CustomCalendarView calendarView;
    private TextView tv_date;
    private ImageView up,down;
    RecyclerView recyclerView;
    CalAdapter adapter;
    private List<CalBean> mList = new ArrayList<CalBean>();
    Time time = new Time("GMT+8");
    int year,month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        recyclerView = findViewById(R.id.rel_calender);


        time.setToNow();
        year = time.year;
        month = time.month+1;

        calendarView = findViewById(R.id.calendarView);
        calendarView.setYear(year);
        calendarView.setMonth(month);

        tv_date = findViewById(R.id.tv_date);
        tv_date.setText(calendarView.getYear() +"-" +calendarView.getMonth());

        up =findViewById(R.id.iv_last_month);
        down = findViewById(R.id.iv_next_month);

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(month ==1){
                    year--;
                    month = 13;
                }
                month--;
                calendarView.setMonth(month);
                calendarView.setYear(year);
                tv_date.setText(calendarView.getYear() +"-" +calendarView.getMonth());
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(month ==12){
                    year++;
                    month=0;
                }
                month++;
                calendarView.setMonth(month);
                calendarView.setYear(year);
                tv_date.setText(calendarView.getYear() +"-" +calendarView.getMonth());
            }
        });

        calendarView.setOnCalendarDayClickListener(new CustomCalendarView.OnCalendarDayClickListener() {
            @Override
            public void onCalendarDayClick(CustomCalendarView calendarView, CustomCalendarView.Day check) {
                initMess(check.getDate());
                setRel(recyclerView);
            }
        });

    }

    private void initMess(String date) {
        mList.clear();
        for (int i = 0; i < 1; i++) {
            CalBean bean = new CalBean();
            bean.setTe("2022马克思主义原理试题");
            bean.setPlace("10-111");
            bean.setLast("1");
            bean.setTime(date);
            mList.add(bean);
            CalBean bean1 = new CalBean();
            bean1.setTe("2022毛泽东思想理论试题");
            bean1.setPlace("2-132");
            bean1.setLast("1");
            bean1.setTime(date);
            mList.add(bean1);
        }
    }

    private void setRel(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CalAdapter(mList);
        recyclerView.setAdapter(adapter);
    }


}