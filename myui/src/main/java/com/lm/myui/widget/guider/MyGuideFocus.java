package com.lm.myui.widget.guider;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyGuideFocus {
    public enum Shape {
        Circle, Rectangle
    }

    private Rect focusTarget = new Rect();
    private View focusView;
    private List<MyGuideContent> contents = new ArrayList<>();
    private WindowManager wm;
    private boolean touchable = true;
    private int paddingLeft, paddingRight, paddingTop, paddingBottom;
    private boolean focus;
    private Shape shape = Shape.Rectangle;

    public MyGuideFocus(Context context) {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * @return 获取焦点目标
     */
    public Rect getFocusTarget() {
        return focusTarget;
    }

    /**
     * @return 焦点View
     */
    public View getFocusView() {
        return focusView;
    }

    /**
     * 设置焦点View
     *
     * @param view 焦点View
     */
    public void setFocusView(View view) {
        focusView = view;
        Rect rect = new Rect();
        focusView.getGlobalVisibleRect(rect);
        setFocusTarget(rect);

        focusView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Rect r = new Rect();
                v.getGlobalVisibleRect(r);
                setFocusTarget(r);
            }
        });
    }

    /**
     * @return 引导内容
     */
    public List<MyGuideContent> getContents() {
        return contents;
    }

    /**
     * 添加引导内容
     *
     * @param content 引导内容
     */
    public void addGuideContent(MyGuideContent content) {
        contents.add(content);
    }

    /**
     * @return 点击状态
     */
    public boolean isTouchable() {
        return touchable;
    }

    /**
     * 设置点击状态
     *
     * @param bool 状态
     */
    public void setTouchable(boolean bool) {
        touchable = bool;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        paddingLeft = left;
        paddingRight = right;
        paddingTop = top;
        paddingBottom = bottom;
        if (focusView != null) {
            setFocusView(focusView);
        }
    }

    /**
     * @return 焦点区域形状
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * 设置焦点区域形状
     *
     * @param shape
     */
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    void setFocus(boolean bool) {
        focus = bool;
    }

    boolean getFocus() {
        return focus;
    }

    private void setFocusTarget(Rect rect) {
        if (!rect.isEmpty()) {
            Rect r = new Rect(rect);
            if (shape == Shape.Circle) {
                int delta;
                if (r.width() > r.height()) {
                    delta = r.width() - r.height();
                    r.top -= delta / 2;
                    r.bottom += delta / 2;
                } else {
                    delta = r.height() - r.width();
                    r.left -= delta / 2;
                    r.right += delta / 2;
                }
            }
            r.left -= paddingLeft;
            r.right += paddingRight;
            r.top -= paddingTop;
            r.bottom += paddingBottom;
            focusTarget.set(r);
            prepareContents();
        }
    }

    private void prepareContents() {
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = getScreenHeight();
        if (contents != null) {
            FrameLayout.LayoutParams lp;
            for (int i = 0; i < contents.size(); i++) {
                MyGuideContent content = contents.get(i);
                View view = content.getContent();
                ViewGroup.LayoutParams nowLp = view.getLayoutParams();
                if (nowLp == null) {
                    lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                } else {
                    lp = new FrameLayout.LayoutParams(nowLp);
                }
                MyOffset offset = content.getOffset();
                switch (content.getHorizontalPosition()) {
                    case MyGuideContent.LEFT_TO_LEFT:
                        lp.gravity = Gravity.LEFT;
                        lp.leftMargin = focusTarget.left - 20 + offset.dx;
                        break;
                    case MyGuideContent.LEFT_TO_RIGHT:
                        lp.gravity = Gravity.LEFT;
                        lp.leftMargin = focusTarget.right + 20 + offset.dx;
                        break;
                    case MyGuideContent.RIGHT_TO_LEFT:
                        lp.gravity = Gravity.RIGHT;
                        lp.rightMargin = screenWidth - focusTarget.left - offset.dx + 20;
                        break;
                    case MyGuideContent.RIGHT_TO_RIGHT:
                        lp.gravity = Gravity.RIGHT;
                        lp.rightMargin = screenWidth - focusTarget.right - offset.dx - 20;
                        break;
                    case MyGuideContent.HORIZONTAL_CENTER:
                        int shiftX = focusTarget.centerX() - screenWidth / 2;
                        lp.gravity = Gravity.CENTER_HORIZONTAL;
                        lp.leftMargin = shiftX + offset.dx;
                        break;
                }
                switch (content.getVerticalPosition()) {
                    case MyGuideContent.TOP_TO_TOP:
                        lp.gravity |= Gravity.TOP;
                        lp.topMargin = focusTarget.top + offset.dy - 20;
                        break;
                    case MyGuideContent.TOP_TO_BOTTOM:
                        lp.gravity |= Gravity.TOP;
                        lp.topMargin = focusTarget.bottom + offset.dy + 20;
                        break;
                    case MyGuideContent.BOTTOM_TO_TOP:
                        lp.gravity |= Gravity.BOTTOM;
                        lp.bottomMargin = screenHeight - focusTarget.top - offset.dy + 20;
                        break;
                    case MyGuideContent.BOTTOM_TO_BOTTOM:
                        lp.gravity |= Gravity.BOTTOM;
                        lp.bottomMargin = screenHeight - focusTarget.bottom - offset.dy - 20;
                        break;
                    case MyGuideContent.VERTICAL_CENTER:
                        int shiftY = focusTarget.centerY() - screenHeight / 2;
                        lp.gravity |= Gravity.CENTER_VERTICAL;
                        lp.topMargin = shiftY + offset.dy;
                        break;
                }
                view.setLayoutParams(lp);
            }
        }
    }

    /**
     * Resources.getSystem().getDisplayMetrics().heightPixels不准确
     *
     * @return 屏幕高度
     */
    private int getScreenHeight() {
        int height = 0;
        final DisplayMetrics metrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        Method getRawH;

        try {
            // For JellyBean 4.2 (API 17) and onward
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealMetrics(metrics);
                height = metrics.heightPixels;
            } else {
                getRawH = Display.class.getMethod("getRawHeight");
                try {
                    height = (Integer) getRawH.invoke(display);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        }
        return height;
    }
}
