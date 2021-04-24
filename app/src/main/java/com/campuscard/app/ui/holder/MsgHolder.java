package com.campuscard.app.ui.holder;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.weigt.recycle.IViewHolder;
import com.base.frame.weigt.recycle.XViewHolder;
import com.campuscard.app.R;
import com.campuscard.app.ui.activity.my.MessageInfoActivity;
import com.campuscard.app.ui.entity.MsgEntity;
import com.campuscard.app.utils.XImageUtils;

/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 消息数据实绑定
 */
public class MsgHolder extends IViewHolder {

    private String type;

    public MsgHolder(String type) {
        this.type = type;
    }

    @Override
    protected XViewHolder create(View view, RecyclerView.Adapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_msg;
    }

    public class ViewHolder extends XViewHolder<MsgEntity> {
        protected TextView tvTime;
        protected ImageView ivType;
        protected TextView tvTitle;
        protected ImageView ivImageContent;
        protected TextView tvContent;

        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {
            super(itemView, adapter);
        }

        @Override
        protected void initView(View rootView) {
            tvTime = (TextView) rootView.findViewById(R.id.tv_time);
            ivType = (ImageView) rootView.findViewById(R.id.iv_type);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            ivImageContent = (ImageView) rootView.findViewById(R.id.iv_image_content);
            tvContent = (TextView) rootView.findViewById(R.id.tv_content);
        }

        @Override
        protected void onBindData(final MsgEntity itemData) {
            tvTime.setText(itemData.getSendDate());
            tvTitle.setText(itemData.getTitle());
            if (itemData.getMessageReadStatus().equals("READ")) {
                tvTitle.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            } else {
                tvTitle.setTextColor(mContext.getResources().getColor(R.color.black));
            }
            tvContent.setText(itemData.getContent());
            if (!TextUtils.isEmpty(itemData.getImgURL())) {
                ivImageContent.setVisibility(View.VISIBLE);
                XImageUtils.loadRoundImage(mContext, itemData.getImgURL(), ivImageContent, 8);
            } else {
                ivImageContent.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(type)) {
                switch (type) {
                    case "我的消息":
                        ivType.setImageResource(R.mipmap.ic_wo_xx);
                        break;
                    case "系统消息":
                        ivType.setImageResource(R.mipmap.ic_xitong);
                        break;
                    case "捡卡通知":
                        ivType.setImageResource(R.mipmap.ic_jk_tz);
                        break;
                }
            }
            //详情
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", itemData.getId());
                    IntentUtil.redirectToNextActivity(mContext, MessageInfoActivity.class, bundle);
                }
            });
        }
    }
}
