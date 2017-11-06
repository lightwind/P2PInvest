package com.lightwind.p2pinvest.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.bean.Product;
import com.lightwind.p2pinvest.ui.RoundProgress;
import com.lightwind.p2pinvest.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Function：全部理财适配器（测试了加载不同item类型）
 * Author：LightWind
 * Time：2017/11/2
 * <p>
 * 没有抽取
 */

public class ProductAdapter extends BaseAdapter {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList == null ? 0 : productList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 将具体的集合数据装配到具体的一个item layout中。
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == 0) {
            TextView tv = new TextView(parent.getContext());
            tv.setText("与子同行，奈何覆舟");
            tv.setTextColor(UIUtils.getColor(R.color.text_progress));
            tv.setTextSize(UIUtils.dp2px(20));
            return tv;
        }

        if (position > 3) {
            position--;
        }

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_product_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 装配数据
        Product product = productList.get(position);
        holder.mPMinNum.setText(product.memberNum);
        holder.mPMinAmount.setText(product.minTouMoney);
        holder.mPMoney.setText(product.money);
        holder.mPName.setText(product.name);
        holder.mPProgress.setProgress(Integer.parseInt(product.progress));
        holder.mPLockDays.setText(product.suodingDays);
        holder.mPYearLv.setText(product.yearRate);

        return convertView;
    }

    /**
     * 不同的position位置上，显示的具体的item的type值
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 3) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 返回不同类型的item的个数
     */
    @Override
    public int getViewTypeCount() {
        return 2;
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
