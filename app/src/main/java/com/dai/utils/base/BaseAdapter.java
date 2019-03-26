package com.dai.utils.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    protected List<T> mList = new ArrayList<>();

    protected RecycleItemCallBack<T> recycleItemCallBack;

    public BaseAdapter(RecycleItemCallBack<T> recycleItemCallBack) {

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateVH(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindVH(holder, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected abstract void setChangedList(List<T> list);

    protected abstract RecyclerView.ViewHolder onCreateVH(ViewGroup parent, int viewType);

    protected abstract void onBindVH(RecyclerView.ViewHolder holder, int position);
}
