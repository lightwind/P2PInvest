package com.lightwind.p2pinvest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Function：BaseAdapter基类
 * Author：LightWind
 * Time：2017/11/2
 */

public abstract class MyBaseAdapter2<T> extends BaseAdapter {

    public List<T> list;

    /**
     * 通过构造器初始化集合数据
     */
    public MyBaseAdapter2(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = initView(parent.getContext());
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 获取集合数据
        T t = list.get(position);

        // 装配数据
        setData(convertView, t);

        return convertView;
    }

    /**
     * 提供具体的item layout的布局
     */
    protected abstract View initView(Context context);

    /**
     * 装配数据的操作
     */
    protected abstract void setData(View convertView, T t);


    class ViewHolder {
        private ViewHolder(View view) {
            view.setTag(this);
        }
    }

}
