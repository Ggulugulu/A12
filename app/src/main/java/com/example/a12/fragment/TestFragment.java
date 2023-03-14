package com.example.a12.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a12.R;
import com.example.a12.test_fragment.testslide_01;
import com.example.a12.test_fragment.testslide_02;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class TestFragment extends Fragment {
    ViewPager2 viewPager2;
    TabLayout tab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_test, null);
        viewPager2 = view.findViewById(R.id.viewPager2);
        tab = view.findViewById(R.id.test_slide);

        viewPager2.setAdapter(new FragmentStateAdapter(getActivity()) {
            @NonNull
            @org.jetbrains.annotations.NotNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        testslide_01 t01 = new testslide_01();
                        return t01;
                    case 1:
                        testslide_02 t02 = new testslide_02();
                        return t02;

                }
                return null;
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        TabLayoutMediator tm = new TabLayoutMediator(tab, viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("全部考试");
            } else {
                tab.setText("已结束");
            }

        });
        tm.attach();

        return view;
    }
}