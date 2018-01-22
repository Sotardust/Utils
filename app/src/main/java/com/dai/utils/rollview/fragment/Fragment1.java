package com.dai.utils.rollview.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dai.utils.R;
import com.dai.utils.rollview.RollAdapter;
import com.dai.utils.rollview.util.ItemDecoration;
import com.dai.utils.util.BaseRecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dai on 2017/9/26.
 */

public class Fragment1 extends Fragment {


    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;
    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView2;
    Unbinder unbinder;

    private int index = 0;
    private int firstPos = 0;
    private boolean isClicked = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        final RollAdapter adapter1 = new RollAdapter();
        final RollAdapter adapter2 = new RollAdapter();
        ArrayList<String> data = new ArrayList<>();
        ArrayList<String> head = new ArrayList<>();

        for (int i = 0; i < 45; i++) {
            data.add("内容" + i);
        }
        for (int i = 0; i < 45; i++) {
            head.add("标题" + i);
        }

        adapter1.setData(head);
        adapter2.setData(data);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(adapter1);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ItemDecoration itemDecoration = new ItemDecoration();
        itemDecoration.setHead(head);
        recyclerView2.addItemDecoration(itemDecoration);
        recyclerView2.setAdapter(adapter2);

        final RightListener rightListener = new RightListener() {
            @Override
            public void onChangeListener(int position, RecyclerView recyclerView) {
                int firstPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (position >= firstPos) {
                    int index = position + (lastPos - firstPos);
                    recyclerView2.smoothScrollToPosition(index);
                } else {
                    recyclerView2.smoothScrollToPosition(position);
                }
            }
        };

        adapter1.setOnItemClickLister(new BaseRecyclerAdapter.OnItemClickLister<String>() {
            @Override
            public void onItemClick(int position, String data) {
                isClicked = true;
                rightListener.onChangeListener(position, recyclerView2);
                adapter1.setSelectTextColor(firstPos);
            }
        });


        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    isClicked = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                recyclerView1.smoothScrollToPosition(firstPos);
                if (!isClicked) {
                    adapter1.setSelectTextColor(firstPos);
                }
            }
        });


        return view;
    }


    public interface RightListener {
        void onChangeListener(int position, RecyclerView recyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
