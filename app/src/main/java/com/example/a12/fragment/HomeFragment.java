package com.example.a12.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a12.R;
import com.example.a12.home_fragment.homeslide_01;
import com.example.a12.home_fragment.homeslide_02;
import com.example.a12.home_fragment.homeslide_03;
import com.example.a12.home_fragment.homeslide_04;
import com.example.a12.home_fragment.homeslide_05;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


public class HomeFragment extends Fragment {
    ViewPager2 viewPager2;
    TabLayout tab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getContext(),R.layout.fragment_home,null);
        viewPager2 = view.findViewById(R.id.viewPager2);
        tab = view.findViewById(R.id.home_slide);

        viewPager2.setAdapter(new FragmentStateAdapter(getActivity()) {
            @NonNull
            @org.jetbrains.annotations.NotNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:
                        homeslide_01 h01 = new homeslide_01();
                        return  h01;
                    case 1:
                        homeslide_02 h02 = new homeslide_02();
                        return  h02;
                    case 2:
                        homeslide_03 h03 = new homeslide_03();
                        return  h03;
                    case 3:
                        homeslide_04 h04 = new homeslide_04();
                        return  h04;
                    case 4:
                        homeslide_05 h05 = new homeslide_05();
                        return  h05;

                }
                return null;
            }

            @Override
            public int getItemCount() {
                return 5;
            }
        });

        TabLayoutMediator tm =new TabLayoutMediator(tab, viewPager2, (tab, position) -> {
            if(position == 0){
                tab.setText("常用");
            }else if(position ==1){
                tab.setText("门户");
            }else if(position ==2){
                tab.setText("关注");
            }else if(position ==3){
                tab.setText("社区");
            }else{
                tab.setText("分析");
            }

        });
        tm.attach();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}