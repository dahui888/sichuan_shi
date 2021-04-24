package com.campuscard.app.ui.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.base.frame.weigt.XBaseViewHolder;
import com.campuscard.app.R;
import com.campuscard.app.ui.entity.EventDeleteImageEntity;
import com.campuscard.app.ui.entity.UploadEntity;

import org.greenrobot.eventbus.EventBus;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 图片展示
 */
public class UploadFileAdapter extends BaseAdapter {

    private Context mContext;
    private List<UploadEntity> pathsList = new ArrayList<UploadEntity>();
    private boolean review = false;

    public UploadFileAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setPath(List<UploadEntity> path) {
        this.pathsList = path;
        notifyDataSetChanged();
    }

    public void addPath(UploadEntity path) {
        if (this.pathsList == null) {
            this.pathsList = new ArrayList<UploadEntity>();
        }
        this.pathsList.add(path);
    }

    public void setReview(boolean review) {
        this.review = review;
    }

    public List<UploadEntity> getPath() {
        return pathsList;
    }

    @Override
    public int getCount() {
        if (review) {
            return pathsList == null ? 0 : pathsList.size() + 0;
        } else {
            return pathsList == null ? 1 : pathsList.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.picture_attachments_layout, parent, false);
        }
        ImageView item_grida_image = XBaseViewHolder.get(convertView, R.id.item_grida_image);
        ImageView iv_del = XBaseViewHolder.get(convertView, R.id.iv_del);
        if (pathsList == null || position == pathsList.size()) {
            item_grida_image.setImageResource(R.mipmap.ic_add_pic);
            iv_del.setVisibility(View.GONE);
        } else {
            x.image().bind(item_grida_image, pathsList.get(position).path, new ImageOptions.Builder().build());
            iv_del.setVisibility(View.VISIBLE);
        }

        //是删除， 且在范围内
        if (pathsList != null && position != pathsList.size()) {
            iv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pathsList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }
}