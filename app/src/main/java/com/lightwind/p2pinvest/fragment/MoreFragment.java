package com.lightwind.p2pinvest.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.activity.AboutInvestActivity;
import com.lightwind.p2pinvest.activity.GestureEditActivity;
import com.lightwind.p2pinvest.activity.UserRegisterActivity;
import com.lightwind.p2pinvest.common.AppNetConfig;
import com.lightwind.p2pinvest.common.BaseActivity;
import com.lightwind.p2pinvest.common.BaseFragment;
import com.lightwind.p2pinvest.utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Function：更多Fragment
 * Author：LightWind
 * Time：2017/10/27
 * <p>
 * AppKey：22272ca5bf737
 * App Secret：4b29455f8c32997fa6d9e54d17a324b5
 */

public class MoreFragment extends BaseFragment {

    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView mIvTitleSetting;
    @BindView(R.id.tv_more_register)
    TextView mTvMoreRegister;
    @BindView(R.id.toggle_more)
    ToggleButton mToggleMore;
    @BindView(R.id.tv_more_reset)
    TextView mTvMoreReset;
    @BindView(R.id.tv_more_phone)
    TextView mTvMorePhone;
    @BindView(R.id.rl_more_contact)
    RelativeLayout mRlMoreContact;
    @BindView(R.id.tv_more_fankui)
    TextView mTvMoreFankui;
    @BindView(R.id.tv_more_share)
    TextView mTvMoreShare;
    @BindView(R.id.tv_more_about)
    TextView mTvMoreAbout;

    private SharedPreferences mPreferences;

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
        return R.layout.fragment_more;
    }

    @Override
    public void initTitle() {
        mIvTitleBack.setVisibility(View.INVISIBLE);
        mTvTitle.setText("更多");
        mIvTitleSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData(String content) {

        mPreferences = this.getActivity().getSharedPreferences("secret_protect", Context.MODE_PRIVATE);

        // 用户注册
        userRegister();

        // 获取当前手势密码的状态
        getGestureStatus();

        // 设置手势密码
        setGesturePassword();

        // 重置手势密码
        resetGesture();

        // 联系客服
        contactService();

        // 提交反馈
        commitFanKui();

        // 分享
        share();

        // 关于硅谷理财
        aboutInvest();
    }

    private void aboutInvest() {
        mTvMoreAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) MoreFragment.this.getActivity()).goToActivity(AboutInvestActivity.class, null);
            }
        });
    }

    private void share() {
        mTvMoreShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this.getActivity());
    }

    private String department = "不明确";

    /**
     * 提交反馈
     */
    private void commitFanKui() {
        mTvMoreFankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 提供一个View
                View view = View.inflate(MoreFragment.this.getActivity(), R.layout.view_fankui, null);
                final RadioGroup radioGroup = view.findViewById(R.id.rg_fankui);
                final EditText et_fankui_content = view.findViewById(R.id.et_fankui_content);

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = radioGroup.findViewById(checkedId);
                        department = radioButton.getText().toString();
                    }
                });

                new AlertDialog.Builder(MoreFragment.this.getActivity())
                        .setView(view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 反馈的信息
                                String content = et_fankui_content.getText().toString();
                                // 联网发送反馈信息
                                AsyncHttpClient client = new AsyncHttpClient();
                                RequestParams params = new RequestParams();
                                params.put("department", department);
                                params.put("content", content);
                                client.post(AppNetConfig.FEEDBACK, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onFailure(Throwable error, String content) {
                                        UIUtils.toast("发送反馈失败！", false);
                                    }

                                    @Override
                                    public void onSuccess(String content) {
                                        UIUtils.toast("发送反馈成功！", false);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    /**
     * 联系客服
     */
    private void contactService() {
        mRlMoreContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MoreFragment.this.getActivity())
                        .setTitle("联系客服")
                        .setMessage("是否现在联系客服")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String phone = mTvMorePhone.getText().toString();
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phone));
                                MoreFragment.this.getActivity().startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    /**
     * 重置手势密码
     */
    private void resetGesture() {
        mTvMoreReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = mToggleMore.isChecked();
                if (checked) {
                    ((BaseActivity) MoreFragment.this.getActivity()).goToActivity(GestureEditActivity.class, null);
                } else {
                    UIUtils.toast("请开启手势密码！", false);
                }
            }
        });
    }

    /**
     * 获取当前手势密码的状态
     */
    private void getGestureStatus() {
        boolean isOpen = mPreferences.getBoolean("isOpen", false);
        mToggleMore.setChecked(isOpen);
    }

    /**
     * 设置手势密码
     */
    private void setGesturePassword() {
        mToggleMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    UIUtils.toast("开启了手势密码", false);
//                    mPreferences.edit().putBoolean("isOpen", true).commit();
                    String inputCode = mPreferences.getString("inputCode", "");
                    if (TextUtils.isEmpty(inputCode)) {// 之前没有设置过
                        new AlertDialog.Builder(MoreFragment.this.getActivity())
                                .setTitle("设置手势密码")
                                .setMessage("是否现在设置手势密码？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mPreferences.edit().putBoolean("isOpen", true).commit();
//                                        mToggleMore.setChecked(true);

                                        // 开启新的Activity
                                        ((BaseActivity) MoreFragment.this.getActivity()).goToActivity(GestureEditActivity
                                                .class, null);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UIUtils.toast("取消现在设置手势密码！", false);
                                        mPreferences.edit().putBoolean("isOpen", false).commit();
                                        mToggleMore.setChecked(false);
                                    }
                                })
                                .show();
                    } else {
                        UIUtils.toast("开启手势密码！", false);
                        mPreferences.edit().putBoolean("isOpen", true).commit();
//                        mToggleMore.setChecked(true);
                    }
                } else {
                    UIUtils.toast("关闭了手势密码！", false);
                    mPreferences.edit().putBoolean("isOpen", false).commit();
//                    mToggleMore.setChecked(false);
                }
            }
        });
    }

    /**
     * 用户注册
     */
    private void userRegister() {
        mTvMoreRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) MoreFragment.this.getActivity()).goToActivity(UserRegisterActivity.class, null);
            }
        });
    }
}
