package com.lightwind.p2pinvest.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.common.BaseActivity;
import com.lightwind.p2pinvest.fragment.HomeFragment;
import com.lightwind.p2pinvest.fragment.InvestFragment;
import com.lightwind.p2pinvest.fragment.MineFragment;
import com.lightwind.p2pinvest.fragment.MoreFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    // 还原标记
    private static final int WHAT_RESET_BACK = 1;
    @BindView(R.id.fl_main)
    FrameLayout mFlMain;
    @BindView(R.id.iv_main_home)
    ImageView mIvMainHome;
    @BindView(R.id.tv_main_home)
    TextView mTvMainHome;
    @BindView(R.id.ll_main_home)
    LinearLayout mLlMainHome;
    @BindView(R.id.iv_main_invest)
    ImageView mIvMainInvest;
    @BindView(R.id.tv_main_invest)
    TextView mTvMainInvest;
    @BindView(R.id.ll_main_invest)
    LinearLayout mLlMainInvest;
    @BindView(R.id.iv_main_mine)
    ImageView mIvMainMine;
    @BindView(R.id.tv_main_mine)
    TextView mTvMainMine;
    @BindView(R.id.ll_main_mine)
    LinearLayout mLlMainMine;
    @BindView(R.id.iv_main_more)
    ImageView mIvMainMore;
    @BindView(R.id.tv_main_more)
    TextView mTvMainMore;
    @BindView(R.id.ll_main_more)
    LinearLayout mLlMainMore;
    private FragmentTransaction mTransaction;
    private HomeFragment mHomeFragment;
    private InvestFragment mInvestFragment;
    private MineFragment mMineFragment;
    private MoreFragment mMoreFragment;
    // 是否复原
    private boolean flag = true;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_RESET_BACK:
                    flag = true;// 复原
            }
            return false;
        }
    });

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initData() {
        // 默认显示首页
        setSelect(0);

        // 模拟异常
//        String str = null;
//        try {
//            if (str.equals("abc")) {
//                LogUtil.e("abc");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @OnClick({R.id.ll_main_home, R.id.ll_main_invest, R.id.ll_main_mine, R.id.ll_main_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_main_home:
                setSelect(0);
                break;
            case R.id.ll_main_invest:
                setSelect(1);
                break;
            case R.id.ll_main_mine:
                setSelect(2);
                break;
            case R.id.ll_main_more:
                setSelect(3);
                break;
        }
    }

    /**
     * 提供相应的Fragment的显示
     */
    private void setSelect(int i) {

        // 开启事务
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        mTransaction = fragmentManager.beginTransaction();

        // 隐藏所有Fragment
        hideFragments();
        // 重置ImageView和TextView的显示状态
        resetTab();

        switch (i) {
            case 0:
                if (mHomeFragment == null) {
                    // 创建对象以后，并不会马上调用声明周期方法，而是在事务commit()之后，才调用。
                    mHomeFragment = new HomeFragment();
                    mTransaction.add(R.id.fl_main, mHomeFragment);
                }
                // 显示当前的Fragment
                mTransaction.show(mHomeFragment);

                // 错误的调用位置
//                mHomeFragment.show();

                // 改变选中项的图片和文本的颜色
                mIvMainHome.setImageResource(R.drawable.bottom02);
                mTvMainHome.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
            case 1:
                if (mInvestFragment == null) {
                    mInvestFragment = new InvestFragment();
                    mTransaction.add(R.id.fl_main, mInvestFragment);
                }
                mTransaction.show(mInvestFragment);
                mIvMainInvest.setImageResource(R.drawable.bottom04);
                mTvMainInvest.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
            case 2:
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    mTransaction.add(R.id.fl_main, mMineFragment);
                }
                mTransaction.show(mMineFragment);
                mIvMainMine.setImageResource(R.drawable.bottom06);
                mTvMainMine.setTextColor(getResources().getColor(R.color.home_back_selected01));
                break;
            case 3:
                if (mMoreFragment == null) {
                    mMoreFragment = new MoreFragment();
                    mTransaction.add(R.id.fl_main, mMoreFragment);
                }
                mTransaction.show(mMoreFragment);
                mIvMainMore.setImageResource(R.drawable.bottom08);
                mTvMainMore.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
        }
        // 提交事务
        mTransaction.commit();
    }

    /**
     * 重置ImageView和TextView的显示状态
     */
    private void resetTab() {
        mIvMainHome.setImageResource(R.drawable.bottom01);
        mIvMainInvest.setImageResource(R.drawable.bottom03);
        mIvMainMine.setImageResource(R.drawable.bottom05);
        mIvMainMore.setImageResource(R.drawable.bottom07);

        mTvMainHome.setTextColor(getResources().getColor(R.color.home_back_unselected));
        mTvMainInvest.setTextColor(getResources().getColor(R.color.home_back_unselected));
        mTvMainMine.setTextColor(getResources().getColor(R.color.home_back_unselected));
        mTvMainMore.setTextColor(getResources().getColor(R.color.home_back_unselected));

    }

    /**
     * 隐藏所有Fragment
     */
    private void hideFragments() {
        if (mHomeFragment != null) {
            mTransaction.hide(mHomeFragment);
        }
        if (mInvestFragment != null) {
            mTransaction.hide(mInvestFragment);
        }
        if (mMineFragment != null) {
            mTransaction.hide(mMineFragment);
        }
        if (mMoreFragment != null) {
            mTransaction.hide(mMoreFragment);
        }
    }

    // 重写unKeyUp()，实现两次点击，退出当前应用
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && flag) {
            Toast.makeText(this, "两秒内再点击返回按钮，退出应用！", Toast.LENGTH_SHORT).show();
            flag = false;
            // 发送延迟消息
            mHandler.sendEmptyMessageDelayed(WHAT_RESET_BACK, 2000);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    // 为了避免内存泄露，需要在onDestroy()
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
