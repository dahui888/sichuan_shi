package com.base.frame.weigt;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 重写GridView
 */
public class XGridViewForScrollView extends GridView {
    public XGridViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XGridViewForScrollView(Context context) {
        super(context);
    }

    public XGridViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}

