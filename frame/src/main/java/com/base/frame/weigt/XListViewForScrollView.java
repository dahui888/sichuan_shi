package com.base.frame.weigt;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * ListViewC重写
 */
public class XListViewForScrollView extends ListView {
    public XListViewForScrollView(Context context) {
        super(context);
    }

    public XListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, height);
    }
}
