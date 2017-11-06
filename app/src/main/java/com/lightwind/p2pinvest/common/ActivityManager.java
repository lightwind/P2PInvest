package com.lightwind.p2pinvest.common;

import android.app.Activity;

import java.util.Stack;

/**
 * Function：统一应用程序中所有的Activity的栈管理（单例）
 * 涉及到activity的添加、删除指定、删除当前、删除所有、返回栈大小的方法
 * Author：LightWind
 * Time：2017/10/27
 */

public class ActivityManager {

    // 单例模式：饿汉式
    private ActivityManager() {
    }

    private static ActivityManager activityManager = new ActivityManager();

    public static ActivityManager getInstance() {
        return activityManager;
    }

    // 提供栈的对象
    private Stack<Activity> mActivityStack = new Stack<>();

    /**
     * 添加Activity的方法
     */
    public void add(Activity activity) {
        if (activity != null) {
            mActivityStack.add(activity);
        }
    }

    /**
     * 删除指定的activity的方法
     */
    public void remove(Activity activity) {
        if (activity != null) {
            for (int i = 0; i < mActivityStack.size(); i++) {
                Activity currentActivity = mActivityStack.get(i);
                if (currentActivity.getClass().equals(activity.getClass())) {
                    currentActivity.finish();//销毁当前的activity
                    mActivityStack.remove(i);//从栈空间移除
                }
            }
        }
    }

    /**
     * 删除当前的activity
     */
    void removeCurrent() {
        //方式一：
//        Activity activity = activityStack.get(activityStack.size() - 1);
//        activity.finish();
//        activityStack.remove(activityStack.size() - 1);

        // 方式二：
        Activity activity = mActivityStack.lastElement();
        activity.finish();
        mActivityStack.remove(activity);
    }

    /**
     * 删除所有的activity
     */
    public void removeAll() {
        for (int i = mActivityStack.size() - 1; i >= 0; i--) {
            Activity activity = mActivityStack.get(i);
            activity.finish();
            mActivityStack.remove(activity);
        }
    }

    /**
     * 返回栈大小
     */
    public int size() {
        return mActivityStack.size();
    }
}
