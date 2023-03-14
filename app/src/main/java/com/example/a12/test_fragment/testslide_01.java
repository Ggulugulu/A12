package com.example.a12.test_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a12.R;
import com.example.a12.activity.ResultActivity;
import com.example.a12.activity.TMessActivity;
import com.example.a12.adapter.TestAdapter;
import com.example.a12.module_style.TestBean;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class testslide_01 extends Fragment {

    private List<TestBean> mList = new ArrayList<TestBean>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_testslide_01, container, false);
        initBean();
        BeanRecy(view);
        return view;
    }

    private void BeanRecy(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_test);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        TestAdapter adapter = new TestAdapter(mList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(MyItemClickListener);
    }

    private void initBean() {
        for (int i = 0; i < 1; i++) {
            TestBean bean = new TestBean();
            bean.setTitle("政治考试期末考试");
            bean.setCount("共16题");
            bean.setTime("2022.02.20");
            bean.setResult("77分");
            mList.add(bean);
            TestBean bean2 = new TestBean();
            bean2.setTitle("2021近代史纲要试题");
            bean2.setCount("共130题");
            bean2.setTime("2020.06.09");
            bean2.setResult("91分");
            mList.add(bean2);
        }
    }
    private TestAdapter.OnItemClickListener MyItemClickListener = new TestAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()) {
                case R.id.ll_test:
                    TextView tv = v.findViewById(R.id.tv_result);
                    if(tv.getText().toString() != "未开始"){
                        final Intent intent=new Intent(getActivity(), ResultActivity.class);
                        startActivity(intent);
                    }else{
                        final Intent intent=new Intent(getActivity(), TMessActivity.class);
                        startActivity(intent);
                    }

                    break;
            }
        }
    };

}
