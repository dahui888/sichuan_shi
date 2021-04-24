package com.base.frame.weigt.recycle;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Adapter 中 onCreateViewHolder调用的构造Holder  的抽象类
 * Created by cheng on 2017/3/17.
 */

public abstract class IViewHolder {

    protected RecyclerView.Adapter adapter;
    protected Context mContext;
    /**
     * Adapter 中 onCreateViewHolder 调用方法
     *
     * @param parent  父viewGroup
     * @param adapter
     * @return
     */
    public XViewHolder create(ViewGroup parent, RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        this.mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        return create(view, adapter);
    }

    protected abstract XViewHolder create(View view, RecyclerView.Adapter adapter);

    public abstract
    @LayoutRes
    int getLayout();
}
