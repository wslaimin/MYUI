package com.lm.myui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.lm.myui.R;

/**
 * View容器，横向布局，自动换行
 * {@attr MyUnevenLayout}
 *
 * @author TFly
 * @github https://github.com/wslaimin/MYUI.git
 */
public class MyUnevenLayout extends ViewGroup {
    private int mHorizontalSpace;
    private int mVerticalSpace;
    private int mItemHeight;

    public MyUnevenLayout(Context context) {
        this(context,null);
    }

    public MyUnevenLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyUnevenLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFromAttrs(context,attrs);
    }

    @TargetApi(21)
    public MyUnevenLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFromAttrs(context,attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalWidth = 0;
        int totalHeight = 0;
        int maxWidth = getPaddingLeft() + getPaddingRight() + (getChildCount() > 1 ? (getChildCount() - 1) * mHorizontalSpace : 0);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            if (totalHeight == 0) {
                totalHeight += child.getMeasuredHeight();
            }

            if (totalWidth == 0) {
                totalWidth = child.getMeasuredWidth();
            } else {
                totalWidth += mHorizontalSpace + child.getMeasuredWidth();
            }

            if (totalWidth > size - getPaddingLeft() - getPaddingRight()) {
                totalWidth = child.getMeasuredWidth();
                totalHeight += mVerticalSpace + child.getMeasuredHeight();
            }

            maxWidth += child.getMeasuredWidth();
        }
        totalHeight+=getPaddingTop()+getPaddingBottom();

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(totalHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int extraWidth = r - l - getPaddingLeft() - getPaddingRight();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getMeasuredWidth() <= extraWidth) {
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                left += child.getMeasuredWidth() + mHorizontalSpace;
                extraWidth = extraWidth - child.getMeasuredWidth() - mHorizontalSpace;
            } else {
                left = getPaddingLeft();
                //每個Item高度一樣才可以這麼算
                top += child.getMeasuredHeight() + mVerticalSpace;
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                left += child.getMeasuredWidth() + mHorizontalSpace;
                extraWidth = r - l - left - getPaddingRight();
            }
        }
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p != null && p.height == mItemHeight;
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new LayoutParams(p.width, mItemHeight);
    }

    private void initFromAttrs(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyUnevenLayout, 0, 0);
        mHorizontalSpace = (int) a.getDimension(R.styleable.MyUnevenLayout_horizontalSpace, 0);
        mVerticalSpace = (int) a.getDimension(R.styleable.MyUnevenLayout_verticalSpace, 0);
        mItemHeight = (int) a.getDimension(R.styleable.MyUnevenLayout_itemHeight, 60);
        a.recycle();
    }
}
