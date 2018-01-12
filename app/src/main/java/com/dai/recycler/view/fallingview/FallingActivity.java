package com.dai.recycler.view.fallingview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dai.recycler.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dai on 2018/1/12.
 */

public class FallingActivity extends AppCompatActivity {
    @BindView(R.id.falling_view)
    FallingView fallingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall);
        ButterKnife.bind(this);
    }
}
