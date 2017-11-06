package com.lightwind.p2pinvest.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.bean.User;
import com.lightwind.p2pinvest.common.AppNetConfig;
import com.lightwind.p2pinvest.common.BaseActivity;
import com.lightwind.p2pinvest.utils.MD5Utils;
import com.lightwind.p2pinvest.utils.UIUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView mIvTitleSetting;
    @BindView(R.id.et_login_number)
    EditText mEtLoginNumber;
    @BindView(R.id.rl_login)
    RelativeLayout mRlLogin;
    @BindView(R.id.et_login_pwd)
    EditText mEtLoginPwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initTitle() {
        mIvTitleBack.setVisibility(View.VISIBLE);
        mTvTitle.setText("用户登陆");
        mIvTitleSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.iv_title_back)
    public void onBack() {
        removeAll();
        goToActivity(MainActivity.class, null);
    }

    @OnClick(R.id.btn_login)
    public void onLogin() {
        final String number = mEtLoginNumber.getText().toString().trim();
        String password = mEtLoginPwd.getText().toString().trim();
        if (!TextUtils.isEmpty(number) && !TextUtils.isEmpty(password)) {
            String url = AppNetConfig.LOGIN;
            RequestParams params = new RequestParams();
            params.put("phone", number);
            params.put("password", MD5Utils.MD5(password));
            mClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    // 解析JSON
                    JSONObject jsonObject = JSON.parseObject(content);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        // 解析JSON数据，生成User对象
                        String data = jsonObject.getString("data");
                        User user = JSON.parseObject(data, User.class);

                        // 保存用户信息
                        saveUser(user);

                        // 重新加载界面
                        removeAll();
                        goToActivity(MainActivity.class, null);
                    } else {
                        UIUtils.toast("用户名不存在或密码不正确", false);
                    }
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    UIUtils.toast("联网失败！", false);
                }
            });
        } else {
            UIUtils.toast("用户名或密码不能为空", false);
        }
    }

}
