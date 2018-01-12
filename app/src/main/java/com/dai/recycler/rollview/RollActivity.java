package com.dai.recycler.rollview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.dai.recycler.R;
import com.dai.recycler.rollview.fragment.Fragment1;
import com.dai.recycler.rollview.fragment.Fragment2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dai on 2017/9/21.
 */

public class RollActivity extends AppCompatActivity {


    @BindView(R.id.backdrop)
    ImageView backdrop;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing)
    CollapsingToolbarLayout collapsing;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
//    @BindView(R.id.fab)
//    FloatingActionButton fab;
    private TabLayout.Tab tab;
    String[] mTitles = new String[]{"商品", "评价"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);
        ButterKnife.bind(this);
//        collapsing.setTitle("标题");
        initTabLayout();
    }

    //初始化tab标签
    private void initTabLayout() {
//        for (int i = 0; i < mTitles.length; i++) {
//            tab = tabLayout.getTabAt(i);
//            if (tab == null) {
//                tab = tabLayout.newTab();
//                tabLayout.addTab(tab);
//            }
//            tab.setCustomView(getTabView(i));
//        }
        viewpager.setAdapter(new Adapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewpager);
    }

//    public View getTabView(final int position) {
//        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_recycler, null);
//        TextView title = (TextView) view.findViewById(R.id.content);
//        System.out.println("mTitles[position] = " + mTitles[position]);
//        title.setText(mTitles[position]);
//        return view;
//    }

    class Adapter extends FragmentPagerAdapter {

        Adapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new Fragment1();
                    break;
                case 1:
                    fragment = new Fragment2();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

}
