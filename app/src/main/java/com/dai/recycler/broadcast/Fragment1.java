package com.dai.recycler.broadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dai.recycler.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dai on 2017/10/24.
 */

public class Fragment1 extends Fragment {
    @BindView(R.id.add)
    Button add;
    Unbinder unbinder;
    BroadCastActivity.MyReceiver myReceiver;
    private Context context;
    private BroadCastActivity activity;

//    public Fragment1() {
//
//
//    }

    public Fragment1 newInstance(Context context) {
        this.context = context;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_broad_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = new BroadCastActivity();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Fragment1.onResume");
        myReceiver = activity.new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.dai.broadcast");
        context.registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    int number = 0;

    @OnClick(R.id.add)
    public void onViewClicked() {
        number++;
        System.out.println("number = " + number);
        Intent intent = new Intent();
        intent.setAction("com.dai.broadcast");
        intent.putExtra("number", number);
        intent.putExtra("index", 1);
        context.sendBroadcast(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("Fragment1.onPause");
        context.unregisterReceiver(myReceiver);
    }
}
