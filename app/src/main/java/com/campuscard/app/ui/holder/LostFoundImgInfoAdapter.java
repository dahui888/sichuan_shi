package com.campuscard.app.ui.holder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.campuscard.app.R;
import com.campuscard.app.ui.entity.LostAndFoundPictureVOSBean;
import com.campuscard.app.utils.XImageUtils;

import java.util.List;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 图片展示
 */
public class LostFoundImgInfoAdapter extends RecyclerView.Adapter<LostFoundImgInfoAdapter.AddressHolder> {
    private Context context;
    private List<LostAndFoundPictureVOSBean> list;

    public LostFoundImgInfoAdapter(Context context, List<LostAndFoundPictureVOSBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LostFoundImgInfoAdapter.AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LostFoundImgInfoAdapter.AddressHolder(LayoutInflater.from(context).inflate(R.layout.item_content_image, null));
    }

    @Override
    public void onBindViewHolder(@NonNull LostFoundImgInfoAdapter.AddressHolder holder, int position) {

        LostAndFoundPictureVOSBean lostAndFoundPictureVOSBean = list.get(position);
        holder.selectItem.setLayoutParams(XImageUtils.getLayoutParams(context, holder.selectItem, 1));
        XImageUtils.loadFitImage(context, lostAndFoundPictureVOSBean.getImgURL(), holder.selectItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AddressHolder extends RecyclerView.ViewHolder {
        public ImageView selectItem;

        public AddressHolder(View itemView) {
            super(itemView);
            selectItem = itemView.findViewById(R.id.iv_image_content);
        }
    }
}
