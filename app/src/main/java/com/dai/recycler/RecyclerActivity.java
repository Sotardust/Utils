package com.dai.recycler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.dai.recycler.recycler.Adapter;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dai on 2017/9/11.
 */

public class RecyclerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        final ArrayList<String> data = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("内容" + i);
        }
        for (int i = 0; i < 4; i++) {
            titles.add("header" + i);
        }
        titles.add("header" + 4);
        titles.add("header" + 4);
        titles.add("header" + 4);
        titles.add("header" + 4);
        for (int i = 5; i < 17; i++) {
            titles.add("header" + i);
        }
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//        LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        View headView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_head, recyclerView, false);
        View footerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_footer, recyclerView, false);
        final Adapter adapter = new Adapter();
        adapter.setData(data);
//        adapter.setHeaderView(headView);
        adapter.setFooterView(footerView);
//        ItemDecoration itemDecoration = new ItemDecoration.Builder(getApplicationContext())
//                .setHeads(titles)
//                .isFooterView()
//                .isHeaderView()
//                .setDivideColor(R.color.colorAccent)
//                .setHeadBackGround(R.color.color_red_ccfa3c55)
//                .setTextColor(R.color.black)
//                .setDivideHeight(1)
//                .build();
//        recyclerView.addItemDecoration(itemDecoration);

        final View footerCompleteView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_footer_completed, recyclerView, false);
        adapter.setOnLoadMoreListener(new Adapter.OnLoadMoreListener() {
            int index = 0;

            @Override
            public void loadMore(final Adapter.LoadMoreCallBack callBack) {
                callBack.onCompleted(footerCompleteView);
//                adapter.setFooterView(footerCompleteView);
//                ArrayList<String> data = new ArrayList<>();
//                for (int i = 0; i < 20; i++) {
//                    data.add("下拉刷新次数" + index++);
//                }
//                adapter.addFooterItem(data);
                Single.create(new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(SingleEmitter<String> e) throws Exception {

                        e.onSuccess("dafd");
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(String s) {
                                System.out.println("s = " + s);

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });


            }
        });

        recyclerView.setAdapter(adapter);

//        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            int index = 0;
//
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ArrayList<String> newData = new ArrayList<String>();
//
//                        newData.add("new item" + index++);
//                        adapter.addItem(newData);
//                        swipeRefreshLayout.setRefreshing(false);
////                        Toast.makeText(RecyclerRefreshActivity.this, "更新了五条数据...", Toast.LENGTH_SHORT).show();
//                    }
//                }, 1000);
//
//            }
//        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                System.out.println("newState = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                System.out.println("recyclerView getScrollState  = " + recyclerView.getScrollState());
            }

        });

        recyclerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                System.out.println("v = " + v);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                System.out.println("v = " + v);
            }
        });


    }
}
