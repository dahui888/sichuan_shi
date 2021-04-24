package com.campuscard.app.ui.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.base.frame.weigt.recycle.IViewHolder;
import com.base.frame.weigt.recycle.XViewHolder;
import com.campuscard.app.R;
import com.campuscard.app.ui.entity.LossRecordEntity;

/**
 * 校园卡挂失记录
 */
public class CardLossRecordHolder extends IViewHolder {

    @Override
    protected XViewHolder create(View view, RecyclerView.Adapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_card_loss_code;
    }

    public class ViewHolder extends XViewHolder<LossRecordEntity> {
        protected TextView tvCardNum;
        protected TextView tvTime;

        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {
            super(itemView, adapter);
        }

        @Override
        protected void initView(View rootView) {
            tvCardNum = (TextView) rootView.findViewById(R.id.tv_card_num);
            tvTime = (TextView) rootView.findViewById(R.id.tv_time);
        }

        @Override
        protected void onBindData(final LossRecordEntity itemData) {
            tvCardNum.setText("挂失卡号：" + itemData.getEcardNo());
            tvTime.setText("挂失时间：" + itemData.getDate());
        }
    }
}
