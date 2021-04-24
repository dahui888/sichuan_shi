package com.campuscard.app.ui.holder;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.base.frame.weigt.recycle.IViewHolder;
import com.base.frame.weigt.recycle.XViewHolder;
import com.campuscard.app.R;
import com.campuscard.app.ui.entity.CardCostEntity;

/**
 * 校园卡消费记录
 */
public class CardHolder extends IViewHolder {

    @Override
    protected XViewHolder create(View view, RecyclerView.Adapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.item_card_cast_code_list;
    }

    public class ViewHolder extends XViewHolder<CardCostEntity> {
        private CardCostChildAdapter cardCostChildAdapter;
        private RecyclerView mXRecyclerView;

        public ViewHolder(View itemView, RecyclerView.Adapter adapter) {
            super(itemView, adapter);
        }

        @Override
        protected void initView(View rootView) {
            mXRecyclerView = (RecyclerView) rootView.findViewById(R.id.mXRecyclerView);
            mXRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mXRecyclerView.setNestedScrollingEnabled(false);
        }

        @Override
        protected void onBindData(final CardCostEntity itemData) {

            if (itemData.getConsumeDTOS() != null && itemData.getConsumeDTOS().size() > 0) {
                cardCostChildAdapter = new CardCostChildAdapter(mContext, itemData.getConsumeDTOS(), "");
                mXRecyclerView.setAdapter(cardCostChildAdapter);
            }
        }
    }
}
