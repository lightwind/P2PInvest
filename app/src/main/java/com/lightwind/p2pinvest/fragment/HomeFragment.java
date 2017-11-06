package com.lightwind.p2pinvest.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.bean.Image;
import com.lightwind.p2pinvest.bean.Index;
import com.lightwind.p2pinvest.bean.Product;
import com.lightwind.p2pinvest.common.AppNetConfig;
import com.lightwind.p2pinvest.common.BaseFragment;
import com.lightwind.p2pinvest.ui.RoundProgress;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Function：首页Fragment
 * Author：LightWind
 * Time：2017/10/27
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView mIvTitleSetting;
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.tv_home_product)
    TextView mTvHomeProduct;
    @BindView(R.id.roundPro_home)
    RoundProgress mRoundProHome;
    @BindView(R.id.tv_home_year_rate)
    TextView mTvHomeYearRate;

    // 数据中的进度值
    private int mCurrentProgress;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mRoundProHome.setMax();
            for (int i = 0; i < mCurrentProgress; i++) {
                mRoundProHome.setProgress(i + 1);
                SystemClock.sleep(20);

                // 强制重绘
                mRoundProHome.postInvalidate();// 主线程、分线程都能用
            }
        }
    };

    @Override
    protected String getUrl() {
        return AppNetConfig.INDEX;
    }

    @Override
    protected RequestParams getParams() {
        return new RequestParams();
//        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initTitle() {
        mIvTitleBack.setVisibility(View.INVISIBLE);
        mTvTitle.setText("首页");
        mIvTitleSetting.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData(String content) {
        if (!TextUtils.isEmpty(content)) {
            Index index = new Index();
            // 解析JSON数据 -- 使用FastJson
            JSONObject jsonObject = JSON.parseObject(content);
            // 解析Json对象数据
            String proInfo = jsonObject.getString("proInfo");
            Product product = JSON.parseObject(proInfo, Product.class);
            // 解析Json数组数据
            String imageArr = jsonObject.getString("imageArr");
            List<Image> imageList = JSON.parseArray(imageArr, Image.class);
            index.mProduct = product;
            index.mImageList = imageList;

            // 更新页面的数据
            mTvHomeProduct.setText(product.name);
            mTvHomeYearRate.setText(product.yearRate + "%");
            // 获取数据中的进度值
            mCurrentProgress = Integer.parseInt(index.mProduct.progress);

            // 在分线程中，实现进度的动态变化
            new Thread(mRunnable).start();

            // 加载显示Banner，包含图片和圆点
            // 设置Banner样式
            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
            // 设置图片加载器
            mBanner.setImageLoader(new GlideImageLoader());
            // 设置图片集合 -- 里面传入的是图片路径的集合，而不是图片的集合
            ArrayList<String> imageListUrl = new ArrayList<>(index.mImageList.size());
            for (int i = 0; i < index.mImageList.size(); i++) {
                imageListUrl.add(index.mImageList.get(i).IMAURL);
            }
            mBanner.setImages(imageListUrl);
            // 设置动画效果
            mBanner.setBannerAnimation(Transformer.DepthPage);
            // 设置标题集合
            String[] titles = new String[]{"分享砍学费", "人脉总动员", "想不到是这样的APP", "购物节"};
            mBanner.setBannerTitles(Arrays.asList(titles));
            // 设置自动轮播，默认为true
            mBanner.isAutoPlay(true);
            // 设置轮播时间
            mBanner.setDelayTime(2000);
            //设置指示器位置（当banner模式中有指示器时）
            mBanner.setIndicatorGravity(BannerConfig.CENTER);
            //banner设置方法全部调用完毕时最后调用
            mBanner.start();
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /*
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */

            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);

            //Picasso 加载图片简单用法
            Picasso.with(context).load((String) path).into(imageView);

            //用fresco加载图片简单用法，记得要写下面的createImageView方法
            Uri uri = Uri.parse((String) path);
            imageView.setImageURI(uri);
        }
    }
}
