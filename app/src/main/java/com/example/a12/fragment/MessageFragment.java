package com.example.a12.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a12.R;
import com.example.a12.adapter.MessageAdapter;
import com.example.a12.module_style.MessageBean;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MessageFragment extends Fragment {

    private List<MessageBean> mList = new ArrayList<MessageBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getContext(),R.layout.fragment_message,null);
        initBean();
        BeanRecy(view);
        return view;
    }
    private void BeanRecy(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_message);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        MessageAdapter adapter = new MessageAdapter(mList);
        recyclerView.setAdapter(adapter);
    }

    private void initBean() {
        for (int i = 0; i < 3; i++) {
            MessageBean bean = new MessageBean();
            bean.setWangming("小黄包");
            bean.setNeirong("乌拉乌拉乌拉乌拉！");
            bean.setTime("2019年3月22日 16:23");
            mList.add(bean);
        }

    }
}