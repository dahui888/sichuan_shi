package com.base.frame.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * View工具类.
 */
public class XViewUtil {

    /**
     * 无效值
     */
    public static final int INVALID = Integer.MIN_VALUE;

    //屏幕宽度
    private static int mScreenWidth = 0;


    /**
     * 缩放文字大小
     *
     * @param textView button
     * @param size     sp值
     * @return
     */
    public static void setSPTextSize(TextView textView, float size) {
        float scaledSize = scaleTextValue(textView.getContext(), size);
        textView.setTextSize(scaledSize);
    }

    /**
     * 缩放文字大小,这样设置的好处是文字的大小不和密度有关，
     * 能够使文字大小在不同的屏幕上显示比例正确
     *
     * @param textView   button
     * @param sizePixels px值
     * @return
     */
    public static void setTextSize(TextView textView, float sizePixels) {
        float scaledSize = scaleTextValue(textView.getContext(), sizePixels);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, scaledSize);
    }

    /**
     * 缩放文字大小
     *
     * @param context
     * @param textPaint
     * @param sizePixels px值
     * @return
     */
    public static void setTextSize(Context context, TextPaint textPaint, float sizePixels) {
        float scaledSize = scaleTextValue(context, sizePixels);
        textPaint.setTextSize(scaledSize);
    }

    /**
     * 缩放文字大小
     *
     * @param context
     * @param paint
     * @param sizePixels px值
     * @return
     */
    public static void setTextSize(Context context, Paint paint, float sizePixels) {
        float scaledSize = scaleTextValue(context, sizePixels);
        paint.setTextSize(scaledSize);
    }

    /**
     * 描述：根据屏幕大小缩放文本.
     *
     * @param context the context
     * @param value   the px value
     * @return the int
     */
    public static int scaleTextValue(Context context, float value) {
        return scaleValue(context, value);
    }

    /**
     * 描述：根据屏幕大小缩放.
     *
     * @param context the context
     * @param value   the px value
     * @return the int
     */
    public static int scaleValue(Context context, float value) {
        DisplayMetrics mDisplayMetrics = XAppUtil.getDisplayMetrics(context);
        //为了兼容尺寸小密度大的情况
        int width = mDisplayMetrics.widthPixels;
        int height = mDisplayMetrics.heightPixels;
        //解决横屏比例问题
        if (width > height) {
            width = mDisplayMetrics.heightPixels;
            height = mDisplayMetrics.widthPixels;
        }
        if (mDisplayMetrics.scaledDensity == XAppConfig.UI_DENSITY) {
            //密度
            if (width > XAppConfig.UI_WIDTH) {
                value = value * (1.3f - 1.0f / mDisplayMetrics.scaledDensity);
            } else if (width < XAppConfig.UI_WIDTH) {
                value = value * (1.0f - 1.0f / mDisplayMetrics.scaledDensity);
            }
        } else {
            //密度小屏幕大:缩小比例
            float offset = XAppConfig.UI_DENSITY - mDisplayMetrics.scaledDensity;
            if (offset > 0.5f) {
                value = value * 0.9f;
            } else {
                value = value * 0.95f;
            }
        }
        return scale(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, value);
    }


    /**
     * 描述：根据屏幕大小缩放.
     *
     * @param displayWidth  the display width
     * @param displayHeight the display height
     * @param pxValue       the px value
     * @return the int
     */
    public static int scale(int displayWidth, int displayHeight, float pxValue) {
        if (pxValue == 0) {
            return 0;
        }
        float scale = 1;
        try {
            int width = displayWidth;
            int height = displayHeight;
            //解决横屏比例问题
            if (width > height) {
                width = displayHeight;
                height = displayWidth;
            }
            float scaleWidth = (float) width / XAppConfig.UI_WIDTH;
            float scaleHeight = (float) height / XAppConfig.UI_HEIGHT;
            scale = Math.min(scaleWidth, scaleHeight);
        } catch (Exception e) {
        }
        return Math.round(pxValue * scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return 屏幕宽度(像素)
     */
    public static int getScreenWidth(Activity activity) {
        if (mScreenWidth == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            mScreenWidth = dm.widthPixels;
        }
        return mScreenWidth;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dp
     * @return
     */
    public static int getPxByDp(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 sp(像素) 的单位 转成为 px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int getPxBySp(Context context, int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
