package com.dai.utils.view.circlebarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dai.utils.util.Constant;

/**
 * Created by dai on 2018/1/10.
 */

public class ProgressView extends View {

    private Paint paint;
    private int defaultSize;//自定义View默认的宽高
    private float barWidth = 5;//圆弧进度条宽度

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        System.out.println("ProgressView.ProgressView1");
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);//只描边，不填充
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        defaultSize = dip2px(context, Constant.WIDTH);
    }

    private int width;
    private int height;
    private int min;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = measureSize(defaultSize, heightMeasureSpec);
        width = measureSize(defaultSize, widthMeasureSpec);
        min = Math.min(width, height);// 获取View最短边的长度
        setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(-30, min / 2, height / 2);
        for (int i = 0; i < 12; i++) {
            paint.setColor(colors[i]);
            canvas.drawLine(min / 2, height / 2 + 20, min / 2, height / 2 + 40, paint);
            canvas.rotate(-30, min / 2, height / 2);
        }
        changeColors();
        invalidate();
    }

    int[] colors = {-1, -657931, -1315861, -1973791, -2631721, -3289651, -3881788, -4539718, -5197648, -5855578, -6513508, -7829368};

    private void changeColors() {
        int end = colors[colors.length - 1];
        int temp = 0;
        for (int i = colors.length - 1; i > 0; i--) {
            colors[i] = colors[i - 1];
            if (i == 1) colors[0] = end;
        }
    }

}
