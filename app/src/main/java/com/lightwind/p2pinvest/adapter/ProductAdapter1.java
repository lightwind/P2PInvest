package com.lightwind.p2pinvest.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.bean.Product;
import com.lightwind.p2pinvest.ui.RoundProgress;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Function：全部理财适配器（测试了加载不同item类型）
 * Author：LightWind
 * Time：2017/11/2
 * <p>
 * 抽取力度比较小
 */

public class ProductAdapter1 extends MyBaseAdapter1<Product> {


    public ProductAdapter1(List<Product> list) {
        super(list);
    }

    @Override
    public View myGetView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_product_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 装配数据
        Product product = list.get(position);
        holder.mPMinNum.setText(product.memberNum);
        holder.mPMinAmount.setText(product.minTouMoney);
        holder.mPMoney.setText(product.money);
        holder.mPName.setText(product.name);
        holder.mPProgress.setProgress(Integer.parseInt(product.progress));
        holder.mPLockDays.setText(product.suodingDays);

        return convertView;
    }

    static class ViewHolder {
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
