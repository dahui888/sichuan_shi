package com.base.frame.weigt;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.base.frame.R;
import com.base.frame.pager.XViewPager;
import com.base.frame.utils.XAppUtil;
import com.base.frame.weigt.adapter.XFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * 名称：XSlidingTabView.java
 * 描述：滑动的tab.
 */
public class XBottomTabView extends LinearLayout {

    /**
     * The context.
     */
    private Context context;

    /**
     * tab的线性布局.
     */
    private LinearLayout mTabLayout = null;

    /**
     * The m view pager.
     */
    private XViewPager mViewPager;

    /**
     * The m listener.
     */
    private ViewPager.OnPageChangeListener mListener;

    /**
     * tab的列表.
     */
    private ArrayList<XTabItemView> tabItemList = null;

    /**
     * 内容的View.
     */
    private ArrayList<Fragment> pagerItemList = null;

    /**
     * tab的文字.
     */
    private List<String> tabItemTextList = null;

    /**
     * tab的图标.
     */
    private List<Drawable> tabItemDrawableList = null;

    /**
     * 当前选中编号.
     */
    private int mSelectedTabIndex = 0;

    /**
     * 内容区域的适配器.
     */
    private XFragmentPagerAdapter mFragmentPagerAdapter = null;

    /**
     * tab的背景.
     */
    private int tabBackgroundResource = -1;

    /**
     * tab的文字大小.
     */
    private int tabTextSize = 30;

    /**
     * tab的文字颜色.
     */
    private int tabTextColor = Color.BLACK;

    /**
     * tab的选中文字颜色.
     */
    private int tabSelectColor = Color.WHITE;

    /**
     * 图片尺寸.
     */
    private int mDrawablesBoundsLeft, mDrawablesBoundsTop, mDrawablesBoundsRight, mDrawablesBoundsBottom;

    private int noOnClick = -1;//没有点击事件
    /**
     * The m tab click listener.
     */
    private OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            XTabItemView tabView = (XTabItemView) view;
            if (noOnClick != tabView.getIndex()) {
                setCurrentItem(tabView.getIndex());
            }
        }
    };
    /**
     * 滑块动画图片.
     */
    private ImageView mTabImg;
    /**
     * tab滑块的高度.
     */
    private int tabSlidingHeight = 3;

    /**
     * tab滑块颜色.
     */
    private int tabSlidingColor = tabSelectColor;

    /**
     * 当前tab的位置.
     */
    private int startX = 0;

    /**
     * The m width.
     */
    private int mWidth = 0;


    /**
     * Instantiates a new ab bottom tab view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public XBottomTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.rgb(255, 255, 255));

        mTabLayout = new LinearLayout(context);
        mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTabLayout.setGravity(Gravity.CENTER);

        //内容的View的适配
        mViewPager = new XViewPager(context);
        //手动创建的ViewPager,必须调用setId()方法设置一个id
        mViewPager.setId(R.id.my_view);
        pagerItemList = new ArrayList<Fragment>();
        this.addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));

        //页卡滑动图片
        mTabImg = new ImageView(context);
        mTabImg.setBackgroundColor(tabSlidingColor);
        this.addView(mTabImg, new LayoutParams(LayoutParams.WRAP_CONTENT, tabSlidingHeight));

        this.addView(mTabLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        //定义Tab栏
        tabItemList = new ArrayList<XTabItemView>();
        tabItemTextList = new ArrayList<String>();
        tabItemDrawableList = new ArrayList<Drawable>();

        DisplayMetrics mDisplayMetrics = XAppUtil.getDisplayMetrics(context);
        mWidth = mDisplayMetrics.widthPixels;

        FragmentManager mFragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();
        mFragmentPagerAdapter = new XFragmentPagerAdapter(
                mFragmentManager, pagerItemList);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setOffscreenPageLimit(3);

    }


    public void setNoOnClick(int noOnClick) {
        this.noOnClick = noOnClick;
    }

    /**
     * The listener interface for receiving myOnPageChange events.
     * The class that is interested in processing a myOnPageChange
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addMyOnPageChangeListener<code> method. When
     * the myOnPageChange event occurs, that object's appropriate
     * method is invoked.
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /* (non-Javadoc)
         * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
         */
        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (mListener != null) {
                mListener.onPageScrollStateChanged(arg0);
            }

        }

        /* (non-Javadoc)
         * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
         */
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (mListener != null) {
                mListener.onPageScrolled(arg0, arg1, arg2);
            }
        }

        /* (non-Javadoc)
         * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
         */
        @Override
        public void onPageSelected(int arg0) {
            setCurrentItem(arg0);
            if (mListener != null) {
                mListener.onPageSelected(arg0);
            }
        }

    }

    /**
     * 描述：设置显示哪一个.
     *
     * @param index the new current item
     */
    public void setCurrentItem(int index) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = index;
        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final XTabItemView child = (XTabItemView) mTabLayout.getChildAt(i);
            final boolean isSelected = (i == index);
            child.setSelected(isSelected);
            if (isSelected) {
                if (tabBackgroundResource != -1) {
                    child.setTabBackgroundResource(tabBackgroundResource);
                }
                if (tabItemDrawableList.size() >= tabCount * 2) {
                    child.setTabCompoundDrawables(null, tabItemDrawableList.get(index * 2 + 1), null, null);
                } else if (tabItemDrawableList.size() >= tabCount) {
                    child.setTabCompoundDrawables(null, tabItemDrawableList.get(index), null, null);
                }
                child.setTabTextColor(tabSelectColor);
                mViewPager.setCurrentItem(index, false);// false 取消动画
            } else {
                if (tabBackgroundResource != -1) {
                    child.setTabBackgroundDrawable(null);
                }
                if (tabItemDrawableList.size() >= tabCount * 2) {
                    child.setTabCompoundDrawables(null, tabItemDrawableList.get(i * 2), null, null);
                }
                child.setTabTextColor(tabTextColor);
            }
        }

        //判断滑动距离
        int itemWidth = mWidth / tabItemList.size();

        LayoutParams mParams = new LayoutParams(itemWidth, tabSlidingHeight);
        mParams.topMargin = -tabSlidingHeight;
        mTabImg.setLayoutParams(mParams);

        int toX = itemWidth * index;
        imageSlide(mTabImg, startX, toX, 0, 0);
        startX = toX;

        mSelectedTabIndex = index;
    }

    /**
     * 描述：设置一个外部的监听器.
     *
     * @param listener the new on page change listener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    /* (non-Javadoc)
     * @see android.widget.LinearLayout#onMeasure(int, int)
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 描述：设置单个tab的背景选择器.
     *
     * @param resid the new tab background resource
     */
    public void setTabBackgroundResource(int resid) {
        tabBackgroundResource = resid;
    }

    /**
     * 描述：设置Tab的背景.
     *
     * @param resid the new tab layout background resource
     */
    public void setTabLayoutBackgroundResource(int resid) {
        this.mTabLayout.setBackgroundResource(resid);
    }

    /**
     * Gets the tab text size.
     *
     * @return the tab text size
     */
    public int getTabTextSize() {
        return tabTextSize;
    }

    /**
     * Sets the tab text size.
     *
     * @param tabTextSize the new tab text size
     */
    public void setTabTextSize(int tabTextSize) {
        this.tabTextSize = tabTextSize;
    }

    /**
     * 描述：设置tab文字的颜色.
     *
     * @param tabColor the new tab text color
     */
    public void setTabTextColor(int tabColor) {
        this.tabTextColor = tabColor;
    }

    /**
     * 描述：设置选中的颜色.
     *
     * @param tabColor the new tab select color
     */
    public void setTabSelectColor(int tabColor) {
        this.tabSelectColor = tabColor;
    }

    /**
     * 描述：创造一个Tab.
     *
     * @param text  the text
     * @param index the index
     */
    private void addTab(String text, int index) {
        addTab(text, index, null);
    }

    /**
     * 描述：创造一个Tab.
     *
     * @param text  the text
     * @param index the index
     * @param top   the top
     */
    private void addTab(String text, int index, Drawable top) {

        XTabItemView tabView = new XTabItemView(this.context);

        if (top != null) {
            tabView.setTabCompoundDrawables(null, top, null, null);
            tabView.setTabCompoundDrawablesBounds(mDrawablesBoundsLeft, mDrawablesBoundsTop, mDrawablesBoundsRight, mDrawablesBoundsBottom);
        }
        tabView.setTabTextColor(tabTextColor);
        tabView.setTabTextSize(tabTextSize);

        tabView.init(index, text);
        tabItemList.add(tabView);
        tabView.setOnClickListener(mTabClickListener);
        mTabLayout.addView(tabView, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
    }

    /**
     * 描述：图片尺寸,在addItemViews之前设置.
     *
     * @param left   the left
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     */
    public void setTabCompoundDrawablesBounds(int left, int top, int right, int bottom) {
        mDrawablesBoundsLeft = left;
        mDrawablesBoundsTop = top;
        mDrawablesBoundsRight = right;
        mDrawablesBoundsBottom = bottom;
    }

    /**
     * 描述：tab有变化刷新.
     */
    public void notifyTabDataSetChanged() {
        mTabLayout.removeAllViews();
        tabItemList.clear();
        final int count = mFragmentPagerAdapter.getCount();
        for (int i = 0; i < count; i++) {
            if (tabItemDrawableList.size() >= count * 2) {
                addTab(tabItemTextList.get(i), i, tabItemDrawableList.get(i * 2));
            } else if (tabItemDrawableList.size() >= count) {
                addTab(tabItemTextList.get(i), i, tabItemDrawableList.get(i));
            } else {
                addTab(tabItemTextList.get(i), i);
            }
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }


    /**
     * 描述：增加一组内容与tab.
     *
     * @param tabTexts  the tab texts
     * @param fragments the fragments
     */
    public void addItemViews(List<String> tabTexts, List<Fragment> fragments) {

        tabItemTextList.addAll(tabTexts);
        pagerItemList.addAll(fragments);

        mFragmentPagerAdapter.notifyDataSetChanged();
        notifyTabDataSetChanged();
    }

    /**
     * 描述：增加一组内容与tab附带顶部图片.
     *
     * @param tabTexts  the tab texts
     * @param fragments the fragments
     * @param drawables the drawables
     */
    public void addItemViews(List<String> tabTexts, ArrayList<Fragment> fragments, List<Drawable> drawables) {

        tabItemTextList.addAll(tabTexts);
        pagerItemList.addAll(fragments);
        tabItemDrawableList.addAll(drawables);
        mFragmentPagerAdapter.notifyDataSetChanged();
        notifyTabDataSetChanged();
    }

    /**
     * 描述：增加一个内容与tab.
     *
     * @param tabText  the tab text
     * @param fragment the fragment
     */
    public void addItemView(String tabText, Fragment fragment) {
        tabItemTextList.add(tabText);
        pagerItemList.add(fragment);
        mFragmentPagerAdapter.notifyDataSetChanged();
        notifyTabDataSetChanged();
    }

    /**
     * 描述：增加一个内容与tab.
     *
     * @param tabText         the tab text
     * @param fragment        the fragment
     * @param drawableNormal  the drawable normal
     * @param drawablePressed the drawable pressed
     */
    public void addItemView(String tabText, Fragment fragment, Drawable drawableNormal, Drawable drawablePressed) {
        tabItemTextList.add(tabText);
        pagerItemList.add(fragment);
        tabItemDrawableList.add(drawableNormal);
        tabItemDrawableList.add(drawablePressed);
        mFragmentPagerAdapter.notifyDataSetChanged();
        notifyTabDataSetChanged();
    }


    /**
     * 描述：删除某一个.
     *
     * @param index the index
     */
    public void removeItemView(int index) {

        mTabLayout.removeViewAt(index);
        pagerItemList.remove(index);
        tabItemList.remove(index);
        tabItemDrawableList.remove(index);
        mFragmentPagerAdapter.notifyDataSetChanged();
        notifyTabDataSetChanged();
    }

    /**
     * 描述：删除所有.
     */
    public void removeAllItemViews() {
        mTabLayout.removeAllViews();
        pagerItemList.clear();
        tabItemList.clear();
        tabItemDrawableList.clear();
        mFragmentPagerAdapter.notifyDataSetChanged();
        notifyTabDataSetChanged();
    }

    /**
     * 描述：获取这个View的ViewPager.
     *
     * @return the view pager
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 描述：设置每个tab的边距.
     *
     * @param left   the left
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     */
    public void setTabPadding(int left, int top, int right, int bottom) {
        for (int i = 0; i < tabItemList.size(); i++) {
            XTabItemView tabView = tabItemList.get(i);
            tabView.setPadding(left, top, right, bottom);
        }
    }

    /**
     * Sets the sliding enabled.
     *
     * @param sliding the new sliding enabled
     */
    public void setSlidingEnabled(boolean sliding) {
        mViewPager.setPagingEnabled(sliding);
    }

    public XTabItemView getTabItemList(int pos) {
        return tabItemList.get(pos);
    }


    /**
     * Gets the tab sliding color.
     *
     * @return the tab sliding color
     */
    public int getTabSlidingColor() {
        return tabSlidingColor;
    }

    /**
     * Sets the tab sliding color.
     *
     * @param tabSlidingColor the new tab sliding color
     */
    public void setTabSlidingColor(int tabSlidingColor) {
        this.tabSlidingColor = tabSlidingColor;
        this.mTabImg.setBackgroundColor(tabSlidingColor);
    }

    /**
     * 描述：滑动动画.
     *
     * @param v      the v
     * @param startX the start x
     * @param toX    the to x
     * @param startY the start y
     * @param toY    the to y
     */
    public void imageSlide(View v, int startX, int toX, int startY, int toY) {
        TranslateAnimation anim = new TranslateAnimation(startX, toX, startY, toY);
        anim.setDuration(100);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }

}
