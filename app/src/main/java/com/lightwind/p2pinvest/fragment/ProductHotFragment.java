package com.lightwind.p2pinvest.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.common.BaseFragment;
import com.lightwind.p2pinvest.ui.FlowLayout;
import com.lightwind.p2pinvest.utils.DrawUtils;
import com.lightwind.p2pinvest.utils.UIUtils;
import com.loopj.android.http.RequestParams;

import java.util.Random;

import butterknife.BindView;

/**
 * Function：热门理财
 * Author：LightWind
 * Time：2017/11/1
 */

public class ProductHotFragment extends BaseFragment {

    @BindView(R.id.flow_hot)
    FlowLayout mFlowHot;

    private String[] datas = new String[]{"新手福利计划", "财神道90天计划", "硅谷计划", "30天理财计划",
            "180天理财计划", "月月升", "中情局投资商业经营", "大学老师购买车辆",
            "屌丝下海经商计划", "美人鱼影视拍摄投资", "Android培训老师自己周转", "养猪场扩大经营",
            "旅游公司扩大规模", "摩托罗拉洗钱计划", "铁路局回款计划", "屌丝迎娶白富美计划"};

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_hot_list;
    }

    @Override
    protected void initTitle() {
    }

    @Override
    protected void initData(String content) {
        for (String data : datas) {
            final TextView textView = new TextView(getContext());

            // 设置属性
            textView.setText(data);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = UIUtils.dp2px(10);
            params.topMargin = UIUtils.dp2px(10);
            params.rightMargin = UIUtils.dp2px(10);
            params.bottomMargin = UIUtils.dp2px(10);
            textView.setLayoutParams(params);// 设置边距

            int padding = UIUtils.dp2px(8);
            textView.setPadding(padding, padding, padding, padding);// 设置内边距

            textView.setTextSize(UIUtils.dp2px(15));

            Random random = new Random();
            int red = random.nextInt(211);
            int green = random.nextInt(211);
            int blue = random.nextInt(211);

            // 设置单一背景
//            textView.setBackground(DrawUtils.getDrawable(Color.rgb(red, green, blue), UIUtils.dp2px(5)));
            // 设置具有选择器的背景
            textView.setBackground(DrawUtils.getSelector(DrawUtils.getDrawable(Color.rgb(red, green, blue), UIUtils.dp2px(5)),
                    DrawUtils.getDrawable(Color.WHITE, UIUtils.dp2px(5))));
//            textView.setClickable(true);// 设置可点击，如果设置了点击事件，则TextView就是可点击的

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.toast(String.valueOf(textView.getText()), false);
                }
            });

            mFlowHot.addView(textView);
        }
    }
}
