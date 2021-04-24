package com.campuscard.app.ui.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.base.frame.weigt.recycle.IViewHolder;
import com.base.frame.weigt.recycle.XViewHolder;
import com.campuscard.app.R;

/**
 * 管理发布数据列表
 */

public class ControllerMyReleaseHolder extends IViewHolder {
    @Override
    protected XViewHolder create(View view, RecyclerView.Adapter adapter) {
        return new ControllerMyReleaseHolder.ViewHolder(view, adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_controllermyrelease_list;
    }

    public class ViewHolder extends XViewHolder<String> {

        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {
            super(itemView, adapter);
        }

        @Override
        protected void initView(View rootView) {
        }

        @Override
        protected void onBindData(final String itemData) {
        }
    }
}