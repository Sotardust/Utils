package com.dai.recycler.view.waveprogressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.dai.recycler.R;
import com.dai.recycler.view.circlebarview.util.DpOrPxUtils;

/**
 * Created by dai on 2018/1/11.
 */

public class WaveProgressView extends View {

    private Paint wavePaint;//绘制波浪画笔
    private Path wavePath;//绘制波浪Path

    private float waveWidth;//波浪宽度
    private float waveHeight;//波浪高度

    private int waveNum;//波浪组的数量（一次起伏为一组）
    private int defaultSize;//自定义View默认的宽高
    private int maxHeight;//为了看到波浪效果，给定一个比填充物稍高的高度

    private int viewSize;//重新测量后View实际的宽高

    private WaveProgressAnim waveProgressAnim;
    private float percent;//进度条占比
    private float progressNum;//可以更新的进度条数值
    private float maxNum;//进度条最大值

    private float waveMovingDistance;//波浪平移的距离

    private Paint circlePaint;//圆形进度框画笔

    private Bitmap bitmap;//缓存bitmap
    private Canvas bitmapCanvas;

    private int waveColor;//波浪颜色
    private int bgColor;//背景进度框颜色

    private int secondWaveColor;//第二层波浪颜色
    private boolean isDrawSecondWave;//是否绘制第二层波浪
    private Paint secondWavePaint;

    public WaveProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveProgressView);
        waveWidth = typedArray.getDimension(R.styleable.WaveProgressView_wave_width, DpOrPxUtils.dip2px(context, 25));
        waveHeight = typedArray.getDimension(R.styleable.WaveProgressView_wave_height, DpOrPxUtils.dip2px(context, 5));
        waveColor = typedArray.getColor(R.styleable.WaveProgressView_wave_color, Color.GREEN);
        bgColor = typedArray.getColor(R.styleable.WaveProgressView_bg_color, Color.GRAY);
        secondWaveColor = typedArray.getColor(R.styleable.WaveProgressView_second_wave_color,getResources().getColor(R.color.light));
        typedArray.recycle();


        secondWavePaint = new Paint();
        secondWavePaint.setColor(secondWaveColor);
        secondWavePaint.setAntiAlias(true);//设置抗锯齿
        //因为要覆盖在第一层波浪上，且要让半透明生效，所以选SRC_ATOP模式
        secondWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        isDrawSecondWave = false;

        maxHeight = DpOrPxUtils.dip2px(context, 250);

        percent = 0;
        progressNum = 0;
        maxNum = 100;
        waveMovingDistance = 0;
        waveProgressAnim = new WaveProgressAnim();

        wavePath = new Path();

        wavePaint = new Paint();
        wavePaint.setColor(Color.GREEN);
        wavePaint.setAntiAlias(true);//设置抗锯齿
        waveProgressAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

                System.out.println("WaveProgressView.onAnimationRepeat");
                if (percent == progressNum / maxNum) {
                    waveProgressAnim.setDuration(3000);
                }
            }
        });

        wavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//根据绘制顺序的不同选择相应的模式即可
        wavePaint.setColor(waveColor);
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);//设置抗锯齿


        circlePaint.setColor(bgColor);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //这里用到了缓存技术
        bitmap = Bitmap.createBitmap(viewSize, viewSize, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);

        bitmapCanvas.drawCircle(viewSize / 2, viewSize / 2, viewSize / 2, circlePaint);
        bitmapCanvas.drawPath(getWavePath(), wavePaint);
        if(isDrawSecondWave){
            bitmapCanvas.drawPath(getSecondWavePath(),secondWavePaint);
        }
        canvas.drawBitmap(bitmap, 0, 0, null);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureSize(defaultSize, widthMeasureSpec);
        int height = measureSize(defaultSize, heightMeasureSpec);
        int min = Math.min(width, height);// 获取View最短边的长度
        setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形
        viewSize = min;

        waveNum = (int) Math.ceil(Double.parseDouble(String.valueOf(viewSize / waveWidth / 3)));

        System.out.println("waveNum = " + waveNum);
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    private Path getWavePath() {
        wavePath.reset();
        //移动到右上方，也就是p0点
        wavePath.moveTo(viewSize, (1 - percent) * viewSize);//让p0p1的长度随percent的增加而增加（注意这里y轴方向默认是向下的）
        //移动到右下方，也就是p1点
        wavePath.lineTo(viewSize, viewSize);
        //移动到左下边，也就是p2点
        wavePath.lineTo(0, viewSize);
        //移动到左上方，也就是p3点
//        wavePath.lineTo(0, (1-percent)*viewSize);//让p3p2的长度随percent的增加而增加（注意这里y轴方向默认是向下的）
        //移动到左上方，也就是p3点（x轴默认方向是向右的，我们要向左平移，因此设为负值）
        //wavePath.lineTo(0, (1-percent)*viewSize);
        wavePath.lineTo(-waveMovingDistance, (1 - percent) * viewSize);
        float changeWaveHeight = waveHeight;
        if (onAnimationListener != null) {
            changeWaveHeight =
                    onAnimationListener.howToChangeWaveHeight(percent, waveHeight) == 0 && percent < 1 ? waveHeight : onAnimationListener.howToChangeWaveHeight(percent, waveHeight);
        }

        //从p3开始向p0方向绘制波浪曲线
        for (int i = 0; i < waveNum * 2; i++) {
            wavePath.rQuadTo(waveWidth / 2, changeWaveHeight, waveWidth, 0);
            wavePath.rQuadTo(waveWidth / 2, -changeWaveHeight, waveWidth, 0);
        }
        //将path封闭起来
        wavePath.close();
        return wavePath;
    }

    private TextView textView;
    private OnAnimationListener onAnimationListener;

    public class WaveProgressAnim extends Animation {
        //省略部分代码...
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (percent < progressNum / maxNum) {
                percent = interpolatedTime * progressNum / maxNum;
                if (textView != null && onAnimationListener != null) {
                    textView.setText(onAnimationListener.howToChangeText(interpolatedTime, progressNum, maxNum));
                }
            }
            waveMovingDistance = interpolatedTime * waveNum * waveWidth * 2;
            postInvalidate();
        }
    }

    /**
     * 设置显示文字的TextView
     *
     * @param textView
     */
    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public interface OnAnimationListener {
        /**
         * 如何处理要显示的文字内容
         *
         * @param interpolatedTime 从0渐变成1,到1时结束动画
         * @param updateNum        进度条数值
         * @param maxNum           进度条最大值
         * @return
         */
        String howToChangeText(float interpolatedTime, float updateNum, float maxNum);

        /**
         * 如何处理波浪高度
         *
         * @param percent    进度占比
         * @param waveHeight 波浪高度
         * @return
         */
        float howToChangeWaveHeight(float percent, float waveHeight);
    }

    public void setOnAnimationListener(OnAnimationListener onAnimationListener) {
        this.onAnimationListener = onAnimationListener;
    }


    /**
     * 设置进度条数值
     *
     * @param progressNum 进度条数值
     * @param time        动画持续时间
     */
    public void setProgressNum(float progressNum, int time) {
        this.progressNum = progressNum;
        percent = 0;
        waveProgressAnim.setDuration(time);
        this.startAnimation(waveProgressAnim);
        waveProgressAnim.setRepeatCount(Animation.INFINITE);//让动画无限循环
        waveProgressAnim.setInterpolator(new LinearInterpolator());//让动画匀速播放，不然会出现波浪平移停顿的现象
    }
    private Path getSecondWavePath(){
        float changeWaveHeight = waveHeight;
        if(onAnimationListener!=null){
            changeWaveHeight =
                    onAnimationListener.howToChangeWaveHeight(percent,waveHeight) == 0 && percent < 1
                            ?waveHeight
                            :onAnimationListener.howToChangeWaveHeight(percent,waveHeight);
        }

        wavePath.reset();
        //移动到左上方，也就是p3点
        wavePath.moveTo(0, (1-percent)*viewSize);
        //移动到左下方，也就是p2点
        wavePath.lineTo(0, viewSize);
        //移动到右下方，也就是p1点
        wavePath.lineTo(viewSize, viewSize);
        //移动到右上方，也就是p0点
        wavePath.lineTo(viewSize + waveMovingDistance, (1-percent)*viewSize);

        //从p0开始向p3方向绘制波浪曲线（注意绘制二阶贝塞尔曲线控制点和终点x坐标的正负值）
        for (int i=0;i<waveNum*2;i++){
            wavePath.rQuadTo(-waveWidth/2, changeWaveHeight, -waveWidth, 0);
            wavePath.rQuadTo(-waveWidth/2, -changeWaveHeight, -waveWidth, 0);
        }

        //将path封闭起来
        wavePath.close();
        return wavePath;
    }

    /**
     * 是否绘制第二层波浪
     * @param isDrawSecondWave
     */
    public void setDrawSecondWave(boolean isDrawSecondWave) {
        this.isDrawSecondWave = isDrawSecondWave;
    }

}
