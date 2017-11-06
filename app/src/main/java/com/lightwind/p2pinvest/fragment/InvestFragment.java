package com.lightwind.p2pinvest.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.common.BaseFragment;
import com.lightwind.p2pinvest.utils.UIUtils;
import com.loopj.android.http.RequestParams;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Function：投资Fragment
 * Author：LightWind
 * Time：2017/10/27
 */

public class InvestFragment extends BaseFragment {

    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView mIvTitleSetting;
    @BindView(R.id.tab_page_invest)
    TabPageIndicator mTabPageInvest;
    @BindView(R.id.vp_invest)
    ViewPager mVpInvest;

    @Override
    protected String getUrl() {
        return null;// 在InvestFragment没有联网，而是在里面ViewPager里面的三个子Fragment里面进行联网
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_invest;
    }

    @Override
    public void initTitle() {
        mIvTitleBack.setVisibility(View.INVISIBLE);
        mTvTitle.setText("投资");
        mIvTitleSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData(String content) {
        // 加载三个不同的Fragment：ProductListFragment、ProductRecommendFragment、ProductHotFragment
        initFragments();
        // 在ViewPager里面设置三个的显示
        MyAdapter adapter = new MyAdapter(getFragmentManager());
        mVpInvest.setAdapter(adapter);

        // 将TabPagerIndicator与ViewPager关联
        mTabPageInvest.setViewPager(mVpInvest);
    }

    private List<Fragment> mFragmentList = new ArrayList<>();

    /**
     * 加载三个不同的Fragment
     */
    private void initFragments() {
        ProductListFragment productListFragment = new ProductListFragment();
        ProductRecommendFragment productRecommendFragment = new ProductRecommendFragment();
        ProductHotFragment productHotFragment = new ProductHotFragment();

        mFragmentList.add(productListFragment);
        mFragmentList.add(productRecommendFragment);
        mFragmentList.add(productHotFragment);
    }

    /**
     * 提供PagerAdapter的实现
     * 如果ViewPager中加载的是Fragment，则提供的Adapter可以继承于FragmentPagerAdapter或FragmentStatePagerAdapter
     * FragmentPagerAdapter：适用于ViewPager中加载的Fragment不多，系统不会清理已经加载的Fragment
     * FragmentStatePagerAdapter：适用于ViewPager中加载的Fragment过多，会根据最近最少使用算法，实现内存中Fragment的清理，避免内存溢出
     */
    class MyAdapter extends FragmentPagerAdapter {
        private MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList == null ? 0 : mFragmentList.size();
        }

        /**
         * 提供TabPagerIndicator显示内容
         */
        @Override
        public CharSequence getPageTitle(int position) {
            // 方式一：
//            if (position == 0) {
//                return "全部理财";
//            } else if (position == 1) {
//                return "推荐理财";
//            } else if (position == 2) {
//                return "热门理财";
//            }

            // 方法二：
            return UIUtils.getStringArr(R.array.invest_tab)[position];
        }
    }
}
