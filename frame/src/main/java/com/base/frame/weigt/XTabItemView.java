package com.base.frame.weigt;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.R;
import com.base.frame.utils.XViewUtil;


/**
 * 名称：AbTabItemView.java
 *
 */
public class XTabItemView extends LinearLayout {
    private TextView red;
    /**
     * The m context.
     */
    private Context mContext;

    /**
     * 当前的索引.
     */
    private int mIndex;

    /**
     * 包含的TextView.
     */
    private TextView mTextView;

    /**
     * 图片.
     */
    private Drawable mLeftDrawable, mTopDrawable, mRightDrawable, mBottomDrawable;

    /**
     * Bounds.
     */
    private int leftBounds, topBounds, rightBounds, bottomBounds;

    /**
     * Instantiates a new ab tab item view.
     *
     * @param context the context
     */
    public XTabItemView(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new ab tab item view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public XTabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setGravity(Gravity.CENTER);
        this.setPadding(10, 10, 10, 10);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.xtabitem, null);

        mTextView = (TextView) view.findViewById(R.id.icon);
        mTextView.setFocusable(true);
        mTextView.setPadding(0, 0, 0, 0);

        mTextView.setCompoundDrawablePadding(10);
        mTextView.setSingleLine();
        red = (TextView) view.findViewById(R.id.textnum);
        red.setBackgroundResource(R.drawable.xy_bg_circle_red);
        this.addView(view);

    }

    /**
     * 设置角标
     *
     * @param num
     */
    public void setNum(String num) {
        if (num == null || num.equals("0")) {
            red.setVisibility(GONE);
        } else {
            red.setVisibility(VISIBLE);
//            red.setText(num);
        }
    }


    /**
     * Inits the.
     *
     * @param index the index
     * @param text  the text
     */
    public void init(int index, String text) {
        mIndex = index;
        mTextView.setText(text);
    }


    /**
     * Gets the index.
     *
     * @return the index
     */
    public int getIndex() {
        return mIndex;
    }


    /**
     * Gets the text view.
     *
     * @return the text view
     */
    public TextView getTextView() {
        return mTextView;
    }

    /**
     * 描述：设置文字大小.
     *
     * @param tabTextSize the new tab text size
     */
    public void setTabTextSize(int tabTextSize) {
        XViewUtil.setTextSize(mTextView, tabTextSize);
    }

    /**
     * 描述：设置文字颜色.
     *
     * @param tabColor the new tab text color
     */
    public void setTabTextColor(int tabColor) {
        mTextView.setTextColor(tabColor);
    }

    /**
     * 描述：设置文字的图片.
     *
     * @param left   the left
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     */
    public void setTabCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        mLeftDrawable = left;
        mTopDrawable = top;
        mRightDrawable = right;
        mBottomDrawable = bottom;

        if (mLeftDrawable != null) {
            mLeftDrawable.setBounds(leftBounds, topBounds, rightBounds, bottomBounds);
        }
        if (mTopDrawable != null) {
            mTopDrawable.setBounds(leftBounds, topBounds, rightBounds, bottomBounds);
        }
        if (mRightDrawable != null) {
            mRightDrawable.setBounds(leftBounds, topBounds, rightBounds, bottomBounds);
        }
        if (mBottomDrawable != null) {
            mBottomDrawable.setBounds(leftBounds, topBounds, rightBounds, bottomBounds);
        }
        mTextView.setCompoundDrawables(mLeftDrawable, mTopDrawable, mRightDrawable, mBottomDrawable);
    }

    /**
     * 描述：设置图片尺寸.
     *
     * @param left   the left
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     */
    public void setTabCompoundDrawablesBounds(int left, int top, int right, int bottom) {
        leftBounds = XViewUtil.scaleValue(mContext, left);
        topBounds = XViewUtil.scaleValue(mContext, top);
        rightBounds = XViewUtil.scaleValue(mContext, right);
        bottomBounds = XViewUtil.scaleValue(mContext, bottom);
    }

    /**
     * 描述：设置tab的背景选择.
     *
     * @param resid the new tab background resource
     */
    public void setTabBackgroundResource(int resid) {
        this.setBackgroundResource(resid);
    }

    /**
     * 描述：设置tab的背景选择.
     *
     * @param d the new tab background drawable
     */
    public void setTabBackgroundDrawable(Drawable d) {
        this.setBackgroundDrawable(d);
    }

}