package com.lm.myui.widget.recycler;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView分割线，支持GridLayoutManager的横向和纵向分割线，支持LinearLayoutManager的横向分割线
 *
 * @author TFly
 * @github https://github.com/wslaimin/MYUI.git
 */
public class MyRecyclerViewDivider extends RecyclerView.ItemDecoration {
    private Drawable drawable;
    private int horizontalSize;
    private int verticalSize;

    public MyRecyclerViewDivider(Drawable drawable) {
        this.drawable = drawable;
    }

    public MyRecyclerViewDivider(int color) {
        drawable = new ColorDrawable(color);
    }

    /**
     * 设置水平分割线高度
     * @param size 分割线高度
     */
    public void setHorizontalSize(int size) {
        horizontalSize = size;
    }

    /**
     * 获取水平分割线高度
     * @return 水平分割线高度
     */
    public int getHorizontalSize() {
        return horizontalSize;
    }

    /**
     * 设置垂直分割线宽度
     * @param size 分割线宽度
     */
    public void setVerticalSize(int size) {
        verticalSize = size;
    }

    /**
     * 获取垂直分割线宽度
     * @return 垂直分割线宽度
     */
    public int getVerticalSize() {
        return verticalSize;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int position = layoutManager.getPosition(view);

        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            int spanCount = gridLayoutManager.getSpanCount();
            position = gridLayoutManager.getPosition(view);
            int spanIndex = gridLayoutManager.getSpanSizeLookup().getSpanIndex(position, gridLayoutManager.getSpanCount());
            if((spanIndex+1)%spanCount!=0){
                outRect.right = verticalSize;
            }

            int lastRow = spanSizeLookup.getSpanGroupIndex(parent.getAdapter().getItemCount(), spanCount);
            int row = spanSizeLookup.getSpanGroupIndex(position, spanCount);
            if (row < lastRow) {
                outRect.bottom = horizontalSize;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            if (position < parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = horizontalSize;
            }
        } else {
            throw new UnsupportedOperationException("Only supports LinearLayoutManager and GridLayoutManager!");
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        View child;
        int left, top, right, bottom;

        for (int i = 0; i < layoutManager.getChildCount(); i++) {
            child = layoutManager.getChildAt(i);
            if (horizontalSize != 0 && layoutManager.getDecoratedBottom(child) != child.getBottom()) {
                left = child.getLeft();
                right = child.getRight();
                top = child.getBottom();
                bottom = top + horizontalSize;
                drawable.setBounds(left, top, right, bottom);
                drawable.draw(c);
            }

            if (verticalSize != 0 && layoutManager.getDecoratedRight(child) != child.getRight()) {
                left = child.getRight();
                right = left + verticalSize;
                top = child.getTop();
                bottom = child.getBottom() + horizontalSize;
                drawable.setBounds(left, top, right, bottom);
                drawable.draw(c);
            }
        }
    }

}
