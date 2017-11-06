package com.lightwind.p2pinvest.adapter;

import com.lightwind.p2pinvest.bean.Product;

import java.util.List;

/**
 * Function：全部理财适配器（测试了加载不同item类型）
 * Author：LightWind
 * Time：2017/11/2
 */

public class ProductAdapter3 extends MyBaseAdapter3<Product> {

    /**
     * 通过构造器初始化集合数据
     */
    public ProductAdapter3(List<Product> list) {
        super(list);
    }

    @Override
    protected BaseHolder<Product> getHolder() {
        return new MyHolder();
    }
}
