package com.campuscard.app.ui.holder;

import android.annotation.SuppressLint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.weigt.recycle.IViewHolder;
import com.base.frame.weigt.recycle.XViewHolder;
import com.campuscard.app.R;
import com.campuscard.app.ui.entity.RechargeRecsBean;
import com.campuscard.app.utils.StringUtil;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 补助
 */
public class BuZhuHolder extends IViewHolder {


    @Override
    protected XViewHolder create(View view, RecyclerView.Adapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_buzhu;
    }

    public class ViewHolder extends XViewHolder<RechargeRecsBean> {
        protected ImageView ivTag;
        protected TextView tvCardNum;
        protected TextView tvAddress;
        protected TextView tvMoney;

        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {
            super(itemView, adapter);
        }

        @Override
        protected void initView(View rootView) {
            ivTag = (ImageView) rootView.findViewById(R.id.iv_tag);
            tvCardNum = (TextView) rootView.findViewById(R.id.tv_card_num);
            tvAddress = (TextView) rootView.findViewById(R.id.tv_address);
            tvMoney = (TextView) rootView.findViewById(R.id.tv_money);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onBindData(final RechargeRecsBean itemData) {
            ivTag.setImageResource(R.mipmap.ic_car);
            tvCardNum.setText("卡号：" + itemData.getEcardNo());
            tvAddress.setText(itemData.getDate());
            tvMoney.setText(StringUtil.doubleToString(itemData.getRechargeAmount()) + "");
        }
    }
}
