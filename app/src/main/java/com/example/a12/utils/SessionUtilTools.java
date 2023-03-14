package com.example.a12.utils;


import android.util.Log;

import com.example.a12.MyApp;

import java.util.List;

import okhttp3.Headers;

public class SessionUtilTools   {


    public static void getSession(Headers headers){
        List<String> cookies = headers.values("Set-Cookie");
        String session = cookies.get(0);
        MyApp.sessionCookie = session.substring(0,session.indexOf(";"));// 取出cookie 保存在全局变量
        Log.d("cookie",MyApp.sessionCookie );
   }

}
