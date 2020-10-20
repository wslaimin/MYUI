package com.lm.myui.widget.guider;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class MyGuider extends FrameLayout {
    private List<MyGuideFocus> guideFocus = new ArrayList<>();
    private int maskColor;
    private Paint paint;
    private int currentIndex = -1;
    private Activity activity;
    private Xfermode xfermode;
    private ViewGroup root;
    private ValueAnimator bounceAnimator;
    private ValueAnimator flyInAnimator;
    private ValueAnimator flyOutAnimator;
    private ObjectAnimator alphaAnimator;
    private int bounceValue;
    private float flyValue;
    private boolean animatorCreated;
    private boolean flying;
    private int flyingIndex;

    public MyGuider(Activity activity) {
        super(activity);
        this.activity = activity;
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    /**
     * 添加焦点目标
     *
     * @param focus 焦点目标
     */
    public void addGuideFocus(MyGuideFocus focus) {
        guideFocus.add(focus);
    }

    /**
     * 获取遮罩颜色
     *
     * @return 遮罩颜色
     */
    public int getMaskColor() {
        return maskColor;
    }

    /**
     * 设置遮罩颜色
     *
     * @param color 颜色
     */
    public void setMaskColor(int color) {
        maskColor = color;
        paint.setColor(maskColor);
    }

    /**
     * 开始引导
     */
    public void show() {
        root = (ViewGroup) activity.findViewById(android.R.id.content).getRootView();
        root.addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        showInternal(currentIndex + 1);
    }

    /**
     * 结束引导
     */
    public void dismiss() {
        root.removeView(this);
        bounceAnimator.cancel();
    }

    /**
     * 显示下一个引导
     */
    public void showNext() {
        showInternal(currentIndex + 1);
    }

    /**
     * 显示前一个引导
     */
    public void showPre() {
        showInternal(currentIndex - 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(maskColor);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setXfermode(xfermode);
        Rect focusRect = guideFocus.get(flyingIndex).getFocusTarget();
        paint.setColor(Color.WHITE);
        if(guideFocus.get(flyingIndex).getShape()== MyGuideFocus.Shape.Circle){
            canvas.drawCircle(focusRect.centerX(),focusRect.centerY(),focusRect.width()/2f+flyValue+bounceValue,paint);
        }else {
            canvas.drawRect(focusRect.left - flyValue - bounceValue, focusRect.top - flyValue - bounceValue,
                    focusRect.right + flyValue + bounceValue, focusRect.bottom + flyValue + bounceValue, paint);
        }
        paint.setXfermode(null);
    }

    @Override
    public void setBackground(Drawable background) {
        throw new RuntimeException("please use setMaskColor method");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean inFocusZone = inFocusZone(ev);
        if (flying || !inFocusZone || !guideFocus.get(currentIndex).isTouchable()) {
            setClickable(true);
            return true;
        }
        setClickable(false);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    private void showInternal(int index) {
        if (currentIndex == index) {
            return;
        }
        removeAllViews();
        if (index >= guideFocus.size()) {
            postOnAnimation(new Runnable() {
                @Override
                public void run() {
                    startFlyOut();
                }
            });
            return;
        }
        if (currentIndex >= 0) {
            guideFocus.get(currentIndex).setFocus(false);
        }
        currentIndex = index;
        guideFocus.get(currentIndex).setFocus(true);
        if (guideFocus.get(currentIndex).getContents() != null) {
            for (int i = 0; i < guideFocus.get(currentIndex).getContents().size(); i++) {
                View view=guideFocus.get(currentIndex).getContents().get(i).getContent();
                view.setAlpha(0);
                addView(view);
            }
        }
        postOnAnimation(new Runnable() {
            @Override
            public void run() {
                if (!animatorCreated) {
                    CreateAnimator();
                }
                if (currentIndex > 0) {
                    startFlyOut();
                } else {
                    startFlyIn();
                }
            }
        });
    }

    private boolean inFocusZone(MotionEvent ev) {
        boolean result = false;
        for (int i = 0; i < guideFocus.size(); i++) {
            MyGuideFocus myGuideFocus = guideFocus.get(i);
            if (myGuideFocus.getFocus() && myGuideFocus.getFocusTarget().contains((int) ev.getX(), (int) ev.getY())) {
                result = true;
                break;
            }
        }
        return result;
    }

    private void CreateAnimator() {
        animatorCreated = true;
        flyInAnimator = ValueAnimator.ofFloat(1.2f * Math.max(getWidth(), getHeight()), 0);
        flyInAnimator.setDuration(500);
        flyInAnimator.setInterpolator(new DecelerateInterpolator());
        flyInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                flyValue = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        flyInAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                flying = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                flying = false;
                startBounce();
                startAlpha();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                flying = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        flyOutAnimator = ValueAnimator.ofFloat(0, 1.2f * Math.max(getWidth(), getHeight()));
        flyOutAnimator.setDuration(500);
        flyOutAnimator.setInterpolator(new DecelerateInterpolator());
        flyOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                flyValue = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        flyOutAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                flying = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                flying = false;
                if (flyingIndex + 1 == guideFocus.size()) {
                    dismiss();
                } else {
                    startFlyIn();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                flying = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        bounceAnimator = ValueAnimator.ofInt(20);
        bounceAnimator.setDuration(1000);
        bounceAnimator.setInterpolator(new BounceInterpolator());
        bounceAnimator.setRepeatMode(ValueAnimator.REVERSE);
        bounceAnimator.setRepeatCount(ValueAnimator.INFINITE);
        bounceAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bounceValue = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });

        alphaAnimator = ObjectAnimator.ofFloat(new AlphaObject(null),"alpha",0,1);
        alphaAnimator.setDuration(500);
    }

    private void startBounce() {
        stopBounce();
        bounceAnimator.start();
    }

    private void stopBounce() {
        bounceAnimator.cancel();
    }

    private void startFlyIn() {
        stopFlyIn();
        flyInAnimator.start();
        flyingIndex = currentIndex;
    }

    private void stopFlyIn() {
        flyInAnimator.cancel();
    }

    private void startFlyOut() {
        stopFlyOut();
        flyOutAnimator.start();
    }

    private void stopFlyOut() {
        flyOutAnimator.cancel();
    }

    private void startAlpha() {
        stopAlpha();
        List<MyGuideContent> contents = guideFocus.get(flyingIndex).getContents();
        if (contents != null) {
            List<View> views = new ArrayList<>();
            for (int i = 0; i < contents.size(); i++) {
                views.add(contents.get(i).getContent());
            }
            alphaAnimator.setTarget(new AlphaObject(views));
            alphaAnimator.start();
        }
    }

    private void stopAlpha() {
        alphaAnimator.cancel();
    }
}
