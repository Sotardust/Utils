package com.dai.recycler.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dai.recycler.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dai on 2017/10/24.
 */

public class BroadCastActivity extends AppCompatActivity {
    @BindView(R.id.fragment)
    RelativeLayout fragment;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private TabLayout.Tab tab;
    String[] mTitles = new String[]{"测试1", "测试2"};

    private Fragment fragment1;
    private Fragment fragment2;

    private static SparseArray<TextView> list = new SparseArray<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        ButterKnife.bind(this);
        fragment1 = new Fragment1().newInstance(getApplicationContext());
        fragment2 = new Fragment2();
        changeFragment(0);
        initTabLayout();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("tab.getPosition() = " + tab.getPosition());

                changeFragment(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //初始化tab标签
    private void initTabLayout() {
        for (int i = 0; i < mTitles.length; i++) {
            tab = tabLayout.getTabAt(i);
            if (tab == null) {
                tab = tabLayout.newTab();
                tabLayout.addTab(tab);
            }
            tab.setCustomView(getTabView(i));
        }
    }

    public View getTabView(final int position) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_tablayout, null);
        TextView title = (TextView) view.findViewById(R.id.content);
        list.put(position,(TextView) view.findViewById(R.id.number));
        System.out.println("list = " + list.size());
        System.out.println("mTitles[position] = " + mTitles[position]);
        title.setText(mTitles[position]);
        return view;
    }

    private void changeFragment(int index) {
        Fragment fragment = index == 1 ? fragment2 : fragment1;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, fragment).commit();
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final int number = intent.getIntExtra("number", -1);
            int index = intent.getIntExtra("index", -1);
            if (index == 1) {
                BroadCastActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("list = " + list.size());
                        list.get(1).setVisibility(View.VISIBLE);
                        list.get(1).setText(String.valueOf(number));
                    }
                });
            }

        }
    }

}
