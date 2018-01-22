package com.dai.utils.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dai.utils.R;

/**
 * Created by dai on 2017/9/15.
 */

public class MainAdapter extends BaseRecyclerAdapter<String> {
    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, String data) {
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).content.setText(data);
        }

    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView content;

        ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
