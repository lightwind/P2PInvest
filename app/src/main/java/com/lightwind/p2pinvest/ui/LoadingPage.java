package com.lightwind.p2pinvest.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.utils.LogUtil;
import com.lightwind.p2pinvest.utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Function：加载页面的公共类
 * Author：LightWind
 * Time：2017/11/1
 * <p>
 * <p>
 * 一、提供页面的4中状态：
 * ---1,2,3,4
 * ---mStateCurrent = 1;
 * 二、提供4个不同的页面：
 * ---正在加载的页面
 * ---加载失败的页面
 * ---返回数据为空的页面
 * ---正确显示数据的页面
 * 三、根据state_current的值，决定显示哪一个页面
 * ---默认情况下为1
 * 四、执行联网操作，根据联网的结果，决定state_current显示哪个具体的页面
 * 五、如果state_current = 4，需要考虑如何将数据传递给具体的Fragment
 */

public abstract class LoadingPage extends FrameLayout {

    // 1、定义4种不同的显示状态
    private static final int STATE_LOADING = 1;
    private static final int STATE_ERROR = 2;
    private static final int STATE_EMPTY = 3;
    private static final int STATE_SUCCESS = 4;

    private int mStateCurrent = STATE_LOADING;// 默认状态是正在加载

    // 2、提供4个不同的界面
    private View mViewLoading;
    private View mViewError;
    private View mViewEmpty;
    private View mViewSuccess;
    private LayoutParams mParams;

    private Context mContext;

    public LoadingPage(@NonNull Context context) {
        this(context, null);
    }

    public LoadingPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();// 初始化方法
    }

    /**
     * 初始化方法
     */
    private void init() {
        // 提供局部显示的参数
        mParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 实例化View
        if (mViewLoading == null) {
            // 加载布局
            mViewLoading = UIUtils.getView(R.layout.page_loading);
            // 添加到当前的FrameLayout中
            addView(mViewLoading, mParams);
        }

        if (mViewError == null) {
            mViewError = UIUtils.getView(R.layout.page_error);
            addView(mViewError, mParams);
        }

        if (mViewEmpty == null) {
            mViewEmpty = UIUtils.getView(R.layout.page_empty);
            addView(mViewEmpty, mParams);
        }

        showSafePage();
    }

    /**
     * 保证如下的操作是在主线程中进行的：更新界面
     */
    private void showSafePage() {
        UIUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 显示界面
                showPage();
            }
        });
    }

    /**
     * 显示界面
     */
    private void showPage() {
        // 根据当前的state_current的值，决定显示哪一个View
        mViewLoading.setVisibility(mStateCurrent == STATE_LOADING ? VISIBLE : INVISIBLE);
        mViewError.setVisibility(mStateCurrent == STATE_ERROR ? VISIBLE : INVISIBLE);
        mViewEmpty.setVisibility(mStateCurrent == STATE_EMPTY ? VISIBLE : INVISIBLE);

        if (mViewSuccess == null) {
//            mViewSuccess = UIUtils.getView(layoutId());// 加载布局使用的是Application
            mViewSuccess = View.inflate(mContext, layoutId(), null);// 加载布局使用的是Activity
            LogUtil.i("mViewSuccess == " + mViewSuccess);
            addView(mViewSuccess, mParams);
        }

        mViewSuccess.setVisibility(mStateCurrent == STATE_SUCCESS ? VISIBLE : INVISIBLE);
    }

    /**
     * 加载布局
     */
    public abstract int layoutId();

    private ResultState mResultState;

    /**
     * 联网，加载数据
     */
    public void show() {

        String url = url();

        // 有的页面不需要联网，就直接显示，不用模拟延迟
        if (TextUtils.isEmpty(url)) {
            mResultState = ResultState.SUCCESS;
            mResultState.setContent("");
            loadImage();// 重新设置当前状态
            return;
        }

        // 需要联网，模拟延迟
        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(url(), params(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String content) {
                        if (TextUtils.isEmpty(content)) {
//                    mStateCurrent = STATE_EMPTY;
                            mResultState = ResultState.EMPTY;
                            mResultState.setContent("");
                        } else {
//                    mStateCurrent = STATE_SUCCESS;
                            mResultState = ResultState.SUCCESS;
                            mResultState.setContent(content);
                        }

//                showSafePage();
                        loadImage();
                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
//                mStateCurrent = STATE_ERROR;
                        mResultState = ResultState.ERROR;
                        mResultState.setContent("");

//                showSafePage();
                        loadImage();
                    }
                });
            }
        }, 2000);
    }

    private void loadImage() {
        switch (mResultState) {
            case ERROR:
                mStateCurrent = STATE_ERROR;
                break;
            case EMPTY:
                mStateCurrent = STATE_EMPTY;
                break;
            case SUCCESS:
                mStateCurrent = STATE_SUCCESS;
                break;
        }
        // 根据修改以后的mStateCurrent的值，更新视图的显示
        showSafePage();

        if (mStateCurrent == STATE_SUCCESS) {
            // 将mResultState里面的数据传出去
            onSuccess(mResultState, mViewSuccess);
        }
    }

    /**
     * 联网的请求地址
     */
    protected abstract String url();

    /**
     * 联网的请求参数
     */
    protected abstract RequestParams params();

    protected abstract void onSuccess(ResultState resultState, View viewSuccess);

    /**
     * 将联网返回得到的状态和数据封装到里面
     */
    public enum ResultState {

        ERROR(2), EMPTY(3), SUCCESS(4);
        int state;

        ResultState(int state) {
            this.state = state;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        private String content;

    }
}
