package com.campuscard.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;


import com.campuscard.app.R;

import java.util.List;

/**
 * 描述:上滑滚动View
 * -------------------
 * \头条1--xxxxxxxx   \
 * \头条2--xxxxxxxx   \
 * -------------------
 */
public class ScrollUpView extends ViewFlipper {
    /**
     * 动画时间
     */
    private static final int ANIM_DURATION = 500;
    /**
     * 滚动间隔时间
     */
    private static final int INTERVAL = 3888;

    /**
     * 动画多久滚动一次？单位秒
     */
    private int mInterval = INTERVAL;
    /**
     * 动画时间
     */
    private int mAnimDuration = ANIM_DURATION;
    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 点击
     */
    private OnItemClickListener onItemClickListener;

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param attrs   属性.
     */
    public ScrollUpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context, mInterval, mAnimDuration);
    }

    /**
     * 初始化动画
     *
     * @param context      上下文
     * @param interval     间隔时间
     * @param animDuration 持续时间
     */
    private void init(Context context, int interval, int animDuration) {
        this.mContext = context;
        setFlipInterval(interval);
        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        animIn.setDuration(animDuration);
        setInAnimation(animIn);
        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    /**
     * 设置循环滚动的View数组
     *
     * @param views
     */
    public void setViews(final List<View> views) {
        if (views == null || views.size() == 0) {
            return;
        }
        removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            final int position = i;
            //设置监听回调
            views.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, views.get(position));
                    }
                }
            });
            addView(views.get(i));
        }
        startFlipping();
    }

    /**
     * 设置动画时间参数
     * 当外部interval 和 animDuration 参数小于0时,设置控件默认值
     *
     * @param interval     动画多久滚动一次？单位秒
     * @param animDuration 动画时间
     */
    public void setAnimTime(int interval, int animDuration) {
        // 动画持续时间>动画间隔时间
        if (interval > 0 && animDuration > 0) {
            init(mContext, interval, animDuration);
        } else {
            // 设置默认值
            init(mContext, mInterval, mAnimDuration);
        }
    }

    /**
     * 设置监听接口
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item_view的接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
