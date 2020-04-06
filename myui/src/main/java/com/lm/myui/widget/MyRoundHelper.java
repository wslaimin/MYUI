package com.lm.myui.widget;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.lm.myui.R;
import java.lang.ref.WeakReference;
import java.util.Arrays;

class MyRoundHelper {
    private Path path = new Path();
    private Path clipPath = new Path();
    private RectF rect = new RectF();
    private RectF clipRect = new RectF();
    private float[] radius = new float[8];
    private float strokeWidth;
    private int strokeColor;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private WeakReference<View> viewRef;

    MyRoundHelper(View view, AttributeSet attrs) {
        viewRef = new WeakReference<>(view);

        TypedArray a = view.getContext().obtainStyledAttributes(attrs, R.styleable.MyRound);
        float r = a.getDimensionPixelSize(R.styleable.MyRound_radius, 0);
        if (r != 0) {
            setRadius(r);
        }
        r = a.getDimensionPixelSize(R.styleable.MyRound_android_topLeftRadius, 0);
        if (r != 0) {
            setTopLeftRadius(r);
        }
        r = a.getDimensionPixelSize(R.styleable.MyRound_android_topRightRadius, 0);
        if (r != 0) {
            setTopRightRadius(r);
        }
        r = a.getDimensionPixelSize(R.styleable.MyRound_android_bottomRightRadius, 0);
        if (r != 0) {
            setBottomRightRadius(r);
        }
        r = a.getDimensionPixelSize(R.styleable.MyRound_android_bottomLeftRadius, 0);
        if (r != 0) {
            setBottomLeftRadius(r);
        }

        setStrokeWidth(a.getDimensionPixelSize(R.styleable.MyRound_strokeWidth, 0));
        setStrokeColor(a.getColor(R.styleable.MyRound_strokeColor, 0));
        a.recycle();

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(strokeColor);
        paint.setStrokeWidth(strokeWidth);
    }

    void setRadius(float radius) {
        Arrays.fill(this.radius, radius);
        invalidate();
    }

    float getStrokeWidth() {
        return strokeWidth;
    }

    void setStrokeWidth(float width) {
        if (strokeWidth == width) {
            return;
        }
        strokeWidth = width;
        paint.setStrokeWidth(strokeWidth);
        invalidate();
    }

    int getStrokeColor() {
        return strokeColor;
    }

    void setStrokeColor(int color) {
        if (strokeColor == color) {
            return;
        }
        strokeColor = color;
        paint.setColor(strokeColor);
        invalidate();
    }

    void setTopLeftRadius(float radius) {
        setRadius(0, radius);
    }

    float getTopLeftRadius() {
        return radius[0];
    }

    void setTopRightRadius(float radius) {
        setRadius(2, radius);
    }

    float getTopRightRadius() {
        return radius[2];
    }

    void setBottomRightRadius(float radius) {
        setRadius(4, radius);
    }

    float getBottomRightRadius() {
        return radius[4];
    }

    void setBottomLeftRadius(float radius) {
        setRadius(6, radius);
    }

    float getBottomLeftRadius() {
        return radius[6];
    }

    private void setRadius(int position, float radius) {
        this.radius[position] = radius;
        this.radius[position + 1] = radius;
        invalidate();
    }

    void setBounds(Canvas canvas,int w, int h) {
        float inset = strokeWidth * 0.5f;
        rect.left = inset;
        rect.top = inset;
        rect.right = w - inset;
        rect.bottom = h - inset;

        clipRect.left = inset;
        clipRect.top = inset;
        clipRect.right = w - inset;
        clipRect.bottom = h - inset;

        path.reset();
        path.addRoundRect(rect,radius,Path.Direction.CW);
        clipPath.reset();
        clipPath.addRoundRect(clipRect,radius,Path.Direction.CW);

        canvas.save();
        canvas.clipPath(clipPath);
    }

    void drawRound(Canvas canvas) {
        canvas.restore();
        canvas.drawPath(path, paint);
    }

    private void invalidate() {
        View view = viewRef.get();
        if (view != null) {
            view.invalidate();
        }
    }

}