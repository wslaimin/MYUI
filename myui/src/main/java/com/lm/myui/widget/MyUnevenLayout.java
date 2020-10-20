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

    public MyUnevenLayout(Context context) {
        this(context, null);
    }

    public MyUnevenLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyUnevenLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFromAttrs(context, attrs);
    }

    @TargetApi(21)
    public MyUnevenLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFromAttrs(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int totalHeight = -mVerticalSpace;
        int maxWidth = 0;
        int maxHeight = 0;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            if (width==0){
                width=child.getMeasuredWidth();
                maxHeight=child.getMeasuredHeight();
                maxWidth=Math.max(maxWidth,width);
            }else{
                width+=mHorizontalSpace+child.getMeasuredWidth();
                if(width>size-getPaddingLeft()-getPaddingRight()){
                    maxWidth=Math.max(maxWidth,width-mHorizontalSpace-child.getMeasuredWidth());
                }
            }
            if(width>size-getPaddingLeft()-getPaddingRight()){
                totalHeight+=mVerticalSpace+maxHeight;
                if(totalHeight>0){
                    width=child.getMeasuredWidth();
                    maxHeight=child.getMeasuredHeight();
                }else{
                    width=0;
                    maxHeight=0;
                }
            }else{
                maxHeight=Math.max(maxHeight,child.getMeasuredHeight());
            }
        }
        totalHeight += mVerticalSpace + maxHeight + getPaddingTop() + getPaddingBottom();
        maxWidth=Math.max(width,maxWidth);

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(totalHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int extraWidth = r - l - getPaddingLeft() - getPaddingRight();
        int maxHeight=0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getMeasuredWidth() <= extraWidth) {
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                left += child.getMeasuredWidth() + mHorizontalSpace;
                extraWidth = extraWidth - child.getMeasuredWidth() - mHorizontalSpace;
                maxHeight=Math.max(maxHeight,child.getMeasuredHeight());
            } else {
                left = getPaddingLeft();
                top += maxHeight + mVerticalSpace;
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                left += child.getMeasuredWidth() + mHorizontalSpace;
                maxHeight=child.getMeasuredHeight();
                extraWidth = r-l-getPaddingLeft()-getPaddingRight()-child.getMeasuredWidth()-mHorizontalSpace;
            }
        }
    }

    private void initFromAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyUnevenLayout, 0, 0);
        mHorizontalSpace = (int) a.getDimension(R.styleable.MyUnevenLayout_horizontalSpace, 0);
        mVerticalSpace = (int) a.getDimension(R.styleable.MyUnevenLayout_verticalSpace, 0);
        a.recycle();
    }
}
