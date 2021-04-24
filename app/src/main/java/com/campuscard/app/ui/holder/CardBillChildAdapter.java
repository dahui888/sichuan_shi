package com.campuscard.app.ui.holder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.utils.XDateUtil;
import com.campuscard.app.R;
import com.campuscard.app.ui.entity.CardCostEntity;
import com.campuscard.app.utils.StringUtil;

import java.util.List;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 统计单
 */
public class CardBillChildAdapter extends RecyclerView.Adapter<CardBillChildAdapter.BaseHolder> {
    private Context context;
    private List<CardCostEntity.ConsumeDTOSBean> list;

    public CardBillChildAdapter(Context context, List<CardCostEntity.ConsumeDTOSBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseHolder(LayoutInflater.from(context).inflate(R.layout.item_record_child, null));
    }

    @Override
    public void onBindViewHolder(final BaseHolder holder, final int position) {
        final CardCostEntity.ConsumeDTOSBean recsBean = list.get(position);
        holder.tvCardNum.setText(recsBean.getPlace());
        holder.tvAddress.setText("[" + recsBean.getConsumeName() + "]");
        holder.tvTime.setText(XDateUtil.getStringByFormat(recsBean.getTime(), XDateUtil.dateFormatMDH));
        holder.tvMoney.setText("-" + StringUtil.douToString(recsBean.getAmount()));

        /**
         * 餐费  210
         * 商场购物  215
         * 公交支出 223
         * 购冷水支出 221
         * 购热水支出 222
         用水支出 220
         * 专用购水支出 713
         */
        switch (recsBean.getConsumeTypeCode()) {
            case "210":
                //餐费
                holder.ivTag.setImageResource(R.mipmap.ic_canfei);
                break;
            case "215":
                holder.ivTag.setImageResource(R.mipmap.ic_chaoshi);
                break;
            case "223":
                holder.ivTag.setImageResource(R.mipmap.ic_gongjiao);
                break;
            case "221":
            case "222":
            case "220":
            case "713":
                holder.ivTag.setImageResource(R.mipmap.ic_canfei);
                break;
        }
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
