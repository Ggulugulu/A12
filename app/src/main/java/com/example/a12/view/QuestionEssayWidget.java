package com.example.a12.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;

import com.example.a12.R;
import com.example.a12.activity.TestActivity;
import com.example.a12.event.MessageEvent;
import com.example.a12.module_datas.QuestionBean;
import com.example.a12.widget.BaseQuestionWidget;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * @Description 简答题
 */
public class QuestionEssayWidget extends BaseQuestionWidget {

    private EditText etReply;

    public QuestionEssayWidget(Context context) {
        super(context);
    }

    public QuestionEssayWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(QuestionBean question, int index, int totalNum) {
        super.setData(question, index, totalNum);
        invalidateData();
    }

    @Override
    protected void initView(AttributeSet attrs) {

    }

    @Override
    protected void invalidateData() {
        etReply = findViewById(R.id.et_reply);
        etReply.addTextChangedListener(onTextChangedListener);

        super.invalidateData();

        //正确解析
        Context context = etReply.getContext();
        if (context instanceof TestActivity) {
            TestActivity testpaperActivity = (TestActivity) getContext();
//            if (testpaperActivity.mDoType == HOMEWORK_PARSE && mChildQuestion.getResult() != null) {// doType 字段意思是练习类型
            if ( mChildQuestion.getResult() != null) {
                etReply.setEnabled(false);
                mAnalysisVS = this.findViewById(R.id.quetion_choice_analysis);
                mAnalysisVS.setOnInflateListener(new ViewStub.OnInflateListener() {
                    @Override
                    public void onInflate(ViewStub viewStub, View view) {
                        initResultAnalysis(view);
                        etReply.setText(mChildQuestion.getResult().answer.get(0));
                    }
                });
                mAnalysisVS.inflate();

            }
        }
    }

    @Override
    protected void restoreResult(ArrayList<String> resultData) {
        etReply.setText(resultData.get(0));

    }


    private TextWatcher onTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int index, int i2, int i3) {
            Bundle bundle = new Bundle();
            bundle.putInt("index", mIndex - 1);
            //bundle.putSerializable("HomeworkQuestionTypeBean", mChildQuestion.getType());
            bundle.putSerializable("QuestionType", mChildQuestion.getType());
            ArrayList<String> data = new ArrayList<>();
            data.add(etReply.getText().toString());

            bundle.putStringArrayList("data", data);
            EventBus.getDefault().post(new MessageEvent<>(bundle, MessageEvent.EXAM_CHANGE_ANSWER));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
