package com.campuscard.app.ui.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.weigt.recycle.IViewHolder;
import com.base.frame.weigt.recycle.XViewHolder;
import com.campuscard.app.R;
import com.campuscard.app.ui.entity.PickUpEntity;

/**
 * 绑定校园卡
 */
public class PickUpCardHolder extends IViewHolder {

    @Override
    protected XViewHolder create(View view, RecyclerView.Adapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_pick_up;
    }

    public class ViewHolder extends XViewHolder<PickUpEntity> {

        protected TextView tvTitle;
        protected TextView tvAddress;
        protected TextView tvTime;
        protected ImageView ivTag;

        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {
            super(itemView, adapter);
        }

        @Override
        protected void initView(View rootView) {
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            tvAddress = (TextView) rootView.findViewById(R.id.tv_address);
            tvTime = (TextView) rootView.findViewById(R.id.tv_time);
            ivTag = (ImageView) rootView.findViewById(R.id.iv_tag);
        }

        @Override
        protected void onBindData(final PickUpEntity itemData) {
            tvTitle.setText(itemData.getName() + "\t\t" + itemData.getEcardNo());
            tvAddress.setText("丢失地点：" + itemData.getPlace());
            tvTime.setText("捡卡时间：" + itemData.getDate());
            switch (itemData.getEcardStatus()) {
                case "NORMAL"://正常
                    ivTag.setImageResource(R.mipmap.ic_zck);
                    break;
                case "LOSS"://挂失
                    ivTag.setImageResource(R.mipmap.ic_guashi);
                    break;
                case "BLACK"://黑卡
                    ivTag.setImageResource(R.mipmap.ic_hk);
                    break;
                case "UNNORMAL"://异常
                    ivTag.setImageResource(R.mipmap.ic_yichang);
                    break;
            }
        }
    }
}
