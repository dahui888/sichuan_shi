package com.campuscard.app.ui.holder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.campuscard.app.R;
import com.campuscard.app.ui.entity.RechargeRecsBean;
import com.campuscard.app.utils.StringUtil;

import java.util.List;

/**
 * 消费记录
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.BaseHolder> {


    private Context context;
    private List<RechargeRecsBean> list;

    public RecordAdapter(Context context, List<RechargeRecsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseHolder(LayoutInflater.from(context).inflate(R.layout.item_record_child, null));
    }

    @Override
    public void onBindViewHolder(final BaseHolder holder, final int position) {
        RechargeRecsBean recsBean = list.get(position);
        holder.tvCardNum.setText("卡号：" + recsBean.getEcardNo());
        holder.tvAddress.setText("[" + recsBean.getRechargeWay() + "]");
        holder.tvTime.setText(recsBean.getDate());
        holder.tvMoney.setText("+" + StringUtil.doubleToString(recsBean.getRechargeAmount()) + "");

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class BaseHolder extends RecyclerView.ViewHolder {
        protected ImageView ivTag;
        protected TextView tvCardNum;
        protected TextView tvAddress;
        protected TextView tvTime;
        protected TextView tvMoney;

        public BaseHolder(View rootView) {
            super(rootView);
            ivTag = (ImageView) rootView.findViewById(R.id.iv_tag);
            tvCardNum = (TextView) rootView.findViewById(R.id.tv_card_num);
            tvAddress = (TextView) rootView.findViewById(R.id.tv_address);
            tvTime = (TextView) rootView.findViewById(R.id.tv_time);
            tvMoney = (TextView) rootView.findViewById(R.id.tv_money);
        }
    }
}
