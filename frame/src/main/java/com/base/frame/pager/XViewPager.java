
package com.base.frame.pager;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : XViewPager 可设置是否滑动的ViewPager.
 */
public class XViewPager extends ViewPager {

    /**
     * The enabled.
     */
    private boolean enabled;
    private boolean scrollble = true;

    /**
     * Instantiates a new ab un slide view pager.
     *
     * @param context the context
     */
    public XViewPager(Context context) {
        super(context);
        this.enabled = true;
    }

    /**
     * Instantiates a new ab un slide view pager.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public XViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    /**
     * 描述：触摸没有反应就可以了.
     *
     * @param event the event
     * @return true, if successful
     * @see ViewPager#onTouchEvent(MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!scrollble) {
            return true;
        }
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    /**
     * @param event the event
     * @return true, if successful
     * @version v1.0
     * @see ViewPager#onInterceptTouchEvent(MotionEvent)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!scrollble) {
            return false;
        }
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }

    /**
     * Sets the paging enabled.
     *
     * @param enabled the new paging enabled
     */
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, enabled);
    }
}
