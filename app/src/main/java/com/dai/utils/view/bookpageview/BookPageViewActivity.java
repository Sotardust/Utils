package com.dai.utils.view.bookpageview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dai.utils.R;
import com.dai.utils.view.util.PicturesPageFactory;

/**
 * Created by dai on 2017/12/19.
 */

public class BookPageViewActivity extends AppCompatActivity {
    //    @BindView(R.id.bookPageView)
//    OptimizeBookPageView bookPageView;
    CoverPageView coverPageView;

    int[] pIds = new int[]{R.mipmap.test1, R.mipmap.test2, R.mipmap.test3};


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pageview);
//        ButterKnife.bind(this);

//        bookPageView = (OptimizeBookPageView) findViewById(R.id.bookPageView);
        coverPageView = (CoverPageView) findViewById(R.id.view_cover_page);
        coverPageView.setPageFactory(new PicturesPageFactory(this, pIds));
    }

}
