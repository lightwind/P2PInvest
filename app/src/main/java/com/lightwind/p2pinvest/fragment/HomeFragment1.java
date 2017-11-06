package com.lightwind.p2pinvest.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.bean.Image;
import com.lightwind.p2pinvest.bean.Index;
import com.lightwind.p2pinvest.bean.Product;
import com.lightwind.p2pinvest.common.AppNetConfig;
import com.lightwind.p2pinvest.utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Function：首页Fragment
 * Author：LightWind
 * Time：2017/10/27
 */

public class HomeFragment1 extends Fragment {

    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView mIvTitleSetting;
    Unbinder unbinder;
//    @BindView(R.id.vp_home)
//    ViewPager mVpHome;
    @BindView(R.id.tv_home_product)
    TextView mTvHomeProduct;
    @BindView(R.id.tv_home_year_rate)
//    TextView mTvHomeYearRate;
//    @BindView(R.id.cp_home_indicator)
    CirclePageIndicator mCpHomeIndicator;
    private Index mIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = UIUtils.getView(R.layout.fragment_home);// context实例是Application
//        View view = View.inflate(getActivity(), R.layout.fragment_home, null);// context实例是Activity
        unbinder = ButterKnife.bind(this, view);
        // 初始化Title
        initTitle();
        // 初始化数据
        initData();
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mIndex = new Index();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppNetConfig.INDEX, new AsyncHttpResponseHandler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String content) {// 响应成功
                // 解析JSON数据 -- 使用FastJson
                JSONObject jsonObject = JSON.parseObject(content);
                // 解析Json对象数据
                String proInfo = jsonObject.getString("proInfo");
                Product product = JSON.parseObject(proInfo, Product.class);
                // 解析Json数组数据
                String imageArr = jsonObject.getString("imageArr");
                List<Image> imageList = JSON.parseArray(imageArr, Image.class);
                mIndex.mProduct = product;
                mIndex.mImageList = imageList;

                // 更新页面的数据
//                mTvHomeProduct.setText(product.name);
//                mTvHomeYearRate.setText(product.yearRate + "%");
//
//                // 设置ViewPager
//                mVpHome.setAdapter(new MyAdapter());
//
//                // 小圆圈的显示
//                mCpHomeIndicator.setViewPager(mVpHome);
            }

            @Override
            public void onFailure(Throwable error, String content) {// 响应失败
                Toast.makeText(UIUtils.getContext(), "联网失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化Title
     */
    private void initTitle() {
        mIvTitleBack.setVisibility(View.INVISIBLE);
        mTvTitle.setText("首页");
        mIvTitleSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            List<Image> imageList = mIndex.mImageList;
            return imageList == null ? 0 : imageList.size();
        }

        /**
         * 界面中显示的视图是不是由当前的对象创建的
         *
         * @param view   界面中显示的视图
         * @param object 当前的对象
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 实例化
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getActivity());
            // 1、imageView显示图片
            String imageUrl = mIndex.mImageList.get(position).IMAURL;// 图片的地址
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);// 显示的位置
            Picasso.with(getActivity()).load(imageUrl).into(imageView);// 联网下载图片
            // 2、imageView添加到容器中
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);// 移除操作
        }
    }
}
