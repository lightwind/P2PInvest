package com.lightwind.p2pinvest.fragment;

import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.adapter.ProductAdapter3;
import com.lightwind.p2pinvest.bean.Product;
import com.lightwind.p2pinvest.common.AppNetConfig;
import com.lightwind.p2pinvest.common.BaseFragment;
import com.lightwind.p2pinvest.ui.MyTextView;
import com.loopj.android.http.RequestParams;

import java.util.List;

import butterknife.BindView;

/**
 * Function：全部理财
 * Author：LightWind
 * Time：2017/11/1
 */

public class ProductListFragment extends BaseFragment {

    @BindView(R.id.tv_product_title)
    MyTextView mTvProductTitle;
    @BindView(R.id.lv_product_list)
    ListView mLvProductList;
    private List<Product> mProductList;

    @Override
    protected String getUrl() {
        return AppNetConfig.PRODUCT;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_product_list;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initData(String content) {
        JSONObject jsonObject = JSON.parseObject(content);
        Boolean success = jsonObject.getBoolean("success");
        if (success) {
            String data = jsonObject.getString("data");
            // 获取集合数据
            mProductList = JSON.parseArray(data, Product.class);

            ProductAdapter3 productAdapter = new ProductAdapter3(mProductList);
            mLvProductList.setAdapter(productAdapter);
        }
    }
}
