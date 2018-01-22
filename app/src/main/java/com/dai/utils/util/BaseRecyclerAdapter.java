package com.dai.utils.util;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<T> data;

    private OnItemClickLister<T> onItemClickLister;
    private OnItemLongClickLister<T> onItemLongClickLister;

    protected int selectedPosition = -1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final T value = data.get(position);
        onBindViewHolder(holder, position, value);
        if (onItemClickLister != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickLister.onItemClick(position, value);
                    selectedPosition = position;
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickLister.onItemLongClick(position, value);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickLister(OnItemClickLister<T> onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    public void setOnItemLongClickLister(OnItemLongClickLister<T> onItemLongClickLister) {
        this.onItemLongClickLister = onItemLongClickLister;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setData(ArrayList<T> data) {
        if (this.data != null) {
            this.data.clear();
        }
        this.data = data;
        notifyDataSetChanged();
    }

    public ArrayList<T> getData() {
        return data;
    }

    protected abstract void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, T data);

    protected abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);

    public interface OnItemClickLister<T> {
        void onItemClick(int position, T data);
    }

    public interface OnItemLongClickLister<T> {
        void onItemLongClick(int position, T data);
    }
}