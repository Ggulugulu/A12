package com.example.a12.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.a12.R;
import com.example.a12.activity.TestActivity;
import com.example.a12.adapter.ExamCardAdapter;
import com.example.a12.event.MessageEvent;
import com.example.a12.module_datas.HomeworkAnswerBean;
import com.example.a12.module_datas.QuestionBean;
import com.example.a12.widget.FixHeightGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


/**
 * 题卡
 */
public class HomeworkCardFragment extends DialogFragment {

    private LinearLayout ll1;
    private FixHeightGridView gridView;

    private TestActivity activity;

    private ArrayList<QuestionBean> questionList;
    private ArrayList<HomeworkAnswerBean> answerList;

    private ExamCardAdapter mAdapter;

    //显示 交卷
    private boolean isShowSubmit = true;
    //是否展示答题结果
    private boolean isShowResult = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog);
        activity = (TestActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_card, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        if (getArguments() != null) {
            isShowSubmit = getArguments().getBoolean("isShowSubmit", true);
        }
        questionList = activity.mQuestionList;
        answerList = activity.answerList;
//        isShowResult = activity.mDoType == HOMEWORK_PARSE;

        ll1 = view.findViewById(R.id.ll_1);

        gridView = view.findViewById(R.id.grid_view);

        if (isShowSubmit) {
            ll1.setVisibility(View.VISIBLE);
        }

    }

    private void initData() {
        mAdapter = new ExamCardAdapter(getContext(), questionList, answerList, isShowResult);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new MessageEvent<>(position, MessageEvent.EXAM_CARD_JUMP));
                dismiss();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //点击外部消失
        dialog.setCanceledOnTouchOutside(true);

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        Display display = window.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        params.height = (int) (height * 0.8f);

        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
}
