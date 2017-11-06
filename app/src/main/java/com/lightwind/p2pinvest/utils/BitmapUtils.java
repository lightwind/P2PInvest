package com.lightwind.p2pinvest.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Function：圆形Bitmap
 * Author：LightWind
 * Time：2017/11/3
 */

public class BitmapUtils {

    /**
     * 获取圆形的图片（有层叠）
     */
    public static Bitmap circleBitmap(Bitmap source) {

        int width = source.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);

        // 设置图片相交情况的处理方式
        // setXfermode()：设置当绘制的图像出现相交的情况的时候的处理方式，包含的常用模式有：
        // PorterDuff.Mode.SRC_IN：取两层图像交集部分，只显示上层图像
        // PorterDuff.Mode.DST_IN：取两层图像交集部分，只显示下层图像
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return bitmap;

    }

    /**
     * 实现图片的压缩
     * 设置宽高必须使用float，否则压缩的比例可能是0
     *
     * @param width  压缩后的宽
     * @param height 压缩后的高
     */
    public static Bitmap zoom(Bitmap source, float width, float height) {
        Matrix matrix = new Matrix();
        // 图片的压缩处理：参数为压缩的比例
        matrix.postScale(width / source.getWidth(), height / source.getHeight());

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }

}
