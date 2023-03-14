package com.example.a12.event;

import android.util.Log;

import com.example.a12.MyApp;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api  {

    static OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
    static MediaType mediaType = MediaType.parse("application/json");

    /**
     * @param act 账号
     * @param pwd 密码
     * @return 登录状态码
     * @throws Exception
     */
    public static Response post_login(String act, String pwd) throws Exception{
        String datas = "{\r\n    \"username\":\"" +act +"\",\r\n    \"password\":\"" +pwd+"\"\r\n}";
        RequestBody body = RequestBody.create(mediaType,datas);
        Log.d("map",datas);
        Request request = new Request.Builder()
                .url("http://139.9.58.231:8080/user/login")
                .method("POST", body)
                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }


    /**
     * @param examID 考试id
     * @param examinee 考生id
     * @param qustionID 题目id
     * @param answer 答案
     * @return
     * @throws Exception
     */
    public static Response post_addAnswer(int examID, int examinee, int qustionID, String answer) throws Exception{
        JSONObject data =new JSONObject();
        data.put("examId",examID);
        data.put("examinee",examinee);
        data.put("questionId",qustionID);
        data.put("answer",answer);
        RequestBody body = RequestBody.create(mediaType, String.valueOf(data));
        Log.d("map", String.valueOf(data));
        Request request = new Request.Builder()
                .url("http://139.9.58.231:8080/exam/addNormalAnswer")
                .addHeader("cookie", MyApp.sessionCookie)//在请求中添加登录后获取的cookie
                .method("POST", body)
                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }


    public static Response post_getQes(int examid,int examinee) throws Exception {
        JSONObject data =new JSONObject();
        data.put("examId",examid);
        data.put("examinee",examinee);
        RequestBody body = RequestBody.create(mediaType, String.valueOf(data));

        Request request = new Request.Builder()
                .url("http://139.9.58.231:8080/exam/getAllExamInfo")
                .addHeader("cookie", MyApp.sessionCookie)//在请求中添加登录后获取的cookie
                .method("POST", body)
                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }


    /**
     * @param examId 考试
     * @param useId 学生
     * @param file 文件
     * @return
     * @throws Exception
     */
    public static Response post_Img(int examId, int useId, File file) throws Exception{
        //1.创建对应的MediaType
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        //2.创建RequestBody
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        //3.构建MultipartBody
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file",file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url("http://139.9.58.231:8080/supervision/uploadImg?examId="+examId+"&userId="+useId)
                .addHeader("cookie", MyApp.sessionCookie)//在请求中添加登录后获取的cookie
                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response post_Time() throws Exception{
        Request request = new Request.Builder()
                .url("http://139.9.58.231:8080/exam/getTime")
                .addHeader("cookie", MyApp.sessionCookie)//在请求中添加登录后获取的cookie
                .method("POST",null)
                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response post_TMess(int id,String search) throws Exception{
        JSONObject data =new JSONObject();
        data.put("id",id);
        data.put("search",search);
        RequestBody body = RequestBody.create(mediaType, String.valueOf(data));
        Request request = new Request.Builder()
                .url("http://139.9.58.231:8080/exam/getExams")
                .addHeader("cookie", MyApp.sessionCookie)//在请求中添加登录后获取的cookie
                .method("POST",body)
                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }



}
