package com.lightwind.p2pinvest.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.lightwind.p2pinvest.bean.User;
import com.loopj.android.http.AsyncHttpClient;

import butterknife.ButterKnife;

/**
 * Function：Activity基类
 * Author：LightWind
 * Time：2017/11/3
 */

public abstract class BaseActivity extends FragmentActivity {

    public AsyncHttpClient mClient = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        // 将当前的Activity添加到ActivityManager中
        ActivityManager.getInstance().add(this);
        initTitle();
        initData();
    }

    /**
     * 获取布局
     */
    protected abstract int getLayoutId();

    /**
     * 初始化标题信息
     */
    protected abstract void initTitle();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 启动新的Activity
     */
    public void goToActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        // 携带数据
        if (bundle != null && bundle.size() != 0) {
            intent.putExtra("data", bundle);
        }
        startActivity(intent);
    }

    /**
     * 销毁当前Activity
     */
    public void removeCurrentActivity() {
        ActivityManager.getInstance().removeCurrent();
    }

    /**
     * 销毁所有Activity
     */
    public void removeAll() {
        ActivityManager.getInstance().removeAll();
    }

    /**
     * 保存用户信息
     */
    public void saveUser(User user) {
        SharedPreferences preferences = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", user.getName());
        editor.putString("imageurl", user.getImageurl());
        editor.putBoolean("iscredit", user.isCredit());
        editor.putString("phone", user.getPhone());
        editor.commit();
    }

    /**
     * 读取用户信息
     */
    public User readUser() {
        SharedPreferences preferences = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        User user = new User();
        user.setName(preferences.getString("name", ""));
        user.setImageurl(preferences.getString("imageurl", ""));
        user.setCredit(preferences.getBoolean("iscredit", false));
        user.setPhone(preferences.getString("phone", ""));
        return user;
    }

}
