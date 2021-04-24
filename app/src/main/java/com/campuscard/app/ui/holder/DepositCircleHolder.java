package com.campuscard.app.ui.holder;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.utils.XDateUtil;
import com.base.frame.weigt.recycle.IViewHolder;
import com.base.frame.weigt.recycle.XViewHolder;
import com.campuscard.app.R;
import com.campuscard.app.ui.entity.RechargeRecsBean;
import com.campuscard.app.utils.StringUtil;

import java.util.List;

/**
 * 圈存记录
 * item_record--一级
 * item_record_child
 */
public class DepositCircleHolder extends IViewHolder {
    @Override
    protected XViewHolder create(View view, RecyclerView.Adapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_record;
    }

    public class ViewHolder extends XViewHolder<List<RechargeRecsBean>> {
        protected TextView tvTime;
        protected TextView tvMoney;
        private LinearLayout linData;
        protected RecyclerView mXRecyclerView;
        private RecordAdapter recordAdapter;

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
            linData = (LinearLayout) rootView.findViewById(R.id.lin_data);
        }

        @Override
        protected void onBindData(final List<RechargeRecsBean> itemData) {

            if (itemData != null && itemData.size() > 0) {
                //设置第一项
                if (getAdapterPosition() == 0) {
                    linData.setBackgroundResource(R.drawable.bg_gray_top_left_right);
                } else {
                    linData.setBackgroundResource(R.drawable.bg_gray_line_zj);
                }
                //数据展示
                double money=0;
                tvTime.setText(XDateUtil.getStringByFormat(itemData.get(0).getDate(), XDateUtil.dateFormatYMD));
                for (int i = 0; i < itemData.size(); i++) {
                    money += itemData.get(i).getRechargeAmount();
                }
                tvMoney.setText("充值总额:" + StringUtil.doubleToString(money));
                recordAdapter = new RecordAdapter(mContext, itemData);
                mXRecyclerView.setAdapter(recordAdapter);
            }
        }
    }
}
