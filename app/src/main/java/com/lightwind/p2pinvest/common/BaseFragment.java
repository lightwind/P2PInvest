package com.lightwind.p2pinvest.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lightwind.p2pinvest.ui.LoadingPage;
import com.loopj.android.http.RequestParams;

import butterknife.ButterKnife;

/**
 * Function：Fragment基类
 * Author：LightWind
 * Time：2017/10/30
 */

public abstract class BaseFragment extends Fragment {

    private LoadingPage mLoadingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
//        View view = UIUtils.getView(getLayoutId());// context实例是Application
        assert container != null;
        mLoadingPage = new LoadingPage(container.getContext()) {
            @Override
            public int layoutId() {
                return getLayoutId();
            }

            @Override
            protected String url() {
                return getUrl();
            }

            @Override
            protected RequestParams params() {
                return getParams();
            }

            @Override
            protected void onSuccess(ResultState resultState, View viewSuccess) {
                ButterKnife.bind(BaseFragment.this, viewSuccess);
                initTitle();
                initData(resultState.getContent());
            }
        };
        return mLoadingPage;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoadingPage.show();// 在Activity创建之后，再去执行show()，去创建Fragment，这样就不会造成空指针异常
    }

    protected abstract String getUrl();

    protected abstract RequestParams getParams();

    /**
     * 提供布局
     */
    public abstract int getLayoutId();

    /**
     * 初始化Title
     */
    protected abstract void initTitle();

    /**
     * 初始化数据
     */
    protected abstract void initData(String content);
}
