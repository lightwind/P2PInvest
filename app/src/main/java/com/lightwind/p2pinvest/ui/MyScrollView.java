package com.lightwind.p2pinvest.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.lightwind.p2pinvest.utils.UIUtils;

/**
 * Function：自定义ScrollView
 * Author：LightWind
 * Time：2017/10/30
 */

public class MyScrollView extends ScrollView {

    private View mChildView;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
//    }

    /**
     * 获取子视图
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            mChildView = getChildAt(0);
        }
    }

    private int mLastY;// 上一次Y轴上的坐标位置
    private Rect mRect = new Rect();// 用于记录临界状态的上下左右
    private boolean isFinishAnimation = true;// 动画是否结束

    private int mLastX, mDowmX, mDownY;

    /**
     * 拦截：实现父视图对子视图的拦截
     * 是否拦截成功，取决于方法的返回值
     *
     * @return true：拦截成功；false：拦截失败
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        int eventX = (int) ev.getX();
        int eventY = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = mDowmX = eventX;
                mLastY = mDownY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                // 获取水平方向和竖直方向移动的距离
                int absX = Math.abs(eventX - mDowmX);
                int absY = Math.abs(eventY - mDownY);

                // 如果竖直上移动的距离大于水平移动的距离并且竖直方向上移动的距离大于10dp，就认为是竖直移动
                if (absY > absX && absY >= UIUtils.dp2px(10)) {
                    isIntercept = true;
                }

                mLastX = eventX;
                mLastY = eventY;
                break;
        }
        return isIntercept;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mChildView == null || !isFinishAnimation) {
            return super.onTouchEvent(ev);
        }
        int eventY = (int) ev.getY();// 获取当前Y轴上的坐标
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = eventY - mLastY;// 用户移动了多少
                if (isNeedMove()) {
                    if (mRect.isEmpty()) {
                        // 记录mChildView临界位置的坐标值
                        mRect.set(mChildView.getLeft(), mChildView.getTop(),
                                mChildView.getRight(), mChildView.getBottom());
                    }
                    // 重新布局
                    mChildView.layout(mChildView.getLeft(), mChildView.getTop() + dy / 2,
                            mChildView.getRight(), mChildView.getBottom() + dy / 2);
                }

                mLastY = eventY;// 重新赋值
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    // 使用平移动画
                    int translateY = mChildView.getBottom() - mRect.bottom;
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -translateY);
                    translateAnimation.setDuration(200);

                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isFinishAnimation = false;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isFinishAnimation = true;
                            // 清除动画
                            mChildView.clearAnimation();
                            // 重新布局
                            mChildView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
                            // 清除mRect的数据
                            mRect.setEmpty();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    mChildView.setAnimation(translateAnimation);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean isNeedAnimation() {
        return !mRect.isEmpty();
    }

    /**
     * 判断是否需要执行平移动画
     */
    private boolean isNeedMove() {
        // 获取子视图的高度
        int childMeasuredHeight = mChildView.getMeasuredHeight();
        // 获取布局的高度
        int scrollViewMeasuredHeight = this.getMeasuredHeight();

        int dy = childMeasuredHeight - scrollViewMeasuredHeight;// dy >= 0

        int scrollY = this.getScrollY();// 在Y轴上的偏移量（向上滑为正数）
        if (scrollY <= 0 || scrollY >= dy) {
            return true;// 按照自定义的MyScrollView的方式处理
        }
        // 在范围内的，返回false，按照系统的ScrollView的方式处理
        return false;
    }
}
