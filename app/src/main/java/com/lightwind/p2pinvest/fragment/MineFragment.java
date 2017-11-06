package com.lightwind.p2pinvest.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.activity.GestureVerifyActivity;
import com.lightwind.p2pinvest.activity.LineChartActivity;
import com.lightwind.p2pinvest.activity.LoginActivity;
import com.lightwind.p2pinvest.activity.PieChartActivity;
import com.lightwind.p2pinvest.activity.RechargeActivity;
import com.lightwind.p2pinvest.activity.UserInfoActivity;
import com.lightwind.p2pinvest.activity.WithdrawActivity;
import com.lightwind.p2pinvest.bean.User;
import com.lightwind.p2pinvest.common.BaseActivity;
import com.lightwind.p2pinvest.common.BaseFragment;
import com.lightwind.p2pinvest.utils.BitmapUtils;
import com.lightwind.p2pinvest.utils.UIUtils;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Function：我的Fragment
 * Author：LightWind
 * Time：2017/10/27
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView mIvTitleSetting;
    @BindView(R.id.iv_me_icon)
    ImageView mIvMeIcon;
    @BindView(R.id.tv_me_name)
    TextView mTvMeName;

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
        return R.layout.fragment_mine;
    }

    @Override
    public void initTitle() {
        mIvTitleBack.setVisibility(View.INVISIBLE);
        mTvTitle.setText("我的资产");
        mIvTitleSetting.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_title_setting)
    public void oSetting() {
        // 启动用户信息界面
        ((BaseActivity) this.getActivity()).goToActivity(UserInfoActivity.class, null);
    }

    @Override
    protected void initData(String content) {
        // 判断用户是否已经登陆
        isLogin();
    }

    /**
     * 判断用户是否已经登陆
     */
    private void isLogin() {
        // 查看本地是否有用户的登陆信息
        SharedPreferences preferences = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = preferences.getString("name", "");
        if (TextUtils.isEmpty(name)) {
            // 本地没有保存过用户信息
            doLogin();
        } else {
            // 已经登陆过，则直接加载用户的信息并显示
            doUser();
        }
    }

    /**
     * 已经登陆过，则直接加载用户的信息并显示
     */
    private void doUser() {

        // 判断是否开启了手势密码
        SharedPreferences preferences = this.getActivity().getSharedPreferences("secret_protect", Context.MODE_PRIVATE);
        boolean isOpen = preferences.getBoolean("isOpen", false);
        if (isOpen) {
            ((BaseActivity) this.getActivity()).goToActivity(GestureVerifyActivity.class, null);
            return;
        }

        User user = ((BaseActivity) this.getActivity()).readUser();
        mTvMeName.setText(user.getName());

        // 判断本地是否已经保存图片，如果有，则不再执行联网操作
        boolean isExist = readImage();
        if (isExist) return;

        // 使用Picasso联网获取图片
        Picasso.with(this.getActivity()).load(user.getImageurl()).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {// 下载以后的内存中的bitmap对象
                // 压缩处理
                Bitmap bitmap = BitmapUtils.zoom(source, UIUtils.dp2px(62), UIUtils.dp2px(62));
                // 圆形处理
                bitmap = BitmapUtils.circleBitmap(bitmap);
                // 回收Bitmap资源
                source.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "";// 需要保证返回值不能为空，否则报错
            }
        }).into(mIvMeIcon);
    }

    /**
     * 没有登陆，提示用户登陆
     */
    private void doLogin() {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("提示")
                .setMessage("还没登陆，请登陆！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((BaseActivity) MineFragment.this.getActivity()).goToActivity(LoginActivity.class, null);
                    }
                })
//                .setNegativeButton("取消", null)
                .setCancelable(false)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 读取本地保存的图片
        readImage();
    }

    /**
     * 读取本地保存的图片
     */
    private boolean readImage() {
        File filesDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// SD卡是否挂载
            // 路径：storage/sdcard/Android/data/包名/files
            filesDir = this.getActivity().getExternalFilesDir("");
        } else {// 手机内部存储
            // 路径：data/data/包名/files
            filesDir = this.getActivity().getFilesDir();
        }
        File file = new File(filesDir, "icon.png");
        if (file.exists()) {
            // 存储 ---> 内存
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            mIvMeIcon.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }

    @OnClick({R.id.recharge, R.id.withdraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recharge:// 设置充值操作
                ((BaseActivity) this.getActivity()).goToActivity(RechargeActivity.class, null);
                break;
            case R.id.withdraw:// 设置提现操作
                ((BaseActivity) this.getActivity()).goToActivity(WithdrawActivity.class, null);
                break;
        }
    }

    @OnClick({R.id.ll_touzi, R.id.ll_touzi_zhiguan, R.id.ll_zichan})
    public void onLinearLayoutClick(View view) {
        switch (view.getId()) {
            case R.id.ll_touzi:// 折线图
                ((BaseActivity) this.getActivity()).goToActivity(LineChartActivity.class, null);
                break;
            case R.id.ll_touzi_zhiguan:// 柱状图
                ((BaseActivity) this.getActivity()).goToActivity(BaseActivity.class, null);
                break;
            case R.id.ll_zichan:// 饼图
                ((BaseActivity) this.getActivity()).goToActivity(PieChartActivity.class, null);
                break;
        }
    }
}
