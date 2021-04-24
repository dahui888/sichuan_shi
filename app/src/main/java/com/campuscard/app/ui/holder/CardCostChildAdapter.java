package com.campuscard.app.ui.holder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campuscard.app.R;
import com.campuscard.app.ui.entity.CardCostEntity;
import com.campuscard.app.utils.StringUtil;

import java.util.List;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 消费
 */
public class CardCostChildAdapter extends RecyclerView.Adapter<CardCostChildAdapter.BaseHolder> {
    private Context context;
    private List<CardCostEntity.ConsumeDTOSBean> list;
    private String type;

    public CardCostChildAdapter(Context context, List<CardCostEntity.ConsumeDTOSBean> list, String type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseHolder(LayoutInflater.from(context).inflate(R.layout.item_card_cast_code, null));
    }

    @Override
    public void onBindViewHolder(final BaseHolder holder, final int position) {
        final CardCostEntity.ConsumeDTOSBean recsBean = list.get(position);
        holder.tvName.setText(recsBean.getConsumeName());
        holder.tvPrice.setText("-" + StringUtil.douToString(recsBean.getAmount()));
        holder.tvTime.setText(recsBean.getDate());
        if (!TextUtils.isEmpty(type)) {
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.black));
        }else {
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.color_999999));
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
        protected TextView tvTime;
        protected TextView tvName;
        protected TextView tvPrice;

        public BaseHolder(View rootView) {
            super(rootView);
            tvName = (TextView) rootView.findViewById(R.id.tv_name);
            tvTime = (TextView) rootView.findViewById(R.id.tv_time);
            tvPrice = (TextView) rootView.findViewById(R.id.tv_price);
        }
    }
}
