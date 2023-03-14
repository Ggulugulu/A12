package com.example.a12.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a12.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SureNoTitleDialog extends DialogFragment implements View.OnClickListener {
    private CallBack mCallBack;

    private String contentStr;
    private String strRight, strLeft;

    private boolean isShowOneBtn = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.PopDialogTheme3);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(setLayoutId(), container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        setListener();
    }
    /**
     * 初始化数据
     */
    protected void initData() {
    }
    /**
     * 设置监听
     */
    protected void setListener() {

    }

    public int setLayoutId() {
        return R.layout.dialog_sure;
    }

    public SureNoTitleDialog setClickListener(CallBack callBack) {
        mCallBack = callBack;
        return this;
    }

    protected void initView(View view) {
        TextView mTvLeft = view.findViewById(R.id.tv_cancel);
        TextView mTvRight = view.findViewById(R.id.tv_sure);
        View viewLine = view.findViewById(R.id.view);
        TextView mTvContent = view.findViewById(R.id.tv_message);
        if (contentStr != null) {
            mTvContent.setText(contentStr);
        }
        if (strRight != null) {
            mTvRight.setText(strRight);
        }
        if (strLeft != null) {
            mTvLeft.setText(strLeft);
        }

        if (isShowOneBtn) {
            mTvLeft.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        }

        mTvLeft.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
    }

    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void dismissDialog(FragmentManager fragmentManager) {
        String className = this.getClass().getSimpleName();
        Fragment prev = fragmentManager.findFragmentByTag(className);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }
    /**
     * Dialog设置dialog的message
     *
     * @param message
     */
    public SureNoTitleDialog setMessage(String message) {
        contentStr = message;
        return this;
    }

    /**
     * @param strRight
     * @param strLeft
     */
    public SureNoTitleDialog setAllBtnText(String strRight, String strLeft) {
        this.strRight = strRight;
        this.strLeft = strLeft;
        return this;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_cancel) {
            if (mCallBack != null) {
                mCallBack.onLeftClick(SureNoTitleDialog.this, v);
            }
        } else if (v.getId() == R.id.tv_sure) {
            if (mCallBack != null) {
                mCallBack.onRightClick(SureNoTitleDialog.this, v);
            }
        }
    }

    public void showDialog(FragmentManager supportFragmentManager) {
        try {
            supportFragmentManager.beginTransaction().remove(this).commit();
            String className = this.getClass().getSimpleName();
            this.show(supportFragmentManager, className);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface CallBack {
        void onRightClick(SureNoTitleDialog dialog, View v);
        void onLeftClick(SureNoTitleDialog dialog, View v);
    }
}
