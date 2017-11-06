package com.lightwind.p2pinvest.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import com.mob.MobApplication;

/**
 * Function：继承MobApplication，在里面初始化ShareSDK
 * Author：LightWind
 * Time：2017/10/27
 */

public class MyApplication extends MobApplication {

    // 在整个应用执行过程中，需要提供的变量
    @SuppressLint("StaticFieldLeak")
    public static Context context;//需要使用的上下文对象：Application
    public static Handler handler;//需要使用的handler
    public static Thread mainThread;//提供主线程对象
    public static int mainThreadId;//提供主线程对象的id

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        handler = new Handler();
        mainThread = Thread.currentThread();// 实例化当前Application的线程即为主线程
        mainThreadId = Process.myTid();// 获取当前线程的id

        // 设置未捕获异常的处理器，APP上线的时候，可以使用
//        CrashHandler.getInstance().init();

    }
}
