package com.lightwind.p2pinvest.adapter;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Function：ViewHolder的基类
 * Author：LightWind
 * Time：2017/11/2
 */

public abstract class BaseHolder<T> {

    // 布局
    private View mRootView;

    // 数据
    private T mData;

    BaseHolder() {
        mRootView = initView();
        mRootView.setTag(this);// 关联视图
        ButterKnife.bind(this, mRootView);
    }

    /**
     * 提供item的布局
     */
    protected abstract View initView();

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
        refreshData();
    }

    View getRootView() {
        return mRootView;
    }

    /**
     * 装配过程
     */
    protected abstract void refreshData();
}
