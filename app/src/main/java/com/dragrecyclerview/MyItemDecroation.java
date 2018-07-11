package com.dragrecyclerview;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by AED on 2018/7/11.
 */

public class MyItemDecroation extends RecyclerView.ItemDecoration {

    private final ColorDrawable mDivider;
    private final int orientation;//方向
    private int space;

    public MyItemDecroation(int space,int orientation,int color) {
        this.space = space;
        this.orientation = orientation;
        mDivider = new ColorDrawable(color);
    }

    /**
     * 绘制装饰
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (orientation == LinearLayoutManager.VERTICAL) {//垂直
            drawHorizontalLines(c, parent);
        }
    }


    /**
     * 绘制垂直布局 水平分割线
     */
    private void drawHorizontalLines(Canvas c, RecyclerView parent) {
        //  final int itemCount = parent.getChildCount()-1;//出现问题的地方  下面有解释
        final int itemCount = parent.getChildCount();
        Log.d("Bruce","---->"+itemCount);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < itemCount; i++) {
            final View child = parent.getChildAt(i);
            if (child == null) return;
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top +space;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
