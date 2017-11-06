package com.lightwind.p2pinvest.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.common.AppNetConfig;
import com.lightwind.p2pinvest.common.BaseActivity;
import com.lightwind.p2pinvest.utils.MD5Utils;
import com.lightwind.p2pinvest.utils.UIUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.OnClick;

public class UserRegisterActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView mIvTitleSetting;
    @BindView(R.id.et_register_number)
    EditText mEtRegisterNumber;
    @BindView(R.id.et_register_name)
    EditText mEtRegisterName;
    @BindView(R.id.et_register_pwd)
    EditText mEtRegisterPwd;
    @BindView(R.id.et_register_pwdagain)
    EditText mEtRegisterPwdagain;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_register;
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
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 获取用户注册信息
                String name = mEtRegisterName.getText().toString().trim();
                String number = mEtRegisterNumber.getText().toString().trim();
                String pwd = mEtRegisterPwd.getText().toString().trim();
                String pwdAgain = mEtRegisterPwdagain.getText().toString().trim();

                // 填写的信息不能为空
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number) ||
                        TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)) {
                    UIUtils.toast("填写信息不能为空！", false);
                } else if (!pwd.equals(pwdAgain)) {
                    // 两次密码一致
                    UIUtils.toast("两次填写的密码不一致！", false);
                    mEtRegisterPwd.setText("");
                    mEtRegisterPwdagain.setText("");
                } else {
                    // 联网发送用户注册信息
                    String url = AppNetConfig.USER_REGISTER;
                    RequestParams params = new RequestParams();
                    params.put("name", name);
                    params.put("password", MD5Utils.MD5(pwd));
                    params.put("phone", number);
                    mClient.post(url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onFailure(Throwable error, String content) {
                            UIUtils.toast("联网请求失败！", false);
                        }

                        @Override
                        public void onSuccess(String content) {
                            JSONObject jsonObject = JSON.parseObject(content);
                            Boolean isExist = jsonObject.getBoolean("isExist");
                            if (isExist) {// 已经注册过
                                UIUtils.toast("此用户已注册！", false);
                            } else {
                                UIUtils.toast("注册成功！", false);
                            }
                        }
                    });
                }
            }
        });
    }
}
