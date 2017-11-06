package com.lightwind.p2pinvest.adapter;

import android.view.View;
import android.widget.TextView;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.bean.Product;
import com.lightwind.p2pinvest.ui.RoundProgress;
import com.lightwind.p2pinvest.utils.UIUtils;

import butterknife.BindView;

/**
 * Function：Product的ViewHolder
 * Author：LightWind
 * Time：2017/11/2
 */

public class MyHolder extends BaseHolder<Product> {
    @BindView(R.id.p_name)
    TextView mPName;
    @BindView(R.id.p_money)
    TextView mPMoney;
    @BindView(R.id.p_year_lv)
    TextView mPYearLv;
    @BindView(R.id.p_lock_days)
    TextView mPLockDays;
    @BindView(R.id.p_min_amount)
    TextView mPMinAmount;
    @BindView(R.id.p_min_num)
    TextView mPMinNum;
    @BindView(R.id.p_progress)
    RoundProgress mPProgress;

    @Override
    protected View initView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_product_list, null);
    }

    @Override
    protected void refreshData() {
        Product product = this.getData();
        // 装数据
        mPMinNum.setText(product.memberNum);
        mPMinAmount.setText(product.minTouMoney);
        mPMoney.setText(product.money);
        mPName.setText(product.name);
        mPProgress.setProgress(Integer.parseInt(product.progress));
        mPLockDays.setText(product.suodingDays);
        mPYearLv.setText(product.yearRate);
    }
}
