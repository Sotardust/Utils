package com.dai.recycler.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.dai.recycler.R;

import java.util.ArrayList;

/**
 * Created by dai on 2017/9/11.
 */

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private Paint paint;
    private Builder builder;

    ItemDecoration(Builder builder) {
        this.builder = builder;
        initPaint();
        paint.setColor(Color.RED);
    }

    static class Builder {
        private boolean isHeadView = false;
        private boolean isFootView = false;
        private int divideHeight = 1;
        private int headHeight = 45;
        private int textSize = 20;
        private Context context;

        private ArrayList<String> heads = new ArrayList<>();
        @ColorRes
        private int divideColor = R.color.grey;
        @ColorRes
        private int headBackGround = R.color.grey;
        @ColorRes
        private int textColor = R.color.black;

        Builder(Context context) {
            this.context = context;
        }


        Builder setDivideHeight(int divideHeight) {
            this.divideHeight = divideHeight;
            return this;
        }

        public Builder setHeadHeight(int headHeight) {
            this.headHeight = headHeight;
            return this;
        }

        Builder setDivideColor(@ColorRes int divideColor) {
            this.divideColor = divideColor;
            return this;
        }

        Builder setHeadBackGround(@ColorRes int headBackGround) {
            this.headBackGround = headBackGround;
            return this;
        }

        Builder setTextColor(@ColorRes int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        Builder setHeads(ArrayList<String> heads) {
            this.heads = heads;
            return this;
        }

        Builder isHeaderView() {
            this.isHeadView = true;
            return this;
        }

        Builder isFooterView() {
            this.isFootView = true;
            return this;
        }

        ItemDecoration build() {
            return new ItemDecoration(this);
        }
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
            outRect.top = builder.headHeight;
        } else {
            outRect.top = builder.divideHeight;
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
            if (builder.isHeadView && position == 0) continue;
            int bottom = child.getTop();
            if (needTitle(position)) {
                //绘制悬浮栏
                int pos = builder.isHeadView ? position - 1 : position;
                int top = child.getTop() - builder.headHeight;
                paint.setColor(builder.context.getResources().getColor(builder.headBackGround));
                c.drawRect(left, top, right, bottom, paint);
                drawText(c, builder.heads.get(pos), left + builder.headHeight * 0.25f, bottom - builder.headHeight * 0.35f);
            } else {
                // 绘制分割线
                int top = child.getTop() - builder.divideHeight;
                paint.setColor(builder.context.getResources().getColor(builder.divideColor));
                c.drawRect(left, top, right, bottom, paint);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (builder.heads.size() == 0) {
            return ;
        }
        int left = parent.getPaddingLeft();
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        int top = parent.getPaddingTop();
        int bottom = top + builder.headHeight;
        paint.setColor(builder.context.getResources().getColor(builder.headBackGround));
        int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        if (!(builder.isHeadView && position == 0)) {
            c.drawRect(left, top, right, bottom, paint);
            int pos = builder.isHeadView ? position - 1 : position;
            drawText(c, builder.heads.get(pos), left + builder.headHeight * 0.25f, bottom - builder.headHeight * 0.35f);
        }

    }

    private void drawText(Canvas c, String itemViewTitle, float x, float y) {
        if (!TextUtils.isEmpty(itemViewTitle)) {
            paint.setColor(builder.context.getResources().getColor(builder.textColor));
            paint.setTextSize(builder.textSize);
            c.drawText(itemViewTitle, x, y, paint);
        }
    }

    private boolean needTitle(int position) {
        if (builder.heads.size() == 0) {
            return false;
        }
        if (builder.isHeadView && position == 0) return false;
        if (builder.isHeadView && position > builder.heads.size()) return false;
        if (builder.isHeadView) {
            return position == 1 || !builder.heads.get(position - 1).equals(builder.heads.get(position - 2));
        } else if (builder.isFootView) {
            return position < builder.heads.size();
        } else {
            return position == 0 || !builder.heads.get(position).equals(builder.heads.get(position - 1));
        }
    }
}
