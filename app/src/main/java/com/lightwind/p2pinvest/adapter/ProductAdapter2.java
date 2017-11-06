package com.lightwind.p2pinvest.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.bean.Product;

import java.util.List;

/**
 * Function：全部理财适配器（测试了加载不同item类型）
 * Author：LightWind
 * Time：2017/11/2
 * <p>
 * 抽取了，但是没有使用ViewHolder，getView()优化不够
 */

public class ProductAdapter2 extends MyBaseAdapter2<Product> {

    /**
     * 通过构造器初始化集合数据
     */
    public ProductAdapter2(List<Product> list) {
        super(list);
    }

    @Override
    protected View initView(Context context) {
        return View.inflate(context, R.layout.item_product_list, null);
    }

    @Override
    protected void setData(View convertView, Product product) {
        ((TextView) convertView.findViewById(R.id.p_name)).setText(product.name);
        // ...


    }
}
