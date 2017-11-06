package com.lightwind.p2pinvest.common;

import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

import com.lightwind.p2pinvest.utils.LogUtil;
import com.lightwind.p2pinvest.utils.UIUtils;

/**
 * Function：程序中的未捕获的全局异常的捕获（单例）
 * <p>
 * 解决两个问题：
 * 1.当出现未捕获的异常时，能够给用户一个相对友好的提示
 * 2.在出现异常时，能够将异常信息发送给后台，便于在后续的版本中解决bug
 * Author：LightWind
 * Time：2017/10/27
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    // 系统默认的处理未捕获异常的处理器
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    /**
     * 单例模式：（懒汉式）
     * 本身实例化未捕获异常的处理器的操作就是系统在一个单独的线程中完成的，所有不涉及到
     * 多线程的问题，所以使用懒汉式更好！
     */
    private CrashHandler() {
    }

    private static CrashHandler crashHandler = null;

    public static CrashHandler getInstance() {
        if (crashHandler == null) {
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    public void init() {
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将当前类设置为出现未捕获异常的处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 一旦系统出现未捕获的异常，就会调用如下的回调方法
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        new Thread() {
            @Override
            public void run() {
                // prepare()和loop()之间的操作就是在主线程中执行的！
                // 在android系统中，默认情况下，一个线程中是不可以调用Looper进行消息的处理的。除非是主线程
                Looper.prepare();
                Toast.makeText(UIUtils.getContext(), "出现了未捕获的异常！", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        // 收集异常信息
        collectionException(e);

        try {
            Thread.sleep(2000);
            // 移除当前activity
            ActivityManager.getInstance().removeCurrent();
            // 结束当前的进程
            Process.killProcess(Process.myPid());
            // 结束虚拟机
            System.exit(0);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 收集异常信息
     */
    private void collectionException(Throwable e) {
        final String exMessage = e.getMessage();
        // 收集具体的客户的手机、系统的信息
        final String message = Build.DEVICE + ":" + Build.MODEL + ":" + Build.PRODUCT + ":" + Build.VERSION.SDK_INT;

        // 发送给后台此异常信息
        new Thread() {
            @Override
            public void run() {
                // 需要按照指定的url，访问后台的servlet,将异常信息发送过去
                LogUtil.e("exception = " + exMessage);
                LogUtil.e("message = " + message);
            }
        }.start();
    }
}
