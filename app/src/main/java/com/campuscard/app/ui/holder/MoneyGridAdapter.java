package com.campuscard.app.ui.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.base.frame.weigt.XBaseViewHolder;
import com.campuscard.app.R;
import com.campuscard.app.ui.entity.MoneyEntity;
import com.campuscard.app.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : j金额展示
 */
public class MoneyGridAdapter extends BaseAdapter {

    private Context context;
    private Double[] names = {10.0, 20.0, 30.0, 50.0, 100.0, 200.0};
    private int lastPosition = -1;
    private boolean isCean;

    public MoneyGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Double getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_money, null);
        }
        final TextView tvMoney = XBaseViewHolder.get(convertView, R.id.tv_money);
        tvMoney.setText(StringUtil.isNumeric(names[position] + "") + "元");

        if (lastPosition == position) {
            tvMoney.setSelected(true);
        } else {
            tvMoney.setSelected(false);
        }
        if (isCean) {
            tvMoney.setSelected(false);
        }
        tvMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvMoney.isSelected()) {
                    tvMoney.setSelected(false);
                    lastPosition = -1;
                    MoneyEntity moneyEntity = new MoneyEntity();
                    moneyEntity.setMoney(0);
                    EventBus.getDefault().post(moneyEntity);
                } else {
                    tvMoney.setSelected(true);
                    lastPosition = position;
                    MoneyEntity moneyEntity = new MoneyEntity();
                    moneyEntity.setMoney(names[position]);
                    EventBus.getDefault().post(moneyEntity);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    //清楚选中
    public void setClean(boolean isClean) {
        this.isCean = isClean;
        notifyDataSetChanged();
    }

}
