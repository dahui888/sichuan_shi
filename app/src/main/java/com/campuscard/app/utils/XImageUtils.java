package com.campuscard.app.utils;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.base.frame.utils.XViewUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * 图片加载
 */
public class XImageUtils {


    public static void load(Context context, String imageUrl, ImageView imageView, int defaultDrawable) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(defaultDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(imageUrl).apply(options).into(imageView);
    }

    public static void load(Context context, String imageUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(imageUrl).apply(options).into(imageView);
    }

    public static void loadFitImage(Context context, String imageUrl, ImageView imageView, int defaultDrawable) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(defaultDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(imageUrl).apply(options).into(imageView);
    }

    public static void loadFitImage(Context context, String imageUrl, RoundedImageView imageView, int defaultDrawable) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(defaultDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(imageUrl).apply(options).into(imageView);
    }

    public static void loadFitImage(Context context, String imageUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(imageUrl).apply(options).into(imageView);
    }

    public static void loadCircle(Context context, String url, ImageView imageView) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setCircular(true)
                .setCrop(true)
                .build();
        x.image().bind(imageView, url, imageOptions);
    }

    public static void loadCircle(Context context, String url, ImageView imageView, int defaultDrawable) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setCircular(true)
                .setCrop(true)
                .setFailureDrawableId(defaultDrawable)
                .setLoadingDrawableId(defaultDrawable)
                .build();
        x.image().bind(imageView, url, imageOptions);
    }

    public static void loadRoundImage(Context context, String url, ImageView imageView, int dp) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setRadius(DensityUtil.dip2px(dp))
                .setIgnoreGif(false)
                .setCrop(true)//是否对图片进行裁剪
                .build();
        x.image().bind(imageView, url, imageOptions);
    }

    public static void loadRoundImage(Context context, String url, ImageView imageView, int defImg, int dp) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setRadius(DensityUtil.dip2px(dp))
                .setIgnoreGif(false)
                .setCrop(true)//是否对图片进行裁剪
                .setFailureDrawableId(defImg)
                .setLoadingDrawableId(defImg)
                .build();
        x.image().bind(imageView, url, imageOptions);
    }

    /**
     * 根据宽高比设置图片
     *
     * @param mContext
     * @param imageView
     * @param width
     * @param height
     */
    public static ViewGroup.LayoutParams getLayoutParams(Context mContext, ImageView imageView, double width, double height) {
        //展示图片
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = (int) (XViewUtil.getScreenWidth((Activity) mContext) / (width / height));
        return layoutParams;
    }

    /**
     * 根据屏幕宽度等分显示图片
     *
     * @param mContext
     * @param imageView
     * @param size      等分份数
     */
    public static ViewGroup.LayoutParams getLayoutParams(Context mContext, ImageView imageView, double size) {
        //展示图片
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = (int) (XViewUtil.getScreenWidth((Activity) mContext) / size);
        layoutParams.width = (int) (XViewUtil.getScreenWidth((Activity) mContext) / size);
        return layoutParams;
    }

    /**
     * 根据屏幕宽度等分显示图片
     *
     * @param mContext
     * @param imageView
     * @param size      等分份数
     */
    public static ViewGroup.LayoutParams getLayoutParams(Context mContext, RoundedImageView imageView, double size) {
        //展示图片
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = (int) (XViewUtil.getScreenWidth((Activity) mContext) / size);
        layoutParams.width = (int) (XViewUtil.getScreenWidth((Activity) mContext) / size);
        return layoutParams;
    }
}
