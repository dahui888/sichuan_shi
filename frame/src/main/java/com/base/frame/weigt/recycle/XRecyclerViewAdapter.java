package com.base.frame.weigt.recycle;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 2017/3/16.
 */

public class XRecyclerViewAdapter extends RecyclerView.Adapter {
    List<HolderDataEntity> list = new ArrayList();
    int array[] = new int[0];
    int count = 0;

    public XRecyclerViewAdapter() {
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateArray();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                updateArray();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                updateArray();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                updateArray();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                updateArray();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                updateArray();
            }
        });
    }

    /**
     * 计算每组数据的数量
     */
    private void updateArray() {
        count = 0;
        array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            count += list.get(i).getList().size();
            array[i] = count;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return list.get(viewType).getiViewHolder().create(parent, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = holder.getItemViewType();
        int datapos = position;
        if (type > 0) {
            datapos = position - array[holder.getItemViewType() - 1];
        }
        ((XViewHolder) holder).onBindItemData(list.get(holder.getItemViewType()).getList().get(datapos));
    }


    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return binarySearch(position);
    }

    /**
     * 绑定holder 构造器
     *
     * @param holder
     */
    public void bindHolder(IViewHolder holder) {
        list.add(new HolderDataEntity(holder));
    }

    /**
     * 绑定 holder构造器 与数据
     *
     * @param holder
     * @param list
     */
    public void bindHolder(IViewHolder holder, List list) {
        this.list.add(new HolderDataEntity(holder, list));
    }

    /**
     * 设置一组数据
     *
     * @param type
     * @param list2
     */
    public void setData(int type, List list2) {
        try{
            this.list.get(type).setList(list2);
            notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 设置一组数据
     * @param type
     * @param object
     */
    public void setData(int type, Object object) {
        try{
            List list = new ArrayList();
            list.add(object);
            this.list.get(type).setList(list);
            notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取列表
     *
     * @param type
     * @return
     */
    public List getData(int type) {
        return this.list.get(type).getList();
    }

    /**
     * 添加一条数据
     *
     * @param type   类型
     * @param object
     */
    public void addData(int type, Object object) {
        try {
            this.list.get(type).getList().add(object);
            notifyItemInserted(array[type]);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 添加一组数据
     *
     * @param type  类型
     * @param list2
     */
    public void addDataAll(int type, List list2) {
        try {
            if(list2!=null){
                this.list.get(type).getList().addAll(list2);
                notifyItemRangeInserted(array[type], list2.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 查找 当前位置是在哪组数据
     *
     * @param postion
     * @retrn
     */
    int binarySearch(int postion) {
        int count = array.length;
        for (int i = 0; i < count; i++) {
            if (array[i] > postion) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 移除指定项目
     *
     * @param adapterPosition
     */
    public void remove(int adapterPosition) {
        int type = binarySearch(adapterPosition);
        if (type > 0) {
            adapterPosition = array[type - 1];
        }
        list.get(type).getList().remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    /**
     * 移除类型中指定项目
     *
     * @param type     类型
     * @param position 当前类型中数据的位置
     */
    public void remove(int type, int position) {
        try {
            list.get(type).getList().remove(position);
            if (type > 0) {
                position = array[type - 1] + position;
            }
            notifyItemRemoved(position);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 移除一组
     *
     * @param type
     */
    public void removeGroup(int type) {
        try {
            list.get(type).setList(null);
            int adapterPosition = 0;
            if (type > 0) {
                adapterPosition = array[type - 1];
            }
            notifyItemRangeRemoved(adapterPosition, array[type] - adapterPosition);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 清空所有数据
     */
    public void removeAll(){
        try {
            list.clear();
            notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取类型count
     * @return
     */
    public int getTypeCount(){
        if (list == null){
            return 0;
        }
        return list.size();
    }
}
