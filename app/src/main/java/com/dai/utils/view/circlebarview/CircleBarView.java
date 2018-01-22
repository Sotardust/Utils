package com.dai.utils.view.circlebarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.dai.utils.R;
import com.dai.utils.view.circlebarview.util.DpOrPxUtils;
import com.dai.utils.view.util.ViewUtils;


/**
 * Created by dai on 2018/1/8.
 */

public class CircleBarView extends View {

    private Paint rPaint;//绘制矩形的画笔
    private Paint progressPaint;//绘制圆弧的画笔


    private CircleBarAnim anim;

    private Paint bgPaint;//绘制背景圆弧的画笔

    private float progressNum;//可以更新的进度条数值
    private float maxNum;//进度条最大值


    private float progressSweepAngle;//进度条圆弧扫过的角度

    private int progressColor;//进度条圆弧颜色
    private int bgColor;//背景圆弧颜色
    private float startAngle;//背景圆弧的起始角度
    private float sweepAngle;//背景圆弧扫过的角度
    private float barWidth;//圆弧进度条宽度

    private RectF mRectF;//绘制圆弧的矩形区域
    private int defaultSize;//自定义View默认的宽高
    float currentAngle;

    public CircleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleBarView);

        progressColor = typedArray.getColor(R.styleable.CircleBarView_progress_color, Color.GREEN);//默认为绿色
        bgColor = typedArray.getColor(R.styleable.CircleBarView_bgr_color, Color.GRAY);//默认为灰色
        startAngle = typedArray.getFloat(R.styleable.CircleBarView_start_angle, 0);//默认为0
        sweepAngle = typedArray.getFloat(R.styleable.CircleBarView_sweep_angle, 360);//默认为360
        barWidth = typedArray.getDimension(R.styleable.CircleBarView_bar_width, DpOrPxUtils.dip2px(context, 10));//默认为10dp
        typedArray.recycle();//typedArray用完之后需要回收，防止内存泄漏
        currentAngle = startAngle;
        defaultSize = DpOrPxUtils.dip2px(context, 100);
//        barWidth = DpOrPxUtils.dip2px(context,10);
        mRectF = new RectF();

        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.STROKE);//只描边，不填充
        progressPaint.setColor(progressColor);
        progressPaint.setAntiAlias(true);//设置抗锯齿
        progressPaint.setStrokeCap(Paint.Cap.ROUND); //设置线帽样式，取值有Cap.ROUND(圆形线帽)、Cap.SQUARE(方形线帽)、Paint.Cap.BUTT(无线帽)
        progressPaint.setStrokeWidth(3);//随便设置一个画笔宽度，看看效果就好，之后会通过attr自定义属性进行设置

        sweepGradient = new SweepGradient(0, 0, Color.WHITE, Color.RED);
        anim = new CircleBarAnim();
        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.STROKE);//只描边，不填充
        bgPaint.setStrokeCap(Paint.Cap.BUTT);
        bgPaint.setColor(bgColor);
        bgPaint.setAntiAlias(true);//设置抗锯齿
        bgPaint.setStrokeWidth(3);
        bgPaint.setShader(sweepGradient);

        maxNum = 100;//也是随便设

    }

    int width = 0;
    int height = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = ViewUtils.measureSize(defaultSize, heightMeasureSpec);
        width = ViewUtils.measureSize(defaultSize, widthMeasureSpec);
        int min = Math.min(width, height);// 获取View最短边的长度
        setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形

        if (min >= barWidth * 2) {//这里简单限制了圆弧的最大宽度
            mRectF.set(barWidth / 2, barWidth / 2, min - barWidth / 2, min - barWidth / 2);
        }

    }

    int total = 0;
    boolean isColor = false;
    SweepGradient sweepGradient;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        System.out.println("CircleBarView.onDraw");

        canvas.rotate(-30, width / 3, height / 3);
//        if (currentAngle >= 360f) {
//            currentAngle = currentAngle - 360f;
//        } else {
//            currentAngle = currentAngle + 2f;
//        }
//        canvas.
//        canvas.drawArc(mRectF, startAngle, sweepAngle, false, bgPaint);
//        canvas.drawArc(mRectF, 30, 60, false, progressPaint);
        for (int i = 0; i < 12; i++) {
            progressPaint.setColor(colors[i]);
            canvas.drawLine(width / 3, height / 3 + 20, width / 3, height / 3 + 50, progressPaint);
//            canvas.drawLine(width / 3, height / 3 + 20, width / 3, height / 3 + 50, progressPaint);
            canvas.rotate(-30, width / 3, height / 3);
        }

//        progressPaint.setColor(sweepGradient);

//        canvas.rotate(60,width/2,height/2);
//        if (anim.interpolatedTime == 1.0f) {
//            total++;
//            startAnimation(anim);
            changeColors();
//        }
        invalidate();
    }

    int[] colors = {-1,-657931,-1315861,-1973791,-2631721,-3289651,-3881788,-4539718,-5197648,-5855578,-6513508,-7829368};

    private TextView textView;
    private OnAnimationListener onAnimationListener;

    private void changeColors() {
        int end = colors[colors.length - 1];
        int temp = 0;
        for (int i = colors.length - 1; i > 0; i--) {
            colors[i] = colors[i - 1];
            if (i == 1) colors[0] = end;
        }
    }

    public void setOnAnimationListener(OnAnimationListener onAnimationListener) {
        this.onAnimationListener = onAnimationListener;
    }

    public class CircleBarAnim extends Animation {

        float interpolatedTime = 0;

        public CircleBarAnim() {
        }


        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            this.interpolatedTime = interpolatedTime;
            progressSweepAngle = interpolatedTime * sweepAngle * progressNum / maxNum;//这里计算进度条的比例
//            if (textView != null && onAnimationListener != null) {
//                textView.setText(onAnimationListener.howToChangeText(interpolatedTime, progressNum, maxNum));
            onAnimationListener.howTiChangeProgressColor(progressPaint, interpolatedTime, progressNum, maxNum);
//            }
            postInvalidate();
        }
    }

    //写个方法给外部调用，用来设置动画时间
    public void setProgressNum(float progressNum, int time) {
        this.progressNum = progressNum;
        anim.setDuration(time);
        this.startAnimation(anim);
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
         * @param progressNum      进度条数值
         * @param maxNum           进度条最大值
         * @return
         */
        String howToChangeText(float interpolatedTime, float progressNum, float maxNum);

        /**
         * 如何处理进度条的颜色
         *
         * @param paint            进度条画笔
         * @param interpolatedTime 从0渐变成1,到1时结束动画
         * @param progressNum      进度条数值
         * @param maxNum           进度条最大值
         */
        void howTiChangeProgressColor(Paint paint, float interpolatedTime, float progressNum, float maxNum);

    }
}
