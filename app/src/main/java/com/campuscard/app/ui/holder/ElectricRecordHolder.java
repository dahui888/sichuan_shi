package com.campuscard.app.ui.holder;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.base.frame.utils.XDateUtil;
import com.base.frame.weigt.recycle.IViewHolder;
import com.base.frame.weigt.recycle.XViewHolder;
import com.campuscard.app.R;
import com.campuscard.app.ui.entity.ElectricRecordEntity;
import com.campuscard.app.utils.DateTimeUtils;
import com.campuscard.app.utils.StringUtil;

/**
 * 电费充值记录
 */
public class ElectricRecordHolder extends IViewHolder {


    @Override
    protected XViewHolder create(View view, RecyclerView.Adapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_record;
    }

    public class ViewHolder extends XViewHolder<ElectricRecordEntity> {
        protected TextView tvTime;
        protected TextView tvMoney;
        protected RecyclerView mXRecyclerView;
        private ElectricRecordChildAdapter recordAdapter;

        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {
            super(itemView, adapter);
        }

        @Override
        protected void initView(View rootView) {
            tvTime = (TextView) rootView.findViewById(R.id.tv_time);
            tvMoney = (TextView) rootView.findViewById(R.id.tv_money);
            mXRecyclerView = (RecyclerView) rootView.findViewById(R.id.mXRecyclerView);
            mXRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mXRecyclerView.setNestedScrollingEnabled(false);
        }

        @Override
        protected void onBindData(final ElectricRecordEntity itemData) {
            if (TextUtils.equals(DateTimeUtils.getInstance().newTime(XDateUtil.dateFormatYM), itemData.getGroupBy())) {
                tvTime.setText("本月");
            } else {
                String time = itemData.getGroupBy().substring(itemData.getGroupBy().indexOf("-") + 1);
                if (!TextUtils.isEmpty(time)) {
                    if (time.startsWith("0")) {
                        tvTime.setText(time.replace("0", "") + "月");
                    } else {
                        tvTime.setText(time + "月");
                    }
                }
            }

            if (itemData.getElectricRechargeRecordDTOS() != null && itemData.getElectricRechargeRecordDTOS().size() > 0) {
                double money = 0;
                for (int i = 0; i < itemData.getElectricRechargeRecordDTOS().size(); i++) {
                    money += itemData.getElectricRechargeRecordDTOS().get(i).getAmount();
                }
                tvMoney.setText("充值总额：￥" + StringUtil.doubleToString(money));
                recordAdapter = new ElectricRecordChildAdapter(mContext, itemData.getElectricRechargeRecordDTOS());
                mXRecyclerView.setAdapter(recordAdapter);
            }
        }
    }
}
