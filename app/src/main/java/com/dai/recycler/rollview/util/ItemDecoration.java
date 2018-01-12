package com.dai.recycler.rollview.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by dai on 2017/9/27.
 */

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private Paint paint;
    private int headHeight = 100;
    private int divideHeight = 3;

    public void setHead(ArrayList<String> head) {
        this.head = head;
    }

    private ArrayList<String> head = new ArrayList<>();

    public ItemDecoration() {
        initPaint();
        paint.setColor(Color.RED);
    }

    private void initPaint() {
        if (paint == null) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        if (needTitle(position)) {
            outRect.top = headHeight;
        } else {
            outRect.top = divideHeight;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int position = ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
            int bottom = child.getTop();
            if (needTitle(position)) {
                //绘制悬浮栏
                int top = child.getTop() - headHeight;
                paint.setColor(Color.RED);
                c.drawRect(left, top, right, bottom, paint);
                drawText(c, head.get(position), left + headHeight * 0.25f, bottom - headHeight * 0.35f);
            } else {
                // 绘制分割线
                int top = child.getTop() - divideHeight;
                paint.setColor(Color.GRAY);
                c.drawRect(left, top, right, bottom, paint);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        int top = parent.getPaddingTop();
        int bottom = top + headHeight;
        paint.setColor(Color.RED);
        int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        c.drawRect(left, top, right, bottom, paint);
        drawText(c, head.get(position), left + headHeight * 0.25f, bottom - headHeight * 0.35f);
    }


    private void drawText(Canvas c, String itemViewTitle, float x, float y) {
        if (!TextUtils.isEmpty(itemViewTitle)) {
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            c.drawText(itemViewTitle, x, y, paint);
        }
    }

    private boolean needTitle(int position) {
        return true;
    }


}
