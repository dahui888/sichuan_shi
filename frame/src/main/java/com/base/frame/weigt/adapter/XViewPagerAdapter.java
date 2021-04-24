package com.base.frame.weigt.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 名称：XViewPagerAdapter.java
 * 描述：一个通用的ViewPager适配器
 */
public class XViewPagerAdapter extends PagerAdapter {

    /**
     * The m context.
     */
    private Context mContext;

    /**
     * The m list views.
     */
    private ArrayList<View> mListViews = null;

    /**
     * The m views.
     */
    private HashMap<Integer, View> mViews = null;

    /**
     * Instantiates a new ab view pager adapter.
     *
     * @param context    the context
     * @param mListViews the m list views
     */
    public XViewPagerAdapter(Context context, ArrayList<View> mListViews) {
        this.mContext = context;
        this.mListViews = mListViews;
        this.mViews = new HashMap<Integer, View>();
    }

    /**
     * 描述：获取数量.
     *
     * @return the count
     * @see PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return mListViews.size();
    }

    /**
     * 描述：Object是否对应这个View.
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     * @return true, if is view from object
     * @see PagerAdapter#isViewFromObject(View, Object)
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == (arg1);
    }

    /**
     * 描述：显示View.
     *
     * @param container the container
     * @param position  the position
     * @return the object
     * @see PagerAdapter#instantiateItem(View, int)
     */
    @Override
    public Object instantiateItem(View container, int position) {
        View v = mListViews.get(position);
        ((ViewPager) container).addView(v);
        return v;
    }

    /**
     * 描述：移除View.
     *
     * @param container the container
     * @param position  the position
     * @param object    the object
     * @see PagerAdapter#destroyItem(View, int, Object)
     */
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    /**
     * 描述：很重要，否则不能notifyDataSetChanged.
     *
     * @param object the object
     * @return the item position
     * @see PagerAdapter#getItemPosition(Object)
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
