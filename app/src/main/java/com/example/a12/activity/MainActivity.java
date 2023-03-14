package com.example.a12.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a12.R;
import com.example.a12.fragment.HomeFragment;
import com.example.a12.fragment.MeFragment;
import com.example.a12.fragment.MessageFragment;
import com.example.a12.fragment.TestFragment;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends FragmentActivity {
    //首页滑动fragment集合
    FrameLayout mFlMain ;
    private static FragmentTransaction ft;

    //首页
    private HomeFragment mHomeFragment;

    //消息
    private MessageFragment mMessageFragment;

    //考试
    private TestFragment mTestFragment;

    //我的
    private MeFragment mMeFragment;

    private ImageView mHome;
    private TextView mTHome;
    private ImageView mMessage;
    private TextView mTMessage;
    private ImageView mTest;
    private TextView mTTest;
    private ImageView mMe;
    private TextView mTMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlMain = findViewById(R.id.fl_main);


        //首页
        mHome =findViewById(R.id.home);
        mTHome =findViewById(R.id.tv_home);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setselect(0);
            }
        });


        //消息
        mMessage =findViewById(R.id.message);
        mTMessage =findViewById(R.id.tv_message);
        mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setselect(1);
            }
        });

        //考试
        mTest =findViewById(R.id.test);
        mTTest =findViewById(R.id.tv_test);
        mTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setselect(2);
            }
        });

        //我的
        mMe =findViewById(R.id.me);
        mTMe =findViewById(R.id.tv_me);
        mMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setselect(3);
            }
        });
        setselect(0);
    }


    //提供页面显示
    public void setselect(int i) {
        ft = getSupportFragmentManager().beginTransaction();
        //隐藏其他页面
        hideFragments();
        //重置图标状态
        resetTab();

        switch (i) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.fl_main, mHomeFragment);
                }
                //显示当前页面
                ft.show(mHomeFragment);

                //改变图标点击效果
                mHome.setImageResource(R.drawable.home_blue);
                mTHome.setTextColor(getResources().getColor(R.color.home_blue));
                break;

            case 1:
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    ft.add(R.id.fl_main, mMessageFragment);
                }
                //显示当前页面
                ft.show(mMessageFragment);

                //改变图标点击效果
                mMessage.setImageResource(R.drawable.message_blue);
                mTMessage.setTextColor(getResources().getColor(R.color.home_blue));
                break;

            case 2:
                if (mTestFragment == null) {
                    mTestFragment = new TestFragment();
                    ft.add(R.id.fl_main, mTestFragment);
                }
                //显示当前页面
                ft.show(mTestFragment);

                //改变图标点击效果
                mTest.setImageResource(R.drawable.test_blue);
                mTTest.setTextColor(getResources().getColor(R.color.home_blue));
                break;

            case 3:
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                    ft.add(R.id.fl_main, mMeFragment);
                }
                //显示当前页面
                ft.show(mMeFragment);

                //改变图标点击效果
                mMe.setImageResource(R.drawable.me_blue);
                mTMe.setTextColor(getResources().getColor(R.color.home_blue));
                break;
        }
        ft.commit();
    }

    private void hideFragments() {
        if (mHomeFragment != null) {
            ft.hide(mHomeFragment);
        }

        if (mMessageFragment != null) {
            ft.hide(mMessageFragment);
        }
        if (mTestFragment != null) {
            ft.hide(mTestFragment);
        }
        if (mMeFragment != null) {
            ft.hide(mMeFragment);
        }
    }

    private void resetTab() {
        mHome.setImageResource(R.drawable.home_gray);
        mTHome.setTextColor(getResources().getColor(R.color.home_gary));
        mMessage.setImageResource(R.drawable.message_gray);
        mTMessage.setTextColor(getResources().getColor(R.color.home_gary));
        mTest.setImageResource(R.drawable.test_gray);
        mTTest.setTextColor(getResources().getColor(R.color.home_gary));
        mMe.setImageResource(R.drawable.me_gray);
        mTMe.setTextColor(getResources().getColor(R.color.home_gary));
    }

}