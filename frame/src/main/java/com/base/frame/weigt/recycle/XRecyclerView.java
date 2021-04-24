package com.base.frame.weigt.recycle;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * 自定义RecyclerView
 */
public class XRecyclerView extends XRefreshLayout {
    protected RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layout;
    protected XRecyclerViewAdapter mXRecyclerViewAdapter;
    /**
     * 设置刷新和加载
     */
    private XDefRefreshLoadView mDefineBAGRefreshWithLoadView = null;

    public XRecyclerView(Context context) {
        super(context);
        init();
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBgaRefreshLayout();
//        setIsShowLoadingMoreView(false);
        mRecyclerView = new RecyclerView(getContext());
        addView(mRecyclerView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layout);
        mXRecyclerViewAdapter = new XRecyclerViewAdapter();
        mRecyclerView.setAdapter(mXRecyclerViewAdapter);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight = 1;
        mRecyclerView.setLayoutParams(layoutParams);
    }

    /**
     * 设置 BGARefreshLayout刷新和加载
     */
    private void setBgaRefreshLayout() {
        mDefineBAGRefreshWithLoadView = new XDefRefreshLoadView(getContext(), true, true);
        //设置刷新样式
        setRefreshViewHolder(mDefineBAGRefreshWithLoadView);
    }

    public XDefRefreshLoadView getRefreshViewHolder() {
        return mDefineBAGRefreshWithLoadView;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return false;
    }


    public void setLayout(RecyclerView.LayoutManager layout) {
        this.layout = layout;
    }

    public RecyclerView.LayoutManager getLayout() {
        return layout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public XRecyclerViewAdapter getAdapter() {
        return (XRecyclerViewAdapter) mRecyclerView.getAdapter();
    }

    /**
     * 关闭
     */
    public void setPullLoadMoreCompleted() {
        endRefreshing();
        endLoadingMore();
    }

    /**
     * 刷新结束
     */
    public void endRefresh() {
        endRefreshing();
    }

    /**
     * 加载更多结束
     */
    public void endLoading() {
        endLoadingMore();
    }


}
