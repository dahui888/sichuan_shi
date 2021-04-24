package com.campuscard.app.ui.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.base.frame.weigt.XBaseViewHolder;
import com.campuscard.app.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 分类展示
 * <p>
 * 餐费  210
 * 商场购物  215
 * 公交支出 223
 * 购冷水支出 221
 * 购热水支出 222
 * 用水支出 220
 * 专用购水支出 713
 */

public class ClassificationAdapter extends BaseAdapter {

    private Context context;
    private String[] names = {"餐费", "商超市", "公交", "购冷水", "购热水", "专用购水", "用水", "全选"};
    private String[] ids = {"210", "215", "223", "221", "222", "713", "220", "0"};
    private Map<String, String> map;

    public ClassificationAdapter(Context context) {
        this.context = context;
        map = new HashMap<String, String>();
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public String getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bill_class, null);
        }
        final TextView tvMoney = XBaseViewHolder.get(convertView, R.id.tv_money);
        tvMoney.setText(names[position]);

        tvMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvMoney.isSelected()) {
                    tvMoney.setSelected(false);
                    map.remove(ids[position]);
                } else {
                    tvMoney.setSelected(true);
                    map.put(ids[position], ids[position]);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public Map<String, String> getMap() {
        return map;
    }

}
