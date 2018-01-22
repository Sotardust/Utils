package com.dai.utils.rollview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dai.utils.R;
import com.dai.utils.util.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dai on 2017/9/21.
 */

public class RollAdapter extends BaseRecyclerAdapter<String> {

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position, final String data) {
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).itemContent.setText(data);
            if (selectedPosition != -1 && selectedPosition == position) {
                ((ViewHolder) viewHolder).itemContent.setBackgroundResource(R.color.login_text);
            } else {
                ((ViewHolder) viewHolder).itemContent.setBackgroundResource(R.color.white);
            }
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_roll, parent, false);
        return new ViewHolder(view);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_content)
        TextView itemContent;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setSelectTextColor(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }
}
