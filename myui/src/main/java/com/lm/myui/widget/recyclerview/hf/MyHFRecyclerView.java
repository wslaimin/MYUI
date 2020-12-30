package com.lm.myui.widget.recyclerview.hf;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.AppBarLayout;

public class MyHFRecyclerView extends MyBaseHFLayout<RecyclerView> {
    private AppBarLayout.Behavior behavior;
    private int maxHeaderScrollExtent;

    public MyHFRecyclerView(Context context) {
        super(context);
    }

    public MyHFRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * set header view
     * @param view display when header visible
     */
    public void setHeader(View view){
        if(header!=null){
            removeView(header);
        }
        header=view;
        addView(header);
        requestLayout();
    }

    /**
     * set footer view
     * @param view display when footer visible
     */
    public void setFooter(View view){
        if(footer!=null){
            removeView(footer);
        }
        footer=view;
        addView(footer);
        requestLayout();
    }

    /**
     * set header max scroll range
     * @param extent max range
     */
    public void setMaxHeaderScrollExtent(int extent){
        maxHeaderScrollExtent=extent;
    }

    @Override
    protected View createHeader() {
        return null;
    }

    @Override
    protected View createFooter() {
        return null;
    }

    @Override
    protected RecyclerView createNestedView() {
        return new RecyclerView(getContext());
    }

    @Override
    protected int getExtraHeight() {
        if (behavior != null) {
            return -behavior.getTopAndBottomOffset();
        }
        return 0;
    }

    @Override
    protected void stopNestedViewScroll() {
        getNestedView().stopScroll();
    }

    @Override
    protected void extraScroll(int dy) {
        if(behavior!=null){
            behavior.setTopAndBottomOffset(dy);
            requestLayout();
        }
    }

    @Override
    public int getMaxHeaderScrollExtent() {
        return maxHeaderScrollExtent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        checkAppBarLayout();
    }

    private void checkAppBarLayout() {
        ViewParent p = getParent();
        CoordinatorLayout root = null;
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                root = (CoordinatorLayout) p;
                break;
            } else {
                p = p.getParent();
            }
        }
        if (root != null) {
            for (int i = 0; i < root.getChildCount(); i++) {
                View child = root.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    behavior = (AppBarLayout.Behavior) ((CoordinatorLayout.LayoutParams) child.getLayoutParams()).getBehavior();
                    break;
                }
            }
        }
    }
}
