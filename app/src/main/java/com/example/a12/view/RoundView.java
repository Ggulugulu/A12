package com.example.a12.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.a12.R;
import com.example.a12.utils.CommonUtil;

import androidx.core.content.ContextCompat;

public class RoundView extends View {
    //圆的半径
    private float mRadius;
    //色带的宽度
    private float mStripeWidth;
    //总体大小
    private int mHeight;
    private int mWidth;
    //动画位置百分比进度
    private int mCurPercent = 10;
    //圆心坐标
    private float x;
    private float y;

    //要画的弧度
    private int mEndAngle;
    //圆弧的颜色
    private int mAngleColor = 0xff6579EC; // 默认颜色
    //圆的颜色
    private int mCircleColor = 0xffE7EBEE;

    //中心百分比文字大小
    private float mCenterTextSize;

    // 大圆画笔
    private Paint bigCirclePaint;
    private Paint sectorPaint;
    private Paint smallCirclePaint;
    private Paint textPaint;
    private RectF rect;
    // 将前面二个构造函数都指向第三个
    public RoundView(Context context) {
        this(context, null);
    }

    // XML中使用此构造函数
    public RoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 默认值，色带宽度，文字大小等
        mStripeWidth = CommonUtil.dp2px(context,15);
        mCenterTextSize = CommonUtil.dp2px(context,11);
        mRadius = CommonUtil.dp2px(context,100);

        //绘制大圆画笔属性设置
        bigCirclePaint = new Paint();
        bigCirclePaint.setAntiAlias(true); // 抗锯齿
        bigCirclePaint.setColor(mCircleColor); // 设置大圆颜色

        //绘制小圆画笔属性设置
        smallCirclePaint = new Paint();
        smallCirclePaint.setAntiAlias(true);
        smallCirclePaint.setColor(Color.WHITE);

        // 饼状图属性
        rect = new RectF();
        sectorPaint = new Paint();
        sectorPaint.setColor(mAngleColor);
        sectorPaint.setAntiAlias(true);

        // 文字画笔属性
        textPaint = new Paint();
        textPaint.setColor(ContextCompat.getColor(context, R.color.resultText));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取测量大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // match_parent
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            mRadius = widthSize / 2;
            x = widthSize / 2;
            y = heightSize / 2;
            mWidth = widthSize;
            mHeight = heightSize;
        }

        // warp_content
        // 如果是自适应，则取默认值
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            mWidth = (int) (mRadius * 2);
            mHeight = (int) (mRadius * 2);
            x = mRadius;
            y = mRadius;
        }
        setMeasuredDimension(mWidth, mHeight);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        mEndAngle = (int) (mCurPercent * 3.6);

        // 大圆
        canvas.drawCircle(x, y, mRadius, bigCirclePaint); // 通过canvas画圆

        //饼状图
        rect.right = mWidth;
        rect.bottom = mHeight;
        //参数说明见知识补充
        canvas.drawArc(rect, 270, mEndAngle, true, sectorPaint);

        // 小圆
        canvas.drawCircle(x, y, mRadius - mStripeWidth, smallCirclePaint);

        //绘制文本
        String text = mCurPercent + "%";
        textPaint.setTextSize(mCenterTextSize);
        float textLength = textPaint.measureText(text);
        canvas.drawText(text, x - textLength / 2, y, textPaint);
    }

    // 设置圆的颜色
    public void setCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
        smallCirclePaint.setColor(mCircleColor);
        bigCirclePaint.setColor(mCircleColor);
        // 此方法是为了使布局生效
        invalidate();
    }

    // 设置圆弧的颜色
    public void setAngleColor(int mAngleColor) {
        this.mAngleColor = mAngleColor;
        sectorPaint.setColor(mAngleColor);
        invalidate();
    }

    // 设置色带宽度
    public void setStripeWidth(float mStripeWidth) {
        this.mStripeWidth = mStripeWidth;
        invalidate();
    }

    // 设置字体颜色
    public void setCenterTextColor(int color) {
        textPaint.setColor(color);
        invalidate();
    }

    // 设置字体大小
    public void setmCenterTextSize(float mCenterTextSize) {
        this.mCenterTextSize = mCenterTextSize;
        invalidate();
    }

    // 外部设置百分比数
    public void setPercent(int percent) {
        if (percent > 100 || percent < 0) {
            throw new IllegalArgumentException("percent must more than 0 and less than 100!");
        }
        mCurPercent = percent;
        invalidate();
    }
}
