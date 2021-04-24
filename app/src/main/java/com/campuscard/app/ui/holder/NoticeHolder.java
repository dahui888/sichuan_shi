package com.campuscard.app.ui.holder;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.campuscard.app.utils.XImageUtils;
import com.base.frame.weigt.recycle.IViewHolder;
import com.base.frame.weigt.recycle.XViewHolder;
import com.campuscard.app.R;
import com.campuscard.app.ui.activity.home.NewsInfoActivity;
import com.campuscard.app.ui.entity.NoticeEntity;

public class NoticeHolder extends IViewHolder {

    @Override
    protected XViewHolder create(View view, RecyclerView.Adapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_news;
    }


    public class ViewHolder extends XViewHolder<NoticeEntity> {
        protected TextView tvTitle;
        protected TextView tvTime;
        protected TextView tvLook;
        protected ImageView ivPic;

        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {
            super(itemView, adapter);
        }

        @Override
        protected void initView(View rootView) {
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            tvTime = (TextView) rootView.findViewById(R.id.tv_time);
            tvLook = (TextView) rootView.findViewById(R.id.tv_look);
            ivPic = (ImageView) rootView.findViewById(R.id.iv_pic);
        }

        @Override
        protected void onBindData(final NoticeEntity itemData) {
            tvTitle.setText(itemData.getTitle());
            tvTime.setText(itemData.getCrateDate());
            tvLook.setText(itemData.getViewCount() + "人阅读");
            XImageUtils.loadRoundImage(mContext, itemData.getImgUrl(), ivPic, R.mipmap.ic_pic_def, 4);
            //新闻详情
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", itemData.getId());
                    bundle.putString("type", "type");
                    IntentUtil.redirectToNextActivity(mContext, NewsInfoActivity.class, bundle);
                }
            });
        }
    }
}
