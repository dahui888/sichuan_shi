package com.campuscard.app.ui.holder;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.weigt.recycle.IViewHolder;
import com.base.frame.weigt.recycle.XViewHolder;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.ui.activity.my.OperationGuideActivity;
import com.campuscard.app.ui.entity.QuestionEntity;

/**
 * 帮助与反馈列表
 */

public class OperationGuideHolder extends IViewHolder {
    @Override
    protected XViewHolder create(View view, RecyclerView.Adapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_operationguide_list;
    }

    public class ViewHolder extends XViewHolder<QuestionEntity> {
        protected TextView tvName;

        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {
            super(itemView, adapter);
        }

        @Override
        protected void initView(View rootView) {
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
        }
        @Override
        protected void onBindData(final QuestionEntity itemData) {
            tvName.setText(itemData.getProblem());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", itemData.getId());
                    bundle.putString("url", Constant.QUESTION_INFO);
                    bundle.putString("title", "详情");
                    IntentUtil.redirectToNextActivity(mContext, OperationGuideActivity.class, bundle);
                }
            });
        }
    }
}
