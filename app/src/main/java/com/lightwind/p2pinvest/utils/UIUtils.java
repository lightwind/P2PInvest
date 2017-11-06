package com.lightwind.p2pinvest.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.view.View;
import android.widget.Toast;

import com.lightwind.p2pinvest.common.MyApplication;

/**
 * Function：专门提供为处理一些UI相关的问题而创建的工具类，
 * 提供资源获取的通用方法，避免每次都写重复的代码获取结果。
 * Author：LightWind
 * Time：2017/10/27
 */

public class UIUtils {

    public static Context getContext() {
        return MyApplication.context;
    }

    public static Handler getHandler() {
        return MyApplication.handler;
    }

    /**
     * 返回指定colorId对应的颜色值
     */
    public static int getColor(int colorId) {
        return getContext().getResources().getColor(colorId);
    }

    /**
     * 加载指定viewId的视图对象，并返回
     */
    public static View getView(int viewId) {
        return View.inflate(getContext(), viewId, null);
    }

    public static String[] getStringArr(int strArrId) {
        return getContext().getResources().getStringArray(strArrId);
    }

    /**
     * 将dp转化为px
     */
    public static int dp2px(int dp) {
        // 获取手机密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);// 实现四舍五入
    }

    /**
     * 将px转化为dp
     */
    public static int px2dp(int px) {
        // 获取手机密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);// 实现四舍五入
    }

    /**
     * 在主线程中操作更新视图
     */
    public static void runOnUiThread(Runnable runnable) {
        if (isInMainThread()) {
            runnable.run();
        } else {
            UIUtils.getHandler().post(runnable);
        }
    }

    /**
     * 判断当前线程是否是主线程
     */
    private static boolean isInMainThread() {
        int currentThreadId = Process.myPid();
        return MyApplication.mainThreadId == currentThreadId;
    }

    public static void toast(String message, boolean isLengthLong) {
        Toast.makeText(UIUtils.getContext(), message, isLengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }
}
