package com.campuscard.app.ui.holder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campuscard.app.utils.XImageUtils;
import com.campuscard.app.R;
import com.campuscard.app.ui.entity.LostAndFoundPictureVOSBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * 图片展示
 */

public class LostFoundImgAdapter extends RecyclerView.Adapter<LostFoundImgAdapter.AddressHolder> {
    private Context context;
    private List<LostAndFoundPictureVOSBean> list;

    public LostFoundImgAdapter(Context context, List<LostAndFoundPictureVOSBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LostFoundImgAdapter.AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LostFoundImgAdapter.AddressHolder(LayoutInflater.from(context).inflate(R.layout.item_content_image_info, null));
    }

    @Override
    public void onBindViewHolder(@NonNull LostFoundImgAdapter.AddressHolder holder, int position) {

        LostAndFoundPictureVOSBean lostAndFoundPictureVOSBean = list.get(position);
        XImageUtils.load(context, lostAndFoundPictureVOSBean.getImgURL(), holder.selectItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AddressHolder extends RecyclerView.ViewHolder {
        public RoundedImageView selectItem;

        public AddressHolder(View itemView) {
            super(itemView);
            selectItem = itemView.findViewById(R.id.iv_image_content);
        }
    }
}
