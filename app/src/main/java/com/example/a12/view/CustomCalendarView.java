package com.example.a12.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class CustomCalendarView extends View{
    //星期文字颜色
    private int weekTextColor = Color.parseColor("#111111");
    //星期文字大小
    private int weekTextSize = (int) (getResources().getDisplayMetrics().density * 14);
    //年
    private int year = 2022;
    //月
    private int month = 7;
    //天文字大小
    private int monthDayTextSize = (int) (getResources().getDisplayMetrics().density * 15);
    //上个月天文字颜色
    private int lastMonthDayTextColor = Color.parseColor("#CFCFCF");
    //当前月天文字颜色
    private int nowMonthDayTextColor = Color.parseColor("#111111");
    //下个月天文字颜色
    private int nextMonthDayTextColor = Color.parseColor("#CFCFCF");
    //单位宽度
    private int unitWidth;
    //单位高度
    private int unitHeight;
    //日历天
    private List<Day> days;
    //是否显示今天
    private boolean showToday = true;
    //当天颜色
    private int nowDayCircleColor = Color.parseColor("#1379D3");
    //选中颜色
    private int checkDayCircleColor = Color.parseColor("#1982F3");
    //选中天
    private Day checkDay;


    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        unitWidth = getMeasuredWidth() / getCalendarTitle().length;
        unitHeight = (int) (unitWidth * 0.80F);
        int requiredWidth = getMeasuredWidth();
        int requiredHeight = unitHeight * 7 + unitHeight / 2;
        int measureSpecWidth = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureSpecHeight = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureWidth = measureSpecWidth;
        int measureHeight = measureSpecHeight;
        if ((widthSpecMode == MeasureSpec.AT_MOST || widthSpecMode == MeasureSpec.UNSPECIFIED)
                && heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            measureWidth = requiredWidth;
            measureHeight = requiredHeight;
        } else if (widthSpecMode == MeasureSpec.AT_MOST || widthSpecMode == MeasureSpec.UNSPECIFIED) {
            measureWidth = requiredWidth;
            measureHeight = measureSpecHeight;
        } else if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            measureWidth = measureSpecWidth;
            measureHeight = requiredHeight;
        }
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                checkDay = findTouchDay(event.getX(), event.getY());
                if (checkDay != null) {
                    if (onCalendarDayClickListener != null) {
                        onCalendarDayClickListener.onCalendarDayClick(this, checkDay);
                    }
                    invalidate();
                }
                break;
        }
        return true;
    }

    /**
     * 找到点击的day
     *
     * @param x 坐标X
     * @param y 坐标y
     * @return
     */
    private Day findTouchDay(float x, float y) {
        for (int i = 0; i < days.size(); i++) {
            float dayX = days.get(i).getX();
            float dayY = days.get(i).getY();
            if (x < dayX + unitHeight / 2 && x > dayX - unitHeight / 2 && y < dayY + unitHeight / 2 && y > dayY - unitHeight / 2) {
                return days.get(i);
            }
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWeekText(canvas);
        drawDayOfMonth(canvas, year, month);
    }

    /**
     * 绘制星期字体
     *
     * @param canvas 画布
     */
    private void drawWeekText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(weekTextSize);
        paint.setColor(weekTextColor);
        String weekNames[] = getCalendarTitle();
        for (int i = 0; i < weekNames.length; i++) {
            String weekName = weekNames[i];
            Rect bounds = new Rect();
            paint.getTextBounds(weekName, 0, weekName.length(), bounds);
            canvas.drawText(weekName, unitWidth * i + (unitWidth / 2.0F - bounds.width() / 2.0F), unitWidth / 2.0F, paint);
        }
    }

    /**
     * 绘制对应月份的天数
     *
     * @param canvas 画布
     * @param year   年
     * @param month  月
     */
    private void drawDayOfMonth(Canvas canvas, int year, int month) {
        Time time = new Time("GMT+8");
        time.setToNow();
        int ye = time.year;
        int m = time.month+1;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(monthDayTextSize);
        //绘制上个月的天数
        days = getCalendarDays(year, month);
        int nowDay = getNowDay();
        for (int i = 0; i < days.size(); i++) {
            int row = i / 7;
            int position = i % 7;
            int type = days.get(i).getType();
            int day = days.get(i).getDay();
            if (type == Day.LAST_MONTH) {
                paint.setColor(lastMonthDayTextColor);
                days.get(i).setDate(formatDate(year, month - 1, day));
            }
            if (type == Day.NOW_MONTH) {
                paint.setColor(nowMonthDayTextColor);
                days.get(i).setDate(formatDate(year, month, day));
            }
            if (type == Day.NEXT_MONTH) {
                paint.setColor(nextMonthDayTextColor);
                days.get(i).setDate(formatDate(year, month + 1, day));
            }
            days.get(i).setTime(parseDate(days.get(i).getDate()).getTime());
            String dayText = String.valueOf(days.get(i).getDay());
            Rect bounds = new Rect();
            paint.getTextBounds(dayText, 0, dayText.length(), bounds);
            float x = unitWidth * (position) + (unitWidth / 2.0F - bounds.width() / 2.0F);
            float y = unitWidth + unitHeight * row + (unitHeight / 2.0F - bounds.height() / 2.0F);
            days.get(i).setX(x);
            days.get(i).setY(y);
            days.get(i).setPosition(i);
            //圆坐标
            float cx = x + bounds.width() / 2.0F;
            float cy = y - bounds.height() / 2.0F;
            //当前天
            if (showToday && type == Day.NOW_MONTH && day == nowDay && month ==m && year==ye) {
                drawDayCircle(canvas, cx, cy, nowDayCircleColor);
                paint.setColor(Color.WHITE);
            }
            //选中天   !intervalSelect
            if (checkDay != null && type == checkDay.getType() && day == checkDay.getDay()) {
                drawDayCircle(canvas, cx, cy, checkDayCircleColor);
                //paint.setColor(Color.WHITE);
            }
            canvas.drawText(dayText, x, y, paint);
        }
    }

    /**
     * 格式化日期
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return
     */
    public String formatDate(int year, int month, int day) {
        DecimalFormat format = new DecimalFormat("00");
        return year + "-" + format.format(month) + "-" + format.format(day);
    }

    /**
     * 转换时间
     *
     * @param date
     * @return
     */
    public Date parseDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 绘制天圆背景
     *
     * @param canvas 画布
     * @param cx     中心x
     * @param cy     中心y
     * @param color  颜色
     */
    private void drawDayCircle(Canvas canvas, float cx, float cy, int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        //若为选中的日子
        if (color == checkDayCircleColor){
            paint.setStrokeWidth(5f);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(cx, cy, unitHeight / 2 - 10, paint);
        }else{
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, unitHeight / 2 - 10, paint);
        }
    }

    /**
     * 找到上个月需要显示的天数
     *
     * @param year  年
     * @param month 月
     * @return
     */
    public int[] getLastMonthEndDays(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        int nowMonth = month - 1;
        calendar.set(Calendar.MONTH, nowMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //月第一天周几
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //上个月需要显示的天数，如果日历需要第一天是日就-2;
        int lastMonthDayCount = week - 1;
        lastMonthDayCount = lastMonthDayCount == -1 ? 6 : lastMonthDayCount;
        int[] days = new int[lastMonthDayCount];
        //设置为上一个月
        calendar.set(Calendar.MONTH, nowMonth - 1);
        //获取上一个月天数
        int maximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = maximum; i > maximum - lastMonthDayCount; i--) {
            days[maximum - i] = i;
        }
        Arrays.sort(days);
        return days;
    }

    /**
     * 获取当前月天数
     *
     * @param year  年
     * @param month 月
     * @return
     */
    public int[] getNowMonthDays(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //月第一天周几
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //月天数
        int maximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int[] days = new int[maximum];
        for (int i = 0; i < maximum; i++) {
            days[i] = i + 1;
        }
        return days;
    }

    /**
     * 获取下个月显示的天数
     *
     * @param lastMonthDays 上个月天数
     * @param nowMonthDays  当前月天数
     * @return
     */
    public int[] geNextMonthStartDays(int lastMonthDays, int nowMonthDays) {
        //需求是6行7列
        int reqRows = 6, reqColumns = 7;
        int sumDays = lastMonthDays + nowMonthDays;
        int dayCount = reqRows * reqColumns - sumDays;
        int days[] = new int[dayCount];
        for (int i = 0; i < dayCount; i++) {
            days[i] = i + 1;
        }
        return days;
    }

    /**
     * 获取日历标题
     *
     * @return
     */
    public String[] getCalendarTitle() {
        return new String[]{"一", "二", "三", "四", "五", "六", "日"};
    }

    /**
     * 获取6行7列日历天数 "一", "二", "三", "四", "五", "六", "日"
     *
     * @param year  年
     * @param month 月
     * @return
     */
    public List<Day> getCalendarDays(int year, int month) {
        List<Day> days = new ArrayList<>();
        //上个月
        int lastMonthDays[] = getLastMonthEndDays(year, month);
        for (int i = 0; i < lastMonthDays.length; i++) {
            Day day = new Day();
            day.setType(Day.LAST_MONTH);
            day.setDay(lastMonthDays[i]);
            days.add(day);
        }
        //当前月
        int nowMonthDays[] = getNowMonthDays(year, month);
        for (int i = 0; i < nowMonthDays.length; i++) {
            Day day = new Day();
            day.setType(Day.NOW_MONTH);
            day.setDay(nowMonthDays[i]);
            days.add(day);
        }
        //下个月
        int nextMonthDays[] = geNextMonthStartDays(lastMonthDays.length, nowMonthDays.length);
        for (int i = 0; i < nextMonthDays.length; i++) {
            Day day = new Day();
            day.setType(Day.NEXT_MONTH);
            day.setDay(nextMonthDays[i]);
            days.add(day);
        }
        return days;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public int getNowDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 天实体
     */
    public class Day {

        /**
         * 上个月
         */
        public static final int LAST_MONTH = -1;
        /**
         * 当前月
         */
        public static final int NOW_MONTH = 0;
        /**
         * 下个月
         */
        public static final int NEXT_MONTH = 1;
        /**
         * 位置
         */
        private int position;
        /**
         * 坐标
         */
        float x, y;
        /**
         * 天
         */
        private int day;
        /**
         * 类型{@link #LAST_MONTH}
         */
        private int type;

        /**
         * 日期字符串
         */
        private String date;
        /**
         * 日期
         */
        private long time;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

    /**
     * 点击事件
     */
    private OnCalendarDayClickListener onCalendarDayClickListener;

    /**
     * 设置点击事件
     *
     * @param onCalendarDayClickListener
     */
    public void setOnCalendarDayClickListener(OnCalendarDayClickListener onCalendarDayClickListener) {
        this.onCalendarDayClickListener = onCalendarDayClickListener;
    }

    public interface OnCalendarDayClickListener {

        /**
         * 天点击事件
         *
         * @param calendarView 日历view
         * @param check        单选天
         *
         */
        void onCalendarDayClick(CustomCalendarView calendarView, Day check);

    }

    /**
     * 获取当前年
     *
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     * 获取当前月
     *
     * @return
     */
    public int getMonth() {
        return month;
    }

    /**
     * 重置选中
     */
    public void reset() {
//        startDay = null;
//        endDay = null;
        checkDay = null;
        invalidate();
    }

    /**
     * 是否显示今天
     *
     * @param showToday
     */
    public void setShowToday(boolean showToday) {
        this.showToday = showToday;
        invalidate();
    }

    /**
     * 设置选中
     *
     * @param checkDay
     */
    public void setCheckDay(Day checkDay) {
        this.checkDay = checkDay;
        invalidate();
    }

    /**
     * 获取选中天
     *
     * @return
     */
    public Day getCheckDay() {
        return checkDay;
    }

    /**
     * 设置年份
     *
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
        invalidate();
    }

    /**
     * 设置月份
     *
     * @param month
     */
    public void setMonth(int month) {
        this.month = month;
        invalidate();
    }

    /**
     * 设置年月
     *
     * @param month
     */
    public void setYearMonth(int year, int month) {
        this.year = year;
        this.month = month;
        invalidate();
    }

    /**
     * 设置天文字大小
     *
     * @param monthDayTextSize
     */
    public void setMonthDayTextSize(int monthDayTextSize) {
        this.monthDayTextSize = monthDayTextSize;
        invalidate();
    }

    /**
     * 设置上个月文字颜色
     *
     * @param lastMonthDayTextColor
     */
    public void setLastMonthDayTextColor(int lastMonthDayTextColor) {
        this.lastMonthDayTextColor = lastMonthDayTextColor;
        invalidate();
    }

    /**
     * 设置当前月文字颜色
     *
     * @param nowMonthDayTextColor
     */
    public void setNowMonthDayTextColor(int nowMonthDayTextColor) {
        this.nowMonthDayTextColor = nowMonthDayTextColor;
        invalidate();
    }

    /**
     * 设置下个月文字颜色
     *
     * @param nextMonthDayTextColor
     */
    public void setNextMonthDayTextColor(int nextMonthDayTextColor) {
        this.nextMonthDayTextColor = nextMonthDayTextColor;
        invalidate();
    }

    /**
     * 设置当天背景圆颜色
     *
     * @param nowDayCircleColor
     */
    public void setNowDayCircleColor(int nowDayCircleColor) {
        this.nowDayCircleColor = nowDayCircleColor;
        invalidate();
    }

    /**
     * 设置选择背景圆颜色
     *
     * @param checkDayCircleColor
     */
    public void setCheckDayCircleColor(int checkDayCircleColor) {
        this.checkDayCircleColor = checkDayCircleColor;
        invalidate();
    }


}
