package com.lightwind.p2pinvest.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Function：BaseAdapter基类
 * Author：LightWind
 * Time：2017/11/2
 */

public abstract class MyBaseAdapter3<T> extends BaseAdapter {

    private List<T> mList;

    /**
     * 通过构造器初始化集合数据
     */
    MyBaseAdapter3(List<T> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 将具体的集合数据装配到具体的一个item layout中
     * 问题一：item layout的布局是不确定的。
     * 问题二：将集合中指定位置的数据装配到item，是不确定的。
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder<T> holder;
        if (convertView == null) {
            holder = getHolder();
        } else {
            holder = (BaseHolder<T>) convertView.getTag();
        }

        // 装配数据
        T t = mList.get(position);
        holder.setData(t);

        return holder.getRootView();
    }

    protected abstract BaseHolder<T> getHolder();

}
