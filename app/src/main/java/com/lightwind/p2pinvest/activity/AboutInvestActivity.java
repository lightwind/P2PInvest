package com.lightwind.p2pinvest.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.common.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutInvestActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView mIvTitleSetting;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_invest;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initTitle() {
        mIvTitleBack.setVisibility(View.VISIBLE);
        mTvTitle.setText("用户注册");
        mIvTitleSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.iv_title_back)
    public void onBack() {
        removeCurrentActivity();
    }

    @Override
    protected void initData() {

    }
}
