package com.dai.recycler.view.fallingview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private Paint snowPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall);
        ButterKnife.bind(this);
        snowPaint = new Paint();
        snowPaint.setColor(Color.WHITE);
        snowPaint.setStyle(Paint.Style.FILL);
        bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmapCanvas.drawCircle(25, 25, 15, snowPaint);

        //初始化一个雪球样式的fallObject
        FallObject.Builder builder = new FallObject.Builder(getResources().getDrawable(R.mipmap.ic_snow));
        FallObject fallObject = builder
                .setSpeed(10,true)
                .setSize(30,30,true)
                .setWind(12,false,true)
                .build();

        fallingView.addFallObject(fallObject, 200);//添加50个雪球对象
    }
}
