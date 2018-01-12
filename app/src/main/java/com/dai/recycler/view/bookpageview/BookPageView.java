package com.dai.recycler.view.bookpageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.Toast;

import com.dai.recycler.R;
import com.dai.recycler.util.Constant;

/**
 * Created by dai on 2017/12/19.
 */

public class BookPageView extends View {

    private String style;

    private Context context;
    public static final String STYLE_LEFT = "STYLE_LEFT";//点击左边区域
    public static final String STYLE_RIGHT = "STYLE_RIGHT";//点击右边区域
    public static final String STYLE_MIDDLE = "STYLE_MIDDLE";//点击中间区域


    public static final String STYLE_TOP_RIGHT = "STYLE_TOP_RIGHT";//f点在右上角
    public static final String STYLE_LOWER_RIGHT = "STYLE_LOWER_RIGHT";//f点在右下角

    //    private Paint pointPaint;//绘制各标识点的画笔
    private Paint bgPaint;//背景画笔

    private MyPoint a, f, g, e, h, c, j, b, k, d, i;

    private int defaultWidth;//默认宽度
    private int defaultHeight;//默认高度
    private int viewWidth;
    private int viewHeight;


    private Paint pathAPaint;//绘制A区域画笔
    private Path pathA;
    private Bitmap bitmap;//缓存bitmap
    private Canvas bitmapCanvas;

    private Paint pathCPaint;//绘制C区域画笔
    private Path pathC;

    private Paint pathBPaint;//绘制B区域画笔
    private Path pathB;

    private Paint textPaint;//绘制文字画笔
    private Rect mBound;

    private Scroller mScroller;

    private Paint pathCContentPaint;//绘制C区域内容画笔
    private float lPathAShadowDis;
    private float rPathAShadowDis;

    public BookPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;


        pathAPaint = new Paint();
        pathAPaint.setColor(context.getResources().getColor(R.color.color_green_00ae8c));
        pathAPaint.setAntiAlias(true);//设置抗锯齿
        pathA = new Path();

        pathBPaint = new Paint();
        pathBPaint.setColor(getResources().getColor(R.color.color_blue_0888ff));
        pathBPaint.setAntiAlias(true);//设置抗锯齿
//        pathBPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        pathB = new Path();

        pathCPaint = new Paint();
        pathCPaint.setColor(Color.YELLOW);
        pathCPaint.setAntiAlias(true);//设置抗锯齿
        pathCPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        pathC = new Path();
        pathCContentPaint = new Paint();
        pathCContentPaint.setColor(Color.YELLOW);
        pathCContentPaint.setAntiAlias(true);//设置抗锯齿

        init();
    }

    boolean isOneLine = true;//如果是一行为true
    int lineNum;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = measureSize(defaultHeight, heightMeasureSpec);
        int width = measureSize(defaultWidth, widthMeasureSpec);
        setMeasuredDimension(width, height);

        viewWidth = width;
        viewHeight = height;
        a.x = -1;
        a.y = -1;
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

    private void init() {

        mScroller = new Scroller(context, new LinearInterpolator());
        defaultWidth = Constant.WIDTH;
        defaultHeight = Constant.HEIGHT;
        System.out.println("defaultHeight = " + defaultHeight);
        System.out.println("defaultWidth = " + defaultWidth);

        a = new MyPoint();
        f = new MyPoint();
        g = new MyPoint();
        e = new MyPoint();
        h = new MyPoint();
        c = new MyPoint();
        j = new MyPoint();
        b = new MyPoint();
        k = new MyPoint();
        d = new MyPoint();
        i = new MyPoint();

//        pointPaint = new Paint();
//        pointPaint.setColor(Color.RED);
//        pointPaint.setTextSize(25);
        bgPaint = new Paint();
        bgPaint.setColor(context.getResources().getColor(R.color.color_green_00ae8c));

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setSubpixelText(true);//设置自像素。如果该项为true，将有助于文本在LCD屏幕上的显示效果。
        textPaint.setTextSize(50);


    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //为了看清楚点与View的位置关系绘制一个背景
//        canvas.drawRect(0,0,viewWidth,viewHeight,bgPaint);


        bitmap = Bitmap.createBitmap((int) viewWidth, (int) viewHeight, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);

        String string = context.getString(R.string.contentA);
        if (a.x == -1 && a.y == -1) {
//            bitmapCanvas.drawPath(getPathDefault(), pathAPaint);
            drawPathAContent(bitmapCanvas, getPathDefault(), pathAPaint);
        } else {
            if (f.x == viewWidth && f.y == 0) {
//                bitmapCanvas.drawPath(getPathAFromTopRight(), pathAPaint);

                drawPathAContent(bitmapCanvas, getPathAFromTopRight(), pathAPaint);
                bitmapCanvas.drawPath(getPathC(), pathCPaint);
                drawPathCContent(bitmapCanvas, getPathAFromTopRight(), pathCContentPaint);
                drawPathBContent(bitmapCanvas, getPathAFromTopRight(), pathBPaint);
            } else if (f.x == viewWidth && f.y == viewHeight) {
//                bitmapCanvas.drawPath(getPathAFromLowerRight(), pathAPaint);
                drawPathAContent(bitmapCanvas, getPathAFromLowerRight(), pathAPaint);

                bitmapCanvas.drawPath(getPathC(), pathCPaint);
                drawPathCContent(bitmapCanvas, getPathAFromLowerRight(), pathCContentPaint);
                drawPathBContent(bitmapCanvas, getPathAFromLowerRight(), pathBPaint);

            }
//            bitmapCanvas.drawPath(getPathC(), pathCPaint);
//            bitmapCanvas.drawPath(getPathB(), pathBPaint);
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }


    boolean isClicked = false;

    /**
     * 绘制A区域内容
     *
     * @param canvas
     * @param pathA
     * @param pathPaint
     */
    private void drawPathAContent(Canvas canvas, Path pathA, Paint pathPaint) {
        String text = context.getString(R.string.contentA);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        float textHeight = (-fontMetrics.ascent - fontMetrics.descent) / 2;

        String[] strings = context.getString(R.string.contentA).split(" ");

        int height = (int) textHeight * 3;
        int lineHeight;

        Bitmap contentBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
        Canvas contentCanvas = new Canvas(contentBitmap);

        //下面开始绘制区域内的内容...
        contentCanvas.drawPath(pathA, pathPaint);//绘制一个背景，用来区分各区域

        int total = 1;
        int lineHeightSum = 0;
        for (String string : strings) {
            lineHeight = 15;
            if ((total * height + lineHeightSum) > viewHeight) break;
            float textWidth = textPaint.measureText(string);
            if (viewWidth > textWidth) {
                contentCanvas.drawText(string, 0, height * total, textPaint);
                contentCanvas.translate(0, 15);
                lineHeightSum = lineHeightSum + lineHeight;
                total++;
                continue;
            }
            int start = 0;
            int end = (int) (string.length() * viewWidth / textWidth);
            int line = string.length() / end + 1;
            int temp = end;
            for (int j = 0; j < line; j++) {
                contentCanvas.drawText(string.substring(start, end), 0, height * total, textPaint);
                total++;
                start = end;
                end = end + temp;
                if (end > string.length()) end = string.length();
                lineHeight = j == line - 1 ? 15 : 5;
                lineHeightSum = lineHeightSum + lineHeight;
                contentCanvas.translate(0, lineHeight);
            }
        }

        canvas.save();
        canvas.clipPath(pathA, Region.Op.INTERSECT);//对绘制内容进行裁剪，取和A区域的交集
        canvas.drawBitmap(contentBitmap, 0, 0, null);
        if(STYLE_LEFT.equals(style) || STYLE_RIGHT.equals(style)){//左右水平翻页
            drawPathAHorizontalShadow(canvas,pathA);
        }else {//上下翻页
            drawPathALeftShadow(canvas,pathA);
            drawPathARightShadow(canvas,pathA);
        }
        canvas.restore();
    }

    /**
     * 绘制A区域左阴影
     *
     * @param canvas
     */
    private void drawPathALeftShadow(Canvas canvas, Path pathA) {
        canvas.restore();
        canvas.save();

        int deepColor = 0x33333333;
        int lightColor = 0x01333333;
        int[] gradientColors = {lightColor, deepColor};//渐变颜色数组

        int left;
        int right;
        int top = (int) e.y;
        int bottom = (int) (e.y + viewHeight);

        GradientDrawable gradientDrawable;
        if (STYLE_TOP_RIGHT.equals(style)) {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            left = (int) (e.x - lPathAShadowDis / 2);
            right = (int) (e.x);
        } else {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            left = (int) (e.x);
            right = (int) (e.x + lPathAShadowDis / 2);
        }

        //裁剪出我们需要的区域
        Path mPath = new Path();
        mPath.moveTo(a.x- Math.max(rPathAShadowDis, lPathAShadowDis) /2,a.y);
        mPath.lineTo(d.x,d.y);
        mPath.lineTo(e.x,e.y);
        mPath.lineTo(a.x,a.y);
        mPath.close();
        canvas.clipPath(pathA);
        canvas.clipPath(mPath, Region.Op.INTERSECT);

        gradientDrawable.setBounds(left, top, right, bottom);

        float mDegrees = (float) Math.toDegrees(Math.atan2(e.x - a.x, a.y - e.y));
        canvas.rotate(mDegrees, e.x, e.y);
        gradientDrawable.draw(canvas);
    }

    /**
     * 绘制A区域右阴影
     *
     * @param canvas
     */
    private void drawPathARightShadow(Canvas canvas, Path pathA) {
        canvas.restore();
        canvas.save();

        int deepColor = 0x33333333;
        int lightColor = 0x01333333;
        int[] gradientColors = {deepColor, lightColor, lightColor};//渐变颜色数组

        float viewDiagonalLength = (float) Math.hypot(viewWidth, viewHeight);//view对角线长度
        int left = (int) h.x;
        int right = (int) (h.x + viewDiagonalLength * 10);//需要足够长的长度
        int top;
        int bottom;

        GradientDrawable gradientDrawable;
        if (STYLE_TOP_RIGHT.equals(style)) {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            top = (int) (h.y - rPathAShadowDis / 2);
            bottom = (int) h.y;
        } else {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            top = (int) h.y;
            bottom = (int) (h.y + rPathAShadowDis / 2);
        }
        gradientDrawable.setBounds(left, top, right, bottom);

        //裁剪出我们需要的区域
        Path mPath = new Path();
        mPath.moveTo(a.x- Math.max(rPathAShadowDis, lPathAShadowDis) /2,a.y);
//        mPath.lineTo(i.x,i.y);
        mPath.lineTo(h.x,h.y);
        mPath.lineTo(a.x,a.y);
        mPath.close();
        canvas.clipPath(pathA);
        canvas.clipPath(mPath, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(a.y - h.y, a.x - h.x));
        canvas.rotate(mDegrees, h.x, h.y);
        gradientDrawable.draw(canvas);
    }

    /**
     * 绘制A区域水平翻页阴影
     * @param canvas
     */
    private void drawPathAHorizontalShadow(Canvas canvas, Path pathA){
        canvas.restore();
        canvas.save();

        int deepColor = 0x44333333;
        int lightColor = 0x01333333;
        int[] gradientColors = {lightColor,deepColor};//渐变颜色数组

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        int maxShadowWidth = 30;//阴影矩形最大的宽度
        int left = (int) (a.x - Math.min(maxShadowWidth,(rPathAShadowDis/2)));
        int right = (int) (a.x);
        int top = 0;
        int bottom = viewHeight;
        gradientDrawable.setBounds(left,top,right,bottom);

        canvas.clipPath(pathA, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(f.x-a.x,f.y-h.y));
        canvas.rotate(mDegrees, a.x, a.y);
        gradientDrawable.draw(canvas);
    }

    /**
     * 绘制B区域内容
     *
     * @param canvas
     * @param pathPaint
     * @param pathA
     */
    private void drawPathBContent(Canvas canvas, Path pathA, Paint pathPaint) {
        Bitmap contentBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
        Canvas contentCanvas = new Canvas(contentBitmap);

        //下面开始绘制区域内的内容...
        contentCanvas.drawPath(getPathB(), pathPaint);
        contentCanvas.drawText("这是在B区域的内容...BBBB", viewWidth - 260, viewHeight - 100, textPaint);

        //结束绘制区域内的内容...

        canvas.save();
        canvas.clipPath(pathA);//裁剪出A区域
        canvas.clipPath(getPathC(), Region.Op.UNION);//裁剪出A和C区域的全集
        canvas.clipPath(getPathB(), Region.Op.REVERSE_DIFFERENCE);//裁剪出B区域中不同于与AC区域的部分
        canvas.drawBitmap(contentBitmap, 0, 0, null);
        //为了更好地观察在不裁剪时阴影区域的范围，重置一下画布
//        canvas.restore();
//        canvas.save();

        drawPathBShadow(canvas);//调用阴影绘制方法
        canvas.restore();
    }

    /**
     * 绘制B区域阴影，阴影左深右浅
     *
     * @param canvas
     */
    private void drawPathBShadow(Canvas canvas) {
        int deepColor = 0xff111111;//为了让效果更明显使用此颜色代码，具体可根据实际情况调整
//        int deepColor = 0x55111111;
        int lightColor = 0x00111111;
        int[] gradientColors = new int[]{deepColor, lightColor};//渐变颜色数组

        int deepOffset = 0;//深色端的偏移值
        int lightOffset = 0;//浅色端的偏移值
        float aTof = (float) Math.hypot((a.x - f.x), (a.y - f.y));//a到f的距离
        float viewDiagonalLength = (float) Math.hypot(viewWidth, viewHeight);//对角线长度

        int left;
        int right;
        int top = (int) c.y;
        int bottom = (int) (viewDiagonalLength + c.y);
        GradientDrawable gradientDrawable;
        if (style.equals(STYLE_TOP_RIGHT)) {//f点在右上角
            //从左向右线性渐变
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);//线性渐变

            left = (int) (c.x - deepOffset);//c点位于左上角
            right = (int) (c.x + aTof / 4 + lightOffset);
        } else {
            //从右向左线性渐变
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            left = (int) (c.x - aTof / 4 - lightOffset);//c点位于左下角
            right = (int) (c.x + deepOffset);
        }
        gradientDrawable.setBounds(left, top, right, bottom);//设置阴影矩形

        float rotateDegrees = (float) Math.toDegrees(Math.atan2(e.x - f.x, h.y - f.y));//旋转角度
        canvas.rotate(rotateDegrees, c.x, c.y);//以c为中心点旋转
        gradientDrawable.draw(canvas);
    }


    /**
     * 绘制C区域内容
     *
     * @param canvas
     * @param pathA
     * @param pathPaint
     */
    private void drawPathCContent(Canvas canvas, Path pathA, Paint pathPaint) {
        Bitmap contentBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
        Canvas contentCanvas = new Canvas(contentBitmap);

        String text = context.getString(R.string.contentA);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        float textHeight = (-fontMetrics.ascent - fontMetrics.descent) / 2;

        String[] strings = context.getString(R.string.contentA).split(" ");

        int height = (int) textHeight * 3;
        int lineHeight;


        //下面开始绘制区域内的内容...
        contentCanvas.drawPath(getPathB(), pathPaint);//绘制一个背景，path用B的就行

        int total = 1;
        int lineHeightSum = 0;
        for (String string : strings) {
            lineHeight = 15;
            if ((total * height + lineHeightSum) > viewHeight) break;
            float textWidth = textPaint.measureText(string);
            if (viewWidth > textWidth) {
                contentCanvas.drawText(string, 0, height * total, textPaint);
                contentCanvas.translate(0, 15);
                lineHeightSum = lineHeightSum + lineHeight;
                total++;
                continue;
            }
            int start = 0;
            int end = (int) (string.length() * viewWidth / textWidth);
            int line = string.length() / end + 1;
            int temp = end;
            for (int j = 0; j < line; j++) {
                contentCanvas.drawText(string.substring(start, end), 0, height * total, textPaint);
                total++;
                start = end;
                end = end + temp;
                if (end > string.length()) end = string.length();
                lineHeight = j == line - 1 ? 15 : 5;
                lineHeightSum = lineHeightSum + lineHeight;
                contentCanvas.translate(0, lineHeight);
            }
        }


        canvas.save();
        canvas.clipPath(pathA);
        canvas.clipPath(getPathC(), Region.Op.REVERSE_DIFFERENCE);//裁剪出C区域不同于A区域的部分
//        canvas.clipPath(getPathC(),Region.Op.UNION);//裁剪出A和C区域的全集
//        canvas.clipPath(getPathC(), Region.Op.INTERSECT);//取与C区域的交集

        float eh = (float) Math.hypot(f.x - e.x, h.y - f.y);
        float sin0 = (f.x - e.x) / eh;
        float cos0 = (h.y - f.y) / eh;
        //设置翻转和旋转矩阵
        float[] mMatrixArray = {0, 0, 0, 0, 0, 0, 0, 0, 1.0f};
        mMatrixArray[0] = -(1 - 2 * sin0 * sin0);
        mMatrixArray[1] = 2 * sin0 * cos0;
        mMatrixArray[3] = 2 * sin0 * cos0;
        mMatrixArray[4] = 1 - 2 * sin0 * sin0;

        Matrix mMatrix = new Matrix();
        mMatrix.reset();
        mMatrix.setValues(mMatrixArray);//翻转和旋转
        mMatrix.preTranslate(-e.x, -e.y);//沿当前XY轴负方向位移得到 矩形A₃B₃C₃D₃
        mMatrix.postTranslate(e.x, e.y);//沿原XY轴方向位移得到 矩形A4 B4 C4 D4

        canvas.drawBitmap(contentBitmap, mMatrix, null);

        drawPathCShadow(canvas);//调用绘制阴影方法
        canvas.restore();
    }

    /**
     * 绘制C区域阴影，阴影左浅右深
     *
     * @param canvas
     */
    private void drawPathCShadow(Canvas canvas) {
        int deepColor = 0xff111111;//为了让效果更明显使用此颜色代码，具体可根据实际情况调整
//        int deepColor = 0x55333333;
        int lightColor = 0x00333333;
        int[] gradientColors = {lightColor, deepColor};//渐变颜色数组

        int deepOffset = 1;//深色端的偏移值
        int lightOffset = -30;//浅色端的偏移值
        float viewDiagonalLength = (float) Math.hypot(viewWidth, viewHeight);//view对角线长度
        int midpoint_ce = (int) (c.x + e.x) / 2;//ce中点
        int midpoint_jh = (int) (j.y + h.y) / 2;//jh中点
        float minDisToControlPoint = Math.min(Math.abs(midpoint_ce - e.x), Math.abs(midpoint_jh - h.y));//中点到控制点的最小值

        int left;
        int right;
        int top = (int) c.y;
        int bottom = (int) (viewDiagonalLength + c.y);
        GradientDrawable gradientDrawable;
        if (style.equals(STYLE_TOP_RIGHT)) {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            left = (int) (c.x - lightOffset);
            right = (int) (c.x + minDisToControlPoint + deepOffset);
        } else {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            left = (int) (c.x - minDisToControlPoint - deepOffset);
            right = (int) (c.x + lightOffset);
        }
        gradientDrawable.setBounds(left, top, right, bottom);

        float mDegrees = (float) Math.toDegrees(Math.atan2(e.x - f.x, h.y - f.y));
        canvas.rotate(mDegrees, c.x, c.y);
        gradientDrawable.draw(canvas);
    }

    @Override
    public void computeScroll() {
        //TODO 不滑动直接点击 view不会刷新啊
        if (mScroller.computeScrollOffset()) {
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();
            if (STYLE_TOP_RIGHT.equals(style)) {
                setTouchPoint(x, y, STYLE_TOP_RIGHT);
            } else {
                setTouchPoint(x, y, STYLE_LOWER_RIGHT);
            }
            if (mScroller.getFinalX() == x && mScroller.getFinalY() == y) {
                setDefaultPath();//重置默认界面
            }
        }
    }

    /**
     * 取消翻页动画,计算滑动位置与时间
     */
    public void startCancelAnim() {
        int dx, dy;
        //让a滑动到f点所在位置，留出1像素是为了防止当a和f重叠时出现View闪烁的情况
        if (style.equals(STYLE_TOP_RIGHT)) {
            dx = (int) (viewWidth - 1 - a.x);
            dy = (int) (1 - a.y);
        } else {
            dx = (int) (viewWidth - 1 - a.x);
            dy = (int) (viewHeight - 1 - a.y);
        }

        mScroller.startScroll((int) a.x, (int) a.y, dx, dy, 400);


    }

    /**
     * 设置触摸点
     *
     * @param x
     * @param y
     * @param style
     */
    public void setTouchPoint(float x, float y, String style) {
//        switch (style) {
//            case STYLE_TOP_RIGHT:
//                f.x = viewWidth;
//                f.y = 0;
//                break;
//            case STYLE_LOWER_RIGHT:
//                f.x = viewWidth;
//                f.y = viewHeight;
//                break;
//            default:
//                break;
//        }
//        a.x = x;
//        a.y = y;
//        calcPointsXY(a, f);//        MyPoint touchPoint = new MyPoint(x, y);
//        //如果大于0则设置a点坐标重新计算各标识点位置，否则a点坐标不变
//        if (calcPointCX(touchPoint, f) < 0) {
//            calcPointAByTouchPoint();
//            calcPointsXY(a, f);
//        }
//        postInvalidate();
        MyPoint touchPoint = new MyPoint();
        a.x = x;
        a.y = y;
        this.style = style;
        switch (style) {
            case STYLE_TOP_RIGHT:
                f.x = viewWidth;
                f.y = 0;
                calcPointsXY(a, f);
                touchPoint = new MyPoint(x, y);
                if (calcPointCX(touchPoint, f) < 0) {//如果c点x坐标小于0则重新测量a点坐标
                    calcPointAByTouchPoint();
                    calcPointsXY(a, f);
                }
                postInvalidate();
                break;
            case STYLE_LEFT:
            case STYLE_RIGHT:
                a.y = viewHeight - 1;
                f.x = viewWidth;
                f.y = viewHeight;
                calcPointsXY(a, f);
                postInvalidate();
                break;
            case STYLE_LOWER_RIGHT:
                f.x = viewWidth;
                f.y = viewHeight;
                calcPointsXY(a, f);
                touchPoint = new MyPoint(x, y);
                if (calcPointCX(touchPoint, f) < 0) {//如果c点x坐标小于0则重新测量a点坐标
                    calcPointAByTouchPoint();
                    calcPointsXY(a, f);
                }
                postInvalidate();
                break;
            default:
                break;
        }

    }

    /**
     * 回到默认状态
     */
    public void setDefaultPath() {
        a.x = -1;
        a.y = -1;
        invalidate();
    }

    /**
     * 绘制默认的界面
     *
     * @return
     */
    private Path getPathDefault() {
        pathA.reset();
        pathA.lineTo(0, viewHeight);
        pathA.lineTo(viewWidth, viewHeight);
        pathA.lineTo(viewWidth, 0);
        pathA.close();
        return pathA;
    }

    /**
     * 获取f点在右下角的pathA
     *
     * @return
     */
    private Path getPathAFromLowerRight() {
        pathA.reset();
        pathA.lineTo(0, viewHeight);//移动到左下角
        pathA.lineTo(c.x, c.y);//移动到c点
        pathA.quadTo(e.x, e.y, b.x, b.y);//从c到b画贝塞尔曲线，控制点为e
        pathA.lineTo(a.x, a.y);//移动到a点
        pathA.lineTo(k.x, k.y);//移动到k点
        pathA.quadTo(h.x, h.y, j.x, j.y);//从k到j画贝塞尔曲线，控制点为h
        pathA.lineTo(viewWidth, 0);//移动到右上角
        pathA.close();//闭合区域
        return pathA;
    }

    public float getViewWidth() {
        return viewWidth;
    }

    public float getViewHeight() {
        return viewHeight;
    }

    /**
     * 获取f点在右上角的pathA
     */
    private Path getPathAFromTopRight() {
        pathA.reset();
        pathA.lineTo(c.x, c.y);//移动到c点
        pathA.quadTo(e.x, e.y, b.x, b.y);//从c到b画贝塞尔曲线，控制点为e
        pathA.lineTo(a.x, a.y);//移动到a点
        pathA.lineTo(k.x, k.y);//移动到k点
        pathA.quadTo(h.x, h.y, j.x, j.y);//从k到j画贝塞尔曲线，控制点为h
        pathA.lineTo(viewWidth, viewHeight);//移动到右下角
        pathA.lineTo(0, viewHeight);//移动到左下角
        pathA.close();
        return pathA;
    }

    /**
     * 绘制区域C
     */
    private Path getPathC() {
        pathC.reset();
        pathC.moveTo(i.x, i.y);//移动到i点
        pathC.lineTo(d.x, d.y);//移动到d点
        pathC.lineTo(b.x, b.y);//移动到b点
        pathC.lineTo(a.x, a.y);//移动到a点
        pathC.lineTo(k.x, k.y);//移动到k点
        pathC.close();//闭合区域
        return pathC;
    }

    /**
     * 绘制区域B
     *
     * @return
     */
    private Path getPathB() {
        pathB.reset();
        pathB.lineTo(0, viewHeight);//移动到左下角
        pathB.lineTo(viewWidth, viewHeight);//移动到右下角
        pathB.lineTo(viewWidth, 0);//移动到右上角
        pathB.close();//闭合区域
        return pathB;
    }

    /**
     * 如果c点x坐标小于0,根据触摸点重新测量a点坐标
     */
    private void calcPointAByTouchPoint() {
        float w0 = viewWidth - c.x;
        float w1 = Math.abs(f.x - a.x);
        float w2 = viewWidth * w1 / w0;
        a.x = Math.abs(f.x - w2);

        float h1 = Math.abs(f.y - a.y);
        float h2 = w2 * h1 / w1;
        a.y = Math.abs(f.y - h2);
    }

    /**
     * 计算C点的X值
     *
     * @param a
     * @param f
     * @return
     */
    private float calcPointCX(MyPoint a, MyPoint f) {
        MyPoint g, e;
        g = new MyPoint();
        e = new MyPoint();
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        return e.x - (f.x - e.x) / 2;
    }

    /**
     * 计算各点坐标
     *
     * @param a
     * @param f
     */
    private void calcPointsXY(MyPoint a, MyPoint f) {
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        h.x = f.x;
        h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y);

        c.x = e.x - (f.x - e.x) / 2;
        c.y = f.y;

        j.x = f.x;
        j.y = h.y - (f.y - h.y) / 2;

        b = getIntersectionPoint(a, e, c, j);
        k = getIntersectionPoint(a, h, c, j);

        d.x = (c.x + 2 * e.x + b.x) / 4;
        d.y = (2 * e.y + c.y + b.y) / 4;

        i.x = (j.x + 2 * h.x + k.x) / 4;
        i.y = (2 * h.y + j.y + k.y) / 4;
        //计算d点到直线ae的距离
        float lA = a.y - e.y;
        float lB = e.x - a.x;
        float lC = a.x * e.y - e.x * a.y;
        lPathAShadowDis = Math.abs((lA * d.x + lB * d.y + lC) / (float) Math.hypot(lA, lB));
        //计算i点到ah的距离
        float rA = a.y - h.y;
        float rB = h.x - a.x;
        float rC = a.x * h.y - h.x * a.y;
        rPathAShadowDis = Math.abs((rA * i.x + rB * i.y + rC) / (float) Math.hypot(rA, rB));
    }

    /**
     * 计算两线段相交点坐标
     *
     * @param lineOne_My_pointOne
     * @param lineOne_My_pointTwo
     * @param lineTwo_My_pointOne
     * @param lineTwo_My_pointTwo
     * @return 返回该点
     */
    private MyPoint getIntersectionPoint(MyPoint lineOne_My_pointOne, MyPoint lineOne_My_pointTwo, MyPoint lineTwo_My_pointOne, MyPoint lineTwo_My_pointTwo) {
        float x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = lineOne_My_pointOne.x;
        y1 = lineOne_My_pointOne.y;
        x2 = lineOne_My_pointTwo.x;
        y2 = lineOne_My_pointTwo.y;
        x3 = lineTwo_My_pointOne.x;
        y3 = lineTwo_My_pointOne.y;
        x4 = lineTwo_My_pointTwo.x;
        y4 = lineTwo_My_pointTwo.y;

        float pointX = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));
        float pointY = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
                / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));

        return new MyPoint(pointX, pointY);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClicked = true;
                System.out.println("BookPageView.onTouchEvent ACTION_DOWN");
                float x = event.getX();
                float y = event.getY();
                float width = getViewWidth();
                float height = getViewHeight();
                if (x <= width / 3) {//左
                    style = STYLE_LEFT;
                    Toast.makeText(context, "点击了左部", Toast.LENGTH_SHORT).show();
                    setTouchPoint(x, y, style);

                } else if (x > width / 3 && y <= height / 3) {//上
                    style = STYLE_TOP_RIGHT;
                    Toast.makeText(context, "点击了上部", Toast.LENGTH_SHORT).show();
                    setTouchPoint(x, y, style);

                } else if (x > width * 2 / 3 && y > height / 3 && y <= height * 2 / 3) {//右
                    style = STYLE_RIGHT;
                    Toast.makeText(context, "点击了右部", Toast.LENGTH_SHORT).show();
                    setTouchPoint(x, y, style);

                } else if (x > width / 3 && y > height * 2 / 3) {//下
                    style = STYLE_LOWER_RIGHT;
                    Toast.makeText(context, "点击了下部", Toast.LENGTH_SHORT).show();
                    setTouchPoint(x, y, style);

                } else if (x > width / 3 && x < width * 2 / 3 && y > height / 3 && y < height * 2 / 3) {//中
                    style = STYLE_MIDDLE;
                    Toast.makeText(context, "点击了中部", Toast.LENGTH_SHORT).show();
                    setTouchPoint(x, y, STYLE_MIDDLE);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                setTouchPoint(event.getX(), event.getY(), style);
                break;
            case MotionEvent.ACTION_UP:

//                setDefaultPath();
                startCancelAnim();
                isClicked = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
