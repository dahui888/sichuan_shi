package com.campuscard.app.ui.holder;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.weigt.recycle.IViewHolder;
import com.base.frame.weigt.recycle.XViewHolder;
import com.campuscard.app.R;
import com.campuscard.app.ui.activity.home.LostFoundInfoActivity;
import com.campuscard.app.ui.entity.LostFoundEntity;
import com.campuscard.app.utils.TelUtils;
import com.campuscard.app.utils.XImageUtils;
import com.campuscard.app.view.XRoundImageView;

public class LostAndFoundHolder extends IViewHolder {

    @Override
    protected XViewHolder create(View view, RecyclerView.Adapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_lost_found;
    }


    public class ViewHolder extends XViewHolder<LostFoundEntity> {
        protected XRoundImageView civHead;
        protected TextView tvName;
        protected TextView tvTime;
        protected TextView tvContent;
        protected RecyclerView recyclePic;
        protected TextView tvPhone;
        protected TextView tvAddress;
        protected TextView tvLook;

        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {
            super(itemView, adapter);
        }

        @Override
        protected void initView(View rootView) {
            civHead = (XRoundImageView) rootView.findViewById(R.id.civ_head);
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
            tvTime = (TextView) rootView.findViewById(R.id.tv_time);
            tvContent = (TextView) rootView.findViewById(R.id.tv_content);
            recyclePic = (RecyclerView) rootView.findViewById(R.id.recycle_pic);
            tvPhone = (TextView) rootView.findViewById(R.id.tv_phone);
            tvAddress = (TextView) rootView.findViewById(R.id.tv_address);
            tvLook = (TextView) rootView.findViewById(R.id.tv_look);
            recyclePic.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        }

        @Override
        protected void onBindData(final LostFoundEntity itemData) {
            XImageUtils.loadCircle(mContext, itemData.getHeadPortrait(), civHead, R.mipmap.ic_head);
            tvName.setText(itemData.getUserName());
            tvContent.setText(itemData.getContent());
            tvPhone.setText(itemData.getContactPhoneNo());
            tvAddress.setText(itemData.getAddress());
            tvLook.setText(itemData.getViewCount());
            tvTime.setText(itemData.getCreateDate());
            //图片
            if (itemData.getLostAndFoundPictureVOS() != null && itemData.getLostAndFoundPictureVOS().size() > 0) {
                LostFoundImgAdapter contentImageAdapter = new LostFoundImgAdapter(mContext, itemData.getLostAndFoundPictureVOS());
                recyclePic.setAdapter(contentImageAdapter);
                recyclePic.setVisibility(View.VISIBLE);
            } else {
                recyclePic.setVisibility(View.GONE);
            }
            //进入详情
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", itemData.getId());
                    IntentUtil.redirectToNextActivity(mContext, LostFoundInfoActivity.class, bundle);
                }
            });

            //电话
            tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(itemData.getContactPhoneNo())) {
                        TelUtils.callPhoneDialog(mContext, itemData.getContactPhoneNo());
                    }
                }
            });
        }
    }
}
