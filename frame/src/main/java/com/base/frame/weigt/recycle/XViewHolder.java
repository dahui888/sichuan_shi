package com.base.frame.weigt.recycle;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


/**
 * XViewHolder 基础抽象类
 *
 * @param <T>
 */
public abstract class XViewHolder<T> extends RecyclerView.ViewHolder {
    RecyclerView.Adapter adapter;
    T itemData;

    public XViewHolder(View itemView, RecyclerView.Adapter adapter) {
        super(itemView);
        this.adapter = adapter;
        initView(itemView);
    }

    /**
     * 初始化view
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);

    /**
     * 绑定数据
     *
     * @param itemData
     */
    public void onBindItemData(T itemData) {
        this.itemData = itemData;
        onBindData(itemData);
    }


    /**
     * 抽象绑定数据方法
     *
     * @param itemData
     */
    protected abstract void onBindData(T itemData);

    /**
     * 更新adapter数据
     */
    public void notifyDataetChanged() {
        adapter.notifyDataSetChanged();
    }

    /**
     * 更新adapter数据
     */
    public void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    /**
     * 移除当前项
     */
    protected void remove() {
        ((XRecyclerViewAdapter) adapter).remove(getAdapterPosition());
    }

}
