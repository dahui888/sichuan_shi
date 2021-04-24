package com.campuscard.app.ui.holder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.campuscard.app.R;

import java.util.List;

/**
 * @Description:
 * @Author xiaoTan
 * @Date Created in 19:54 2018/7/30
 */
public class CampusHolder extends RecyclerView.Adapter<CampusHolder.CampusAdapter> {
    private List<String> list;
    private Context context;
    private OnRecycleListener onRecycleListener;

    public CampusHolder(List<String> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CampusAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CampusAdapter(LayoutInflater.from(context).inflate(R.layout.item_dialog_campus,null));
    }

    @Override
    public void onBindViewHolder(CampusAdapter holder, final int position) {
        holder.textView.setText(list.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecycleListener.onRecycleListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CampusAdapter extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout RlItem;
        public CampusAdapter(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_dialog_campus);
        }
    }

    public void setOnRecycleListener(OnRecycleListener onRecycleListener) {
        this.onRecycleListener = onRecycleListener;
    }

    public interface OnRecycleListener{
        void onRecycleListener(int postion);
    }
}
