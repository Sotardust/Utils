package com.dai.utils.loadmore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dai.utils.R;
import com.dai.utils.base.BaseAdapter;
import com.dai.utils.base.RecycleItemCallBack;

import java.util.List;
import java.util.logging.Handler;

public class LoadMoreAdapter extends BaseAdapter<String> {

    private static final String TAG = "dht";

    private static final int NORMAL = 0;

    private static final int FOOTER = 1;

    public LoadMoreAdapter(RecycleItemCallBack<String> recycleItemCallBack) {
        super(recycleItemCallBack);
    }

    @Override
    protected void setChangedList(List<String> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.ViewHolder onCreateVH(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_loading, parent, false);
            return new LoadViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_load_more, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    protected void onBindVH(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).content.setText(mList.get(position));

        } else if (holder instanceof LoadViewHolder) {
            if (position == getItemCount() - 1) {
                ((LoadViewHolder) holder).llLoading.setVisibility(View.VISIBLE);
                listener.onLoadMore((LoadViewHolder) holder);
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) return FOOTER;
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }


    public void setLoadMoreData(List<String> list, LoadViewHolder viewHolder, boolean isCompleted) {
        int index = mList.size();
        mList.addAll(list);
        viewHolder.llLoading.setVisibility(isCompleted ? View.GONE : View.VISIBLE);
        viewHolder.completed.setVisibility(isCompleted ? View.VISIBLE : View.GONE);
//        notifyItemInserted(index, index + list.size());
//        notifyItemRangeInserted(index, list.size());

        notifyDataSetChanged();
    }

    public interface LoadMoreListener {
        void onLoadMore(LoadViewHolder viewHolder);
    }

    private LoadMoreListener listener;

    public void LoadMoreListener(LoadMoreListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }

    class LoadViewHolder extends RecyclerView.ViewHolder {

        private TextView completed;
        private LinearLayout llLoading;

        public LoadViewHolder(View itemView) {
            super(itemView);
            completed = (TextView) itemView.findViewById(R.id.loading_completed);
            llLoading = (LinearLayout) itemView.findViewById(R.id.ll_loading);
        }
    }
}
