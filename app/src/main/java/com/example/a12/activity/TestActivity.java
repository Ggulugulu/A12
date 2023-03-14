package com.example.a12.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a12.R;
import com.example.a12.adapter.QuestionPagerAdapter;
import com.example.a12.dialog.HomeworkCardFragment;
import com.example.a12.dialog.SureNoTitleDialog;
import com.example.a12.event.Api;
import com.example.a12.event.MessageEvent;
import com.example.a12.module_datas.HomeworkAnswerBean;
import com.example.a12.module_datas.QuestionBean;
import com.example.a12.module_datas.QuestionTypeBean;
import com.example.a12.utils.CommonUtil;
import com.example.a12.utils.FastClickUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Response;
public class TestActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static long lastClickTime;
    private static long endtime;

    //OpenCV的相机接口
    private JavaCameraView cameraView;
    private int containerWidth;
    private int containerHeight;
    float lastX, lastY;
    private Mat rgba;
    private String TAG="OPENCVCVCV";
    private SureNoTitleDialog mSubmitDialog,mTitleDialog;
    private static int Q_NUMBER = 1;


    public ArrayList<HomeworkAnswerBean> answerList = new ArrayList<>();
    public ArrayList<QuestionBean> mQuestionList = new ArrayList<>();
    private QuestionPagerAdapter mAdapter;
    private ViewPager viewPager;
    private RelativeLayout title,parentView;
    private TextView datika,up,down,checking;
    private int mCurrentViewID ;         //当前页面
    private int PAGER_NUM;//10个页面



    /**
     * 通过OpenCV管理Android服务，异步初始化OpenCV
     */
    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status){
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG,"OpenCV loaded successfully");
                    cameraView.enableView();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //setStartExamData(mQuestionList);
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG,"OpenCV library not found!");
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    @Subscribe
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //endtime = 3600000;
        endtime = getIntent().getLongExtra("end_time",0);

        EventBus.getDefault().register(this);
//        initOpenCV();
        viewPager = findViewById(R.id.test_viewpager);
        checking = findViewById(R.id.daojishi);
        title = findViewById(R.id.title);
        up = findViewById(R.id.q_up);
        down = findViewById(R.id.q_down);
        datika =findViewById(R.id.tv_datika);
        up.setOnClickListener(listener);
        down.setOnClickListener(listener);
        datika.setOnClickListener(listener);



        parentView = (RelativeLayout) title.getParent();

        cameraView = (JavaCameraView) findViewById(R.id.OpencvCameraView);
        cameraView.bringToFront();
        cameraView.setCvCameraViewListener(this);
        cameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        cameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);//前置摄像头
        cameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MetTouchLeftClick(event);
                return true;
            }
        });

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, 1);
        }

        try{
            init();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        CountDownTimer countDownTimer = new CountDownTimer(endtime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Log.d("time", String.valueOf(endtime));
                checking.setText(millisUntilFinished/1000/60 + "分"+(millisUntilFinished /1000)%60+"秒");
            }
            @Override
            public void onFinish() {
            }
        };
        countDownTimer.start();
    }

    /**
     * 倒计时
     */


    /**
     * 设置题目
     */
    private void init() throws InterruptedException {
        mAdapter = new QuestionPagerAdapter(TestActivity.this, mQuestionList);
        //加载试题
        getQes();
        initDialog();
        initListener();
        Thread.sleep(500);

        setStartExamData(mQuestionList);
        PAGER_NUM =mQuestionList.size();
//        // 问答题
//        for (int i = 0; i < 2; i++) {
//            Log.i("甲乙丙丁", String.valueOf(mQuestionList));
//            ArrayList<String> metas1 = new ArrayList<>();
//            QuestionBean homeworkQuestionBean2 = new QuestionBean();
//            homeworkQuestionBean2.type = QuestionTypeBean.essay;
//            homeworkQuestionBean2.setMetas(metas1);
//            homeworkQuestionBean2.setStem("甲、乙因合同纠纷申请仲裁，仲裁庭对案件裁决时两位仲裁员支持甲方的请求，但首席仲裁员支持乙的请求，关于该案件仲裁" +
//                    "裁决的下列表述中，符合法律规定的是（）。");
//
//            mQuestionList.add(homeworkQuestionBean2);
//        }
    }

    private void getQes() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = Api.post_getQes(37,1);
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    int errCode = jsonObject.getInt("errCode");
                    Log.i("quesion", String.valueOf(errCode));
                    if (errCode == 0) {
                        JSONObject data = jsonObject.getJSONObject("data").getJSONObject("questions");
                        JSONArray choice = data.getJSONArray("choice");
                        JSONArray fill = data.getJSONArray("normal");
                        if (choice.length() > 0) hanQs(choice);
                        if (fill.length() > 0)   hanQs(fill);


                        //Log.i("quesion",qes);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                viewPager.setAdapter(mAdapter);
//                setStartExamData(mQuestionList);
            }
        }).start();
    }

    private void hanQs(JSONArray qs) throws JSONException {
        for(int i =0;i<qs.length();i++){
           JSONObject q = qs.getJSONObject(i);
           String type = q.getString("type");
           switch (type){
               // 单选题
               case "choice":
                   ArrayList<String> metas = new ArrayList<>();
                   ArrayList<String> ans = new ArrayList<>();
                   ans.add("11");
                   String des1 = q.getString("description");
                   String choice_str = q.getString("choice");
                   choice_str  = CommonUtil.removeHtml_P(choice_str);
                   choice_str  = CommonUtil.removeHtml_br(choice_str);

                   String pattern = "\"(.*?)\"";
                   Pattern p = Pattern.compile(pattern);
                   Matcher m = p.matcher(choice_str);
                   String choice = "";
                   while (m.find()) {
                       choice = m.group(1);
                       metas.add(choice);
                   }
                   QuestionBean b1 = new QuestionBean();
                   b1.type = QuestionTypeBean.single_choice;
                   b1.setAnswer(ans);
                   b1.setMetas(metas);
                   b1.setStem(des1);
                   mQuestionList.add(b1);
                   break;

               //填空题
               case "completion":
                   String des2 = q.getString("description");
                   ArrayList<String> ans2 = new ArrayList<>();
                   QuestionBean b2 = new QuestionBean();
                   b2.type = QuestionTypeBean.fill;
                   b2.setStem(des2);
                   //b2.setAnswer(ans2);
                   mQuestionList.add(b2);
                   break;

               //主观题
               case "short_answer":
                   String des3 = q.getString("description");
                   ArrayList<String> ans3 = new ArrayList<>();
                   QuestionBean b3 = new QuestionBean();
                   b3.type = QuestionTypeBean.essay;
                   b3.setStem(des3);
                   //b3.setAnswer(ans3);
                   mQuestionList.add(b3);
                   break;
           }
       }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * @param results 题目集合
     */
    private void setStartExamData(ArrayList<QuestionBean> results) {
        mAdapter.notifyDataSetChanged();
        viewPager.setAdapter(mAdapter);
        Log.d("123","111");

        for (QuestionBean homeworkQuestionBean : mQuestionList) {
            HomeworkAnswerBean answerBean = new HomeworkAnswerBean();
            answerBean.data = homeworkQuestionBean.getAnswer();
            answerList.add(answerBean);
        }

    }
    private void initDialog() {
        mTitleDialog = new SureNoTitleDialog()
                .setMessage("你当前还有题目未做完，确定交卷")
                .setAllBtnText("坚持考完", "交卷")
                .setClickListener(new SureNoTitleDialog.CallBack() {
                    @Override
                    public void onRightClick(SureNoTitleDialog dialog, View v) {
                        dialog.dismissDialog(getSupportFragmentManager());

                    }

                    @Override
                    public void onLeftClick(SureNoTitleDialog dialog, View v) {
                        dialog.dismissDialog(getSupportFragmentManager());
                        Intent intent = new Intent(TestActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });

        mSubmitDialog = new SureNoTitleDialog()
                .setMessage("确认交卷")
                .setAllBtnText("确认交卷", "检查一下")
                .setClickListener(new SureNoTitleDialog.CallBack() {
                    @Override
                    public void onRightClick(SureNoTitleDialog dialog, View v) {
                        dialog.dismissDialog(getSupportFragmentManager());
                        Intent intent = new Intent(TestActivity.this,MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onLeftClick(SureNoTitleDialog dialog, View v) {
                        dialog.dismissDialog(getSupportFragmentManager());
                    }
                });
    }

    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private boolean flag;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentViewID = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        flag = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        flag = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        //判断是不是最后一页，同是是不是拖的状态
                        if(viewPager.getCurrentItem() == mAdapter.getCount() - 1){
                            down.setText("提交");
                        }
                        if (viewPager.getCurrentItem() == mAdapter.getCount() - 1 && !flag) {
                            Toast.makeText(TestActivity.this,"已经是最后一题",Toast.LENGTH_SHORT).show();
                            down.setText("提交");
                            TestActivity.this.showFinishDialog();
                        }
                        flag = true;
                        break;
                    default:
                        break;
                }
            }
        });
        viewPager.setOffscreenPageLimit(3);
    }

    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.q_up:
                    if(mCurrentViewID != 0){
                        down.setText("下一题");
                        mCurrentViewID--;
                        viewPager.setCurrentItem(mCurrentViewID, true);
                    }
                    break;
                case R.id.q_down:
                    if(viewPager.getCurrentItem() != mAdapter.getCount() - 1){
                        down.setText("下一题");
                        mCurrentViewID++;
                        viewPager.setCurrentItem(mCurrentViewID, true);
                    }

                    if(down.getText().toString().equals("提交")){
                        TestActivity.this.showFinishDialog();
                    }
                    break;
                case R.id.tv_datika:
                    if (mQuestionList.size() == 0) return;
                    showQuestionCard();
                    break;
//                    startActivityForResult(new Intent(TestActivity.this,Popup_question.class),Q_NUMBER);
            }
        }
    };

    private void showQuestionCard() {
        HomeworkCardFragment fragment = new HomeworkCardFragment();
        Bundle params = new Bundle();
        params.putBoolean("isShowSubmit", true);
        fragment.setArguments(params);
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    public void showFinishDialog() {
        for (int i = 0; i < mQuestionList.size(); i++) {
            HomeworkAnswerBean answer = answerList.get(i);
            if (!answer.isAnswer) {
                mTitleDialog.showDialog(getSupportFragmentManager());
                return;
            }
        }
        mSubmitDialog.showDialog(getSupportFragmentManager());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == Q_NUMBER && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            int position = bundle.getInt("result");
            mCurrentViewID = position;
            viewPager.setCurrentItem(mCurrentViewID, true);
            if(mCurrentViewID == PAGER_NUM-1){
                down.setText("提交");
            }else{
                down.setText("下一题");
            }
        }
    }

    @Subscribe
    public void onEvent(MessageEvent messageEvent) throws Exception {
        switch (messageEvent.getType()) {
            case MessageEvent.EXAM_CHANGE_ANSWER: // 保存答案
                Bundle bundle = (Bundle) messageEvent.getMessageBody();
                int index = bundle.getInt("index", 0);
                ArrayList<String> data = bundle.getStringArrayList("data");
                QuestionTypeBean questionType = (QuestionTypeBean) bundle.getSerializable("QuestionType");
                changeAnswer(index, data, questionType);
                responAnswer(index,data,questionType);//服务器上传答案

                break;
            case MessageEvent.EXAM_NEXT_QUESTION:
                //自动下一题
                if (viewPager.getCurrentItem() == mQuestionList.size() - 1) {
                    Toast.makeText(TestActivity.this,"已经是最后一题",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (viewPager.getCurrentItem() < mQuestionList.size() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    Log.d("dangqian", String.valueOf(viewPager.getCurrentItem()));
                    EventBus.getDefault().cancelEventDelivery(messageEvent);
                }
                break;
            case MessageEvent.EXAM_CARD_JUMP:
                //题目跳转
                int num = (Integer) messageEvent.getMessageBody();
                if (num < 0 || num > mQuestionList.size() - 1) {
                    return;
                }
                viewPager.setCurrentItem(num, true);
                break;

            default:
                break;
        }
    }

    /**
     * @param data 题目答案上传服务器
     * @param questionType
     */
    private void responAnswer(int index, ArrayList<String> data, QuestionTypeBean questionType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HomeworkAnswerBean answer = answerList.get(index);
                answer.data = data;

                String answer1 ="";
                //处理答案体
                switch (questionType){
                    case single_choice://单选题
                        answer1 = "{\""+answer.data+"\":true}";

                        break;
                    case fill://填空
                        //answer1= " {\""+answer.data.get(index)+"\":\""+answer.data[index] +"\"}";
                        Map<String, String> map = new HashMap<String, String>();
                        for(int i=answer.data.size()-1;i>=0;i--){
                            map.put("\"" + i + "\"","\"" + (String)answer.data.get(i) + "\""  );
                            answer1 = map.toString();
                        }
                        break;
                    case essay://材料题
                        answer1="{\"0\":\"" +answer.data +"\"}";
                        break;
                }

                try {
                    Response response = Api.post_addAnswer(37,1,index+1,answer1);

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String errCode = jsonObject.getString("data");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 通过Event 事件回传答案
     */
    private void changeAnswer(int index, ArrayList<String> data, QuestionTypeBean questionType) {
        if (answerList == null) {
            return;
        }

        HomeworkAnswerBean answer = answerList.get(index);
        answer.data = data;
        answer.isAnswer = data != null && !data.isEmpty();
        if (questionType == QuestionTypeBean.single_choice || questionType == QuestionTypeBean.determine) {
            if (FastClickUtil.isFastClick()) {
                viewPager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.EXAM_NEXT_QUESTION));
                    }
                }, 500);
            }
        } else if (questionType == QuestionTypeBean.material
                && (mQuestionList.get(index).getType() == QuestionTypeBean.single_choice
                || mQuestionList.get(index).getType() == QuestionTypeBean.determine)) {
            if (FastClickUtil.isFastClick()) {
                viewPager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.EXAM_NEXT_QUESTION));
                    }
                }, 500);
            }
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 这里来获取容器的宽和高
        if (hasFocus) {
            containerHeight = parentView.getHeight();
            containerWidth = parentView.getWidth();
        }
    }

    /**
     * @param event 移动摄像头
     */
    private void MetTouchLeftClick(MotionEvent event) {
        //得到事件的坐标
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                lastY = event.getRawY();
            case MotionEvent.ACTION_MOVE:
                float distanceX = lastX - event.getRawX();
                float distanceY = lastY - event.getRawY();

                float nextY = cameraView.getY() - distanceY;
                float nextX = cameraView.getX() - distanceX;

                // 不能移出屏幕
                if (nextY < 0) {
                    nextY = 0;
                } else if (nextY > containerHeight - cameraView.getHeight()) {
                    nextY = containerHeight - cameraView.getHeight();
                }
                if (nextX < 0)
                    nextX = 0;
                else if (nextX > containerWidth - cameraView.getWidth())
                    nextX = containerWidth - cameraView.getWidth();

                // 属性动画移动
                ObjectAnimator y = ObjectAnimator.ofFloat(cameraView, "y", cameraView.getY(), nextY);
                ObjectAnimator x = ObjectAnimator.ofFloat(cameraView, "x", cameraView.getX(), nextX);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(x, y);
                animatorSet.setDuration(0);
                animatorSet.start();

                lastX = event.getRawX();
                lastY = event.getRawY();
        }
    }

    /**
     *  当摄像机预览开始时，这个方法就会被调用。在调用该方法之后，框架将通过onCameraFrame()回调向客户端发送。
     *
     * @param width  - 帧的宽度
     * @param height - 帧的高度
     */
    @Override
    public void onCameraViewStarted(int width, int height) {
        //定义Mat对象
        rgba = new Mat();
    }

    /**
     * 当摄像机预览由于某种原因被停止时，这个方法就会被调用。
     *在调用这个方法之后，不会通过onCameraFrame()回调来传递任何帧。
     */
    @Override
    public void onCameraViewStopped() {
        rgba.release();
    }


    /**
     * 图像处理
     */
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        //直接返回输入视频预览图的RGBA数据并存在Mat数据中
        rgba = inputFrame.rgba();
        //判断横竖屏用于进行图像的旋转
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            Core.rotate(rgba, rgba, Core.ROTATE_180);
        }
        //把旋转后的Mat图像根据摄像头屏幕的大小进行缩放
        Size size = new Size(176,144);
        Imgproc.resize(rgba, rgba, size);

        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >=1000){
            PostFile(rgba);
            lastClickTime=time;
        }

        return rgba;

    }

    private void PostFile(Mat rgba) {
        //mat 是要被转换的Mat对象
        //Imgproc.cvtColor(rgba,rgba,Imgproc.COLOR_BGR2RGBA);//转换色彩空间
        //创建Bitmap对象
        Bitmap bitmap = Bitmap.createBitmap(rgba.width(),rgba.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(rgba,bitmap);//转换

        File file = new File(compressImage(bitmap),"icon.png");

        Log.i("path", String.valueOf(file.length()));
        try {
            Response response = Api.post_Img(37,1,file);
            Log.i("传递",response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public File compressImage(Bitmap bmp) {
        File filesDir;

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = this.getExternalFilesDir("");
            Log.d("路径", String.valueOf(filesDir));
        }else{//手机内部存储
            //路径：data/data/包名/files
            filesDir = this.getFilesDir();
            //Log.d("路径2", String.valueOf(filesDir));
        }


        FileOutputStream fos = null;
        try {
            File file = new File(filesDir,"icon.png");
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filesDir ;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

}