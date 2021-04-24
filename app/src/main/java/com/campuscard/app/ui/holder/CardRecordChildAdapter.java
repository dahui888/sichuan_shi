package com.campuscard.app.ui.holder;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XDateUtil;
import com.campuscard.app.R;
import com.campuscard.app.ui.activity.home.RecordInfoActivity;
import com.campuscard.app.ui.entity.CardRecordEntity;
import com.campuscard.app.utils.StringUtil;

import java.util.List;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 校园卡充值记录
 */
public class CardRecordChildAdapter extends RecyclerView.Adapter<CardRecordChildAdapter.BaseHolder> {
    private Context context;
    private List<CardRecordEntity.ECardRechargeRecordDTOSBean> list;

    public CardRecordChildAdapter(Context context, List<CardRecordEntity.ECardRechargeRecordDTOSBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseHolder(LayoutInflater.from(context).inflate(R.layout.item_record_child, null));
    }

    @Override
    public void onBindViewHolder(final BaseHolder holder, final int position) {
        final CardRecordEntity.ECardRechargeRecordDTOSBean recsBean = list.get(position);
        holder.tvCardNum.setText("卡号：" + recsBean.getECardId());
        holder.tvAddress.setText("[持卡人：" + recsBean.getEndUserName() + "]");
        holder.tvTime.setText(XDateUtil.getStringByFormat(recsBean.getModifyDate(), XDateUtil.dateFormatMDH));
        holder.tvMoney.setText("+" + StringUtil.doubleToString(recsBean.getAmount()) + "");


        //详情
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", recsBean.getId());
                bundle.putString("type", "1");
                IntentUtil.redirectToNextActivity(context, RecordInfoActivity.class, bundle);
            }
        });
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
