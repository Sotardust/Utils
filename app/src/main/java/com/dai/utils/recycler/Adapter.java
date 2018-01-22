package com.dai.utils.recycler;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dai.utils.R;

import java.util.ArrayList;

/**
 * Created by dai on 2017/9/11.
 */

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_FOOTER = 2;
    private static final int TYPE_FOOTER_COMPLETE = 3;

    private ArrayList<String> data = new ArrayList<>();

    private View headerView;
    private View footerView;

    public void setFooterCompleteView(View footerCompleteView) {
        this.footerCompleteView = footerCompleteView;
    }

    private View footerCompleteView;


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private OnLoadMoreListener onLoadMoreListener;

    public View getFooterView() {
        return footerView;
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
        notifyItemInserted(getItemCount());
    }

    public View getHeaderView() {
        return headerView;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        notifyItemInserted(getItemCount());
    }

    public boolean isHeaderView() {
        return headerView != null;
    }


    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerView != null && viewType == TYPE_HEADER) return new ViewHolder(headerView);
        if (footerView != null && viewType == TYPE_FOOTER) return new LoadViewHolder(footerView);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
//        footerCompleteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_completed, parent, false);
//        footerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;


        System.out.println("position = " + position);
//        if (getItemViewType(position) == TYPE_FOOTER) return;
        final int pos = getRealPosition(holder);
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).content.setText(data.get(pos));
            ((ViewHolder) holder).content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("data(" + pos + ")= " + data.get(pos));
                }
            });
        } else if (holder instanceof LoadViewHolder) {
            System.out.println("Adapter.onBindViewHolder");

            requestData();
        }


    }

    private void requestData() {

        //网络请求,如果是异步请求，则在成功之后的回调中添加数据，并且调用notifyDataSetChanged方法，hasMoreData为true
        //如果没有数据了，则hasMoreData为false，然后通知变化，更新recylerview

        if (onLoadMoreListener != null) {
            onLoadMoreListener.loadMore(new LoadMoreCallBack() {
                @Override
                public void onSuccess() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    }, 2000);
                }

                @Override
                public void onCompleted(final View view) {

                }

                @Override
                public void onFailure() {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int size = headerView == null ? data.size() : data.size() + 1;
        return footerView == null ? size : size + 1;
    }

    private boolean isLoadCompleted = false;

    @Override
    public int getItemViewType(int position) {

        if (headerView == null && position == 0) return TYPE_NORMAL;
        if (footerView != null && position == getItemCount() - 1) return TYPE_FOOTER;
        return position == 0 ? TYPE_HEADER : TYPE_NORMAL;
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return headerView == null ? position : position - 1;
    }

    public void addItem(ArrayList<String> newData) {
        for (String string : newData) {
            data.add(0, string);
        }
        notifyDataSetChanged();
    }

    public void addFooterItem(ArrayList<String> newData) {
        for (String string : newData) {
            data.add(string);
        }
        System.out.println("addFooterItem data.size() = " + data.size());

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView content;

        ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }

    class LoadViewHolder extends RecyclerView.ViewHolder {
        public LoadViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    public interface OnLoadMoreListener {
        void loadMore(LoadMoreCallBack callBack);
    }

    public interface LoadMoreCallBack {
        void onSuccess();

        void onCompleted(View view);

        void onFailure();

    }
}


