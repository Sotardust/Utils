package com.dai.utils.loadmore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dai.utils.R;
import com.dai.utils.base.RecycleItemCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadMoreActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private LoadMoreAdapter adapter;
    private int count = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);
        ButterKnife.bind(this);


        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("数据" + i);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        adapter = new LoadMoreAdapter(recycleItemCallBack);
        adapter.setChangedList(list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.LoadMoreListener(new LoadMoreAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore(LoadMoreAdapter.LoadViewHolder viewHolder) {
                ++count;
                List<String> list1 = new ArrayList<>();
                for (int i = 10 * (count - 1); i < 10 * count; i++) {
                    list1.add("数据" + i);
                }
                adapter.setLoadMoreData(list1,viewHolder,false);
            }
        });

    }

    RecycleItemCallBack<String> recycleItemCallBack = new RecycleItemCallBack<String>() {
        @Override
        public void OnItemClickLister(String data, int position) {
            super.OnItemClickLister(data, position);
        }
    };

}
