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
     *
     * @param size 分割线高度
     */
    public void setHorizontalSize(int size) {
        horizontalSize = size;
    }

    /**
     * 获取水平分割线高度
     *
     * @return 水平分割线高度
     */
    public int getHorizontalSize() {
        return horizontalSize;
    }

    /**
     * 设置垂直分割线宽度
     *
     * @param size 分割线宽度
     */
    public void setVerticalSize(int size) {
        verticalSize = size;
    }

    /**
     * 获取垂直分割线宽度
     *
     * @return 垂直分割线宽度
     */
    public int getVerticalSize() {
        return verticalSize;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(0, 0, 0, 0);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int position = layoutManager.getPosition(view);
        int orientation;

        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            int spanCount = gridLayoutManager.getSpanCount();
            position = gridLayoutManager.getPosition(view);
            int spanIndex = gridLayoutManager.getSpanSizeLookup().getSpanIndex(position, gridLayoutManager.getSpanCount());
            int spanSize = gridLayoutManager.getSpanSizeLookup().getSpanSize(position);
            orientation = gridLayoutManager.getOrientation();
            int itemCount = layoutManager.getItemCount();
            if (orientation == RecyclerView.VERTICAL && (spanIndex + spanSize) % spanCount != 0 || orientation == RecyclerView.HORIZONTAL && position < itemCount - spanCount) {
                outRect.right = verticalSize;
            }

            int lastRow = spanSizeLookup.getSpanGroupIndex(layoutManager.getItemCount()-1, spanCount);
            int row = spanSizeLookup.getSpanGroupIndex(position, spanCount);
            if (orientation == RecyclerView.VERTICAL && row < lastRow|| orientation == RecyclerView.HORIZONTAL && (spanIndex + spanSize) % spanCount != 0) {
                outRect.bottom = horizontalSize;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == RecyclerView.VERTICAL && position < layoutManager.getItemCount() - 1) {
                outRect.bottom = horizontalSize;
            } else if (orientation == RecyclerView.HORIZONTAL && position < layoutManager.getItemCount() - 1) {
                outRect.right = verticalSize;
            }
        } else {
            throw new UnsupportedOperationException("Only supports LinearLayoutManager and GridLayoutManager!");
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        View child;
        int left, top, right, bottom;
        int tempSize;

        for (int i = 0; i < layoutManager.getChildCount(); i++) {
            tempSize=0;
            child = layoutManager.getChildAt(i);
            if (horizontalSize != 0 && layoutManager.getDecoratedBottom(child) != child.getBottom()) {
                left = Math.max(child.getLeft(), parent.getPaddingLeft());
                right = Math.min(child.getRight(), parent.getWidth() - parent.getPaddingRight());
                top = Math.max(parent.getPaddingTop(), child.getBottom());
                bottom = Math.min(child.getBottom() + horizontalSize, parent.getHeight() - parent.getPaddingBottom());
                if (left < right && top < bottom) {
                    drawable.setBounds(left, top, right, bottom);
                    drawable.draw(c);
                    tempSize=bottom-top;
                }
            }

            if (verticalSize != 0 && layoutManager.getDecoratedRight(child) != child.getRight()) {
                left = Math.max(parent.getPaddingLeft(), child.getRight());
                right = Math.min(child.getRight() + verticalSize, parent.getWidth() - parent.getPaddingRight());
                top = Math.max(child.getTop(), parent.getPaddingTop());
                bottom = Math.min(child.getBottom() + tempSize, parent.getHeight() - parent.getPaddingBottom());
                if (left < right && top < bottom) {
                    drawable.setBounds(left, top, right, bottom);
                    drawable.draw(c);
                }
            }
        }
    }
}
