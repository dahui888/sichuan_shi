package com.base.frame.weigt;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.base.frame.R;


/**
 * 自定义标题栏
 */
public class Toolbar extends androidx.appcompat.widget.Toolbar {

    private TextView mTitleTextView;
    private Context mContext;
    private int mTitleTextColor = 0xff333333;
    private float titleSize = 0;

    public Toolbar(Context context) {
        super(context);
        init(null);
    }
    public Toolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mContext = getContext();
        mTitleTextView = new TextView(mContext);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //此处相当于布局文件中的Android:layout_gravity属性
        lp.gravity = Gravity.CENTER;
        mTitleTextView.setLayoutParams(lp);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setTextSize(18);
        if (attrs != null) {
            TypedArray a = mContext.obtainStyledAttributes(attrs,
                    R.styleable.Toolbar);
            int c = a.getColor(R.styleable.Toolbar_titleTextColor, mTitleTextColor);
            mTitleTextView.setTextColor(a.getColor(R.styleable.Toolbar_titleTextColor, mTitleTextColor));
            mTitleTextView.setText(a.getString(R.styleable.Toolbar_title));
        } else {
            mTitleTextView.setTextColor(mTitleTextColor);
        }
        if (titleSize != 0) {
            mTitleTextView.setTextSize(titleSize);
        }
        addView(mTitleTextView);

    }

    public void setTitleSize(float size) {
        if (mTitleTextView != null) {
            mTitleTextView.setTextSize(size);
        }
        titleSize = size;
    }

    public void setTitle(CharSequence title) {
        mTitleTextView.setText(title);
    }

    /**
     * Sets the text color, size, style, hint color, and highlight color
     * from the specified TextAppearance resource.
     */
    public void setTitleTextAppearance(Context context, @StyleRes int resId) {
        if (mTitleTextView != null) {
            mTitleTextView.setTextAppearance(context, resId);
        }
    }

    /**
     * Sets the text color of the title, if present.
     *
     * @param color The new text color in 0xAARRGGBB format
     */
    public void setTitleTextColor(@ColorInt int color) {
        mTitleTextColor = color;
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(color);
        }
    }
}