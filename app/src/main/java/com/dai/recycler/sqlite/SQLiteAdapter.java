package com.dai.recycler.sqlite;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dai.recycler.R;
import com.dai.recycler.util.BaseRecyclerAdapter;

import butterknife.ButterKnife;

/**
 * Created by dai on 2017/9/14.
 */

public class SQLiteAdapter extends BaseRecyclerAdapter<UserInfo> {

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sqlite, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, UserInfo data) {
        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).id.setText(data.getId() + "");
            ((ViewHolder) viewHolder).name.setText(data.getName());
            ((ViewHolder) viewHolder).age.setText(data.getAge() + "");
            ((ViewHolder) viewHolder).sex.setText(data.getSex());
        }
    }

    void addItem(UserInfo userInfo) {
        data.add(userInfo);
        notifyDataSetChanged();
    }

    void updateItem(){

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView name;
        private TextView age;
        private TextView sex;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
            id = (TextView) itemView.findViewById(R.id.item_id);
            name = (TextView) itemView.findViewById(R.id.item_name);
            age = (TextView) itemView.findViewById(R.id.item_age);
            sex = (TextView) itemView.findViewById(R.id.item_sex);
        }
    }
}
