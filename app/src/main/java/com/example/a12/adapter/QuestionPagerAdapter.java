package com.example.a12.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.a12.R;
import com.example.a12.module_datas.QuestionBean;
import com.example.a12.module_datas.QuestionTypeBean;
import com.example.a12.widget.BaseQuestionWidget;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


public class QuestionPagerAdapter extends PagerAdapter {
    protected LayoutInflater inflater;
    protected Context mContext;
    protected ArrayList<QuestionBean> mList;


    public QuestionPagerAdapter(Context context, ArrayList<QuestionBean> list) {
        mList = list;
        mContext = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        QuestionBean questionBean = mList.get(position);

        View view = switchQuestionWidget(questionBean, position + 1, mList.size());
        ScrollView scrollView = new ScrollView(mContext);
        scrollView.setFillViewport(true);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        container.addView(scrollView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return scrollView;
    }


    private View switchQuestionWidget(QuestionBean question, int index, int totalNum) {
        BaseQuestionWidget mWidget;
        QuestionTypeBean typeBean = question.getType();
        if (typeBean == QuestionTypeBean.material) {
            typeBean = question.getItems().get(0).getType();
        }
        int layoutId = 0;
        switch (typeBean) {
            case choice://多选
            case uncertain_choice://不定向
                layoutId = R.layout.item_pager_homework_question_choice;
                break;
            case single_choice://单选题
                layoutId = R.layout.item_pager_homework_question_singlechoice;
                break;
            case determine://判断item_pager_homework_question_determine.xml题
                layoutId = R.layout.item_pager_homework_question_determine;
                break;
            case fill:
                layoutId = R.layout.item_pager_homework_question_fill;
                break;
            case essay:
                layoutId = R.layout.item_pager_homework_question_essay;
                break;
            default:
                break;
        }
        mWidget = (BaseQuestionWidget) LayoutInflater.from(mContext).inflate(layoutId, null);
        mWidget.setData(question, index, totalNum);
        return mWidget;
    }


}
