package com.example.a12.home_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.a12.R;
import com.example.a12.activity.CalenderActivity;
import com.example.a12.activity.ResultActivity;

import androidx.fragment.app.Fragment;

public class homeslide_01 extends Fragment {
    LinearLayout calernder,result;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_homeslide_01, container, false);

        calernder = view.findViewById(R.id.ll_slide01_1);
        result = view.findViewById(R.id.ll_slide01_2);

        calernder.setOnClickListener(mClickListener);
        result.setOnClickListener(mClickListener);
        return view;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_slide01_1:
                    startActivity(new Intent(getActivity(), CalenderActivity.class));
                    break;
                case R.id.ll_slide01_2:
                    startActivity(new Intent(getActivity(), ResultActivity.class));
                    break;
            }
        }
    };
}
