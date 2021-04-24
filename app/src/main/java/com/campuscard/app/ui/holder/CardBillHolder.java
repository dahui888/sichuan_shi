package com.campuscard.app.ui.holder;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XDateUtil;
import com.base.frame.weigt.recycle.IViewHolder;
import com.base.frame.weigt.recycle.XViewHolder;
import com.campuscard.app.R;
import com.campuscard.app.ui.activity.card.ScreeningByTimeActivity;
import com.campuscard.app.ui.entity.CardCostEntity;
import com.campuscard.app.utils.StringUtil;

/**
 * 账单数据
 */
public class CardBillHolder extends IViewHolder {

    @Override
    protected XViewHolder create(View view, RecyclerView.Adapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_card_bill;
    }

    public class ViewHolder extends XViewHolder<CardCostEntity> {
        protected TextView tvTime;
        protected TextView tvPrice;
        protected ImageView ivZb;
        protected RecyclerView mXRecyclerView;

        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {
            super(itemView, adapter);
        }

        @Override
        protected void initView(View rootView) {
            tvTime = (TextView) rootView.findViewById(R.id.tv_time);
            tvPrice = (TextView) rootView.findViewById(R.id.tv_price);
            ivZb = (ImageView) rootView.findViewById(R.id.iv_zb);
            mXRecyclerView = (RecyclerView) rootView.findViewById(R.id.mXRecyclerView);
            mXRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mXRecyclerView.setNestedScrollingEnabled(false);
        }

        @Override
        protected void onBindData(final CardCostEntity itemData) {
            if (itemData.getConsumeDTOS() != null && itemData.getConsumeDTOS().size() > 0) {
                double money = 0;
                for (int i = 0; i < itemData.getConsumeDTOS().size(); i++) {
                    tvTime.setText(XDateUtil.getStringByFormat(itemData.getConsumeDTOS().get(0).getTime(), XDateUtil.dateFormatYMD));
                    money += itemData.getConsumeDTOS().get(i).getAmount();
                }
                tvPrice.setText("充值总额：￥" + StringUtil.doubleToString(money));
                CardBillChildAdapter childAdapter = new CardBillChildAdapter(mContext, itemData.getConsumeDTOS());
                mXRecyclerView.setAdapter(childAdapter);

                //赛选
                ivZb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtil.redirectToNextActivity(mContext, ScreeningByTimeActivity.class);
                    }
                });

            }
        }
    }
}
