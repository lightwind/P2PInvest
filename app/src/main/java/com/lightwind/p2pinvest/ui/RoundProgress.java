package com.lightwind.p2pinvest.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.utils.UIUtils;

/**
 * Function：自定义圆形进度条
 * Author：LightWind
 * Time：2017/10/30
 */

public class RoundProgress extends View {

    // 设置绘制圆环及文本的属性 ==> 使用自定义属性替换
//    private int roundColor = Color.GRAY;// 圆环的颜色
//    private int roundProgressColor = Color.RED;// 进度的颜色
//    private int textColor = Color.BLUE;// 文本的颜色
//    private int roundWidth = UIUtils.dp2px(10);// 圆环的宽度
//    private int testSize = UIUtils.dp2px(20);// 文本的字体大小
//    private float max = 100;// 圆环的最大值
//    private float progress = 50;// 圆环的进度

    // 使用自定义属性初始化如下的变量
    private int roundColor;// 圆环的颜色
    private int roundProgressColor; // 进度的颜色
    private int textColor;// 文本的颜色
    private float roundWidth;// 圆环的宽度
    private float testSize;// 文本的字体大小
    private int max;// 圆环的最大值
    private int progress;// 圆环的进度

    // 当前视图的宽度 = 高度
    private int mWidth;

    private Paint mPaint;

    public RoundProgress(Context context) {
        this(context, null);
    }

    public RoundProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        // 获取自定义属性
        // 获取TypedArray对象
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgress);

        // 取出所有的自定义属性
        roundColor = typedArray.getColor(R.styleable.RoundProgress_roundColor, Color.GRAY);
        roundProgressColor = typedArray.getColor(R.styleable.RoundProgress_roundProgressColor, Color.RED);
        textColor = typedArray.getColor(R.styleable.RoundProgress_textColor, Color.BLUE);
        roundWidth = typedArray.getDimension(R.styleable.RoundProgress_roundWidth, UIUtils.dp2px(10));
        testSize = typedArray.getDimension(R.styleable.RoundProgress_textSize, UIUtils.dp2px(20));
        max = typedArray.getInteger(R.styleable.RoundProgress_max, 100);
        progress = typedArray.getInteger(R.styleable.RoundProgress_progress, 10);

        typedArray.recycle();// 回收
    }

    public void setMax() {
        this.max = 100;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * 测量：获取当前视图的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = this.getMeasuredWidth();
    }

    /**
     * @param canvas 画布，对应着视图在布局中的范围区间，范围的左上角为坐标的原点
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // 1、绘制圆环
        // 圆心坐标
        int cx = mWidth / 2;
        int cy = mWidth / 2;
        float radius = mWidth / 2 - roundWidth / 2;
        mPaint.setColor(roundColor);
        mPaint.setStyle(Paint.Style.STROKE);// 设置圆环的样式
        mPaint.setStrokeWidth(roundWidth);// 设置圆环的宽度
        canvas.drawCircle(cx, cy, radius, mPaint);
        // 2、绘制圆弧
        @SuppressLint("DrawAllocation")
        RectF rectF = new RectF(roundWidth / 2, roundWidth / 2, mWidth - roundWidth / 2, mWidth - roundWidth / 2);
        mPaint.setColor(roundProgressColor);
        canvas.drawArc(rectF, -90, progress * 360 / max, false, mPaint);
        // 3、绘制文本
        String text = progress * 100 / max + "%";

        mPaint.setColor(textColor);
        mPaint.setTextSize(testSize);
        mPaint.setStrokeWidth(0);

        @SuppressLint("DrawAllocation")
        Rect rect = new Rect();// 创建一个矩形
        mPaint.getTextBounds(text, 0, text.length(), rect);// 设置矩形的宽度和高度
        // 获取左下顶点的坐标
        int x = mWidth / 2 - rect.width() / 2;
        int y = mWidth / 2 + rect.height() / 2;
        canvas.drawText(text, x, y, mPaint);
    }
}
