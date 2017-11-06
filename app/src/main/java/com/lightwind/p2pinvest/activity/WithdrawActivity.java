package com.lightwind.p2pinvest.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.common.BaseActivity;
import com.lightwind.p2pinvest.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class WithdrawActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView mIvTitleSetting;
    @BindView(R.id.account_zhifubao)
    TextView mAccountZhifubao;
    @BindView(R.id.select_bank)
    RelativeLayout mSelectBank;
    @BindView(R.id.recharge_text)
    TextView mRechargeText;
    @BindView(R.id.et_input_money)
    EditText mEtInputMoney;
    @BindView(R.id.btn_withdraw)
    Button mBtnWithdraw;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void initTitle() {
        mIvTitleBack.setVisibility(View.VISIBLE);
        mTvTitle.setText("提现");
        mIvTitleSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {
        // Button默认是不可操作的
        mBtnWithdraw.setClickable(false);

        mEtInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String money = mEtInputMoney.getText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    // 设置Button为不可操作
                    mBtnWithdraw.setClickable(false);
                    // 设置背景颜色
                    mBtnWithdraw.setBackgroundResource(R.drawable.btn_02);
                } else {
                    // 设置Button为可操作
                    mBtnWithdraw.setClickable(true);
                    // 设置背景颜色
                    mBtnWithdraw.setBackgroundResource(R.drawable.btn_01);
                }
            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.btn_withdraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                removeCurrentActivity();
                break;
            case R.id.btn_withdraw:
                //将要提现的数据数额发送给后台，由后台连接第三方支付平台，完成金额的提现操作。（略）
                //提示用户信息：
                UIUtils.toast("您的提现申请已被成功受理。审核通过后，24小时内，你的钱自然会到账", false);

                UIUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeCurrentActivity();
                    }
                }, 2000);
                break;
        }
    }
}
