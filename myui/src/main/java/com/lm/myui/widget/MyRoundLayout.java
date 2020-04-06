package com.lm.myui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 圆角容器
 * {@attr MyRound}
 *
 * @author TFly
 * @github https://github.com/wslaimin/MYUI.git
 */
public class MyRoundLayout extends FrameLayout implements MyIRoundView {
    private MyRoundHelper roundHelper;

    public MyRoundLayout(@NonNull Context context) {
        this(context, null);
    }

    public MyRoundLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRoundLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        roundHelper = new MyRoundHelper(this, attrs);
    }

    @TargetApi(21)
    public MyRoundLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        roundHelper = new MyRoundHelper(this, attrs);
    }

    @Override
    public void draw(Canvas canvas) {
        roundHelper.setBounds(canvas, getWidth(), getHeight());
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        roundHelper.drawRound(canvas);
    }

    @Override
    public void setTopLeftRadius(float radius) {
        roundHelper.setTopLeftRadius(radius);
    }

    @Override
    public float getTopLeftRadius() {
        return roundHelper.getTopLeftRadius();
    }

    @Override
    public void setTopRightRadius(float radius) {
        roundHelper.setTopRightRadius(radius);
    }

    @Override
    public float getTopRightRadius() {
        return roundHelper.getTopRightRadius();
    }

    @Override
    public void setBottomRightRadius(float radius) {
        roundHelper.setBottomRightRadius(radius);
    }

    @Override
    public float getBottomRightRadius() {
        return roundHelper.getBottomRightRadius();
    }

    @Override
    public void setBottomLeftRadius(float radius) {
        roundHelper.setBottomLeftRadius(radius);
    }

    @Override
    public float getBottomLeftRadius() {
        return roundHelper.getBottomLeftRadius();
    }

    @Override
    public void setRadius(float radius) {
        roundHelper.setRadius(radius);
    }

    @Override
    public void setStrokeWidth(float width) {
        roundHelper.setStrokeWidth(width);
    }

    @Override
    public float getStrokeWidth() {
        return roundHelper.getStrokeWidth();
    }

    @Override
    public void setStrokeColor(int color) {
        roundHelper.setStrokeColor(color);
    }

    @Override
    public int getStrokeColor() {
        return roundHelper.getStrokeColor();
    }
}
