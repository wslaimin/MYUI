package com.lm.myui_demo.material;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;
import com.google.android.material.card.MaterialCardView;

public class MyDraggableFrameLayout extends FrameLayout {
    private ViewDragHelper dragHelper;

    public MyDraggableFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public MyDraggableFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        dragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return child instanceof MaterialCardView;
            }

            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                ((MaterialCardView) capturedChild).setDragged(true);
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                ((MaterialCardView) releasedChild).setDragged(false);
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                return top;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }
}
