package com.lm.myui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import com.lm.myui.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 列表索引条
 * {@attr MyIndexBar}
 *
 * @author TFly
 * @github https://github.com/wslaimin/MYUI.git
 */
public class MyIndexBar extends View {
    private List<String> indexs;
    private List<IndexState> indexStates = new ArrayList<>();
    private Paint paint;
    private IndexListener listener;
    private int textColor, indexColor;
    private float textSize;
    private float verticalSpace;
    private Rect rect = new Rect();
    private String indexStr;
    private int gravity;

    public MyIndexBar(Context context) {
        this(context, null);
    }

    public MyIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyIndexBar);
        paint = new Paint();
        paint.setAntiAlias(true);
        setTextSize(a.getDimensionPixelSize(R.styleable.MyIndexBar_textSize, 20));
        setTextColor(a.getColor(R.styleable.MyIndexBar_textColor, 0x00000000));
        setIndexColor(a.getColor(R.styleable.MyIndexBar_indexColor, 0x00000000));
        setVerticalSpace(a.getDimensionPixelSize(R.styleable.MyIndexBar_verticalSpace, 20));
        setGravity(a.getInt(R.styleable.MyIndexBar_android_gravity, 0));
        CharSequence[] s = a.getTextArray(R.styleable.MyIndexBar_indexs);
        if (s != null) {
            List<String> l = new ArrayList<>();
            for (int i = 0; i < s.length; i++) {
                l.add(s[i].toString());
            }
            setIndexs(l);
        }
        a.recycle();
        setClickable(true);
    }

    /**
     * 设置文本大小
     * @param size 文本大小
     */
    public void setTextSize(int size) {
        if (textSize == size) {
            return;
        }
        textSize = size;
        paint.setTextSize(textSize);
        requestLayout();
    }

    /**
     * 获取文本大小
     * @return 文本大小
     */
    public float getTextSize() {
        return textSize;
    }

    /**
     * 设置文本颜色
     * @param color 文本颜色
     */
    public void setTextColor(int color) {
        if (color == textColor) {
            return;
        }
        textColor = color;
        invalidate();
    }

    /**
     * 获取文本颜色
     * @return 文本颜色
     */
    public int getTextColor() {
        return textColor;
    }

    /**
     * 设置选中文本颜色
     * @param color 文本颜色
     */
    public void setIndexColor(int color) {
        if (color == indexColor) {
            return;
        }
        indexColor = color;
        invalidate();
    }

    /**
     * 获取选中文本颜色
     * @return 文本颜色
     */
    public int getIndexColor() {
        return indexColor;
    }

    /**
     * 获取垂直距离
     * @return 距离
     */
    public float getVerticalSpace() {
        return verticalSpace;
    }

    /**
     * 设置垂直距离
     * @param space 距离
     */
    public void setVerticalSpace(float space) {
        if (verticalSpace == space) {
            return;
        }
        verticalSpace = space;
        requestLayout();
    }

    /**
     * 获取索引列表
     * @return 索引列表
     */
    public List<String> getIndexs() {
        return indexs;
    }

    /**
     * 设置索引列表
     * @param list 索引列表
     */
    public void setIndexs(List<String> list) {
        indexs = list;
        indexStates.clear();
        for (int i = 0; indexs != null && i < indexs.size(); i++) {
            IndexState state = new IndexState();
            state.s = indexs.get(i);
            indexStates.add(state);
        }
        requestLayout();
    }

    public int getGravity() {
        return gravity;
    }

    /**
     * 设置文本Gravity
     * @param gravity gravity
     */
    public void setGravity(int gravity) {
        if (this.gravity == gravity) {
            return;
        }
        this.gravity = gravity;
        invalidate();
    }

    /**
     * 获取索引监听器
     * @return 监听器
     */
    public IndexListener getIndexListener() {
        return listener;
    }

    /**
     * 设置索引监听器
     * @param listener 监听器
     */
    public void setIndexListener(IndexListener listener) {
        this.listener = listener;
    }

    /**
     * 获取索引文本
     * @param pos 索引位置
     * @return 索引文本
     */
    public String getIndexStr(int pos) {
        if (indexs == null || pos < 0 || pos >= indexs.size()) {
            return null;
        }
        return indexs.get(pos);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            width = getPaddingLeft() + getPaddingRight();
            if (indexs != null) {
                int max = 0;
                for (String s : indexs) {
                    paint.getTextBounds(s, 0, s.length(), rect);
                    if (rect.width() > max) {
                        max = rect.width();
                    }
                }
                width += max;
            }
        } else {
            width = widthMeasureSpec;
        }

        int height;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            height = getPaddingTop() + getPaddingBottom();
            if (indexs != null) {
                height += getIndexsHeight();
            }
        } else {
            height = heightMeasureSpec;
        }
        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0), resolveSizeAndState(height, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        computeState();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (IndexState state : indexStates) {
            if (state.s.equals(indexStr)) {
                paint.setColor(indexColor);
            } else {
                paint.setColor(textColor);
            }
            canvas.drawText(state.s, state.left, state.bottom, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
            case MotionEvent.ACTION_MOVE:
                int index = getIndexPosition(event.getY());
                if (index != -1) {
                    indexStr = indexs.get(index);
                }
                if (listener != null) {
                    listener.index(index, Math.max(0,Math.min(getWidth(),event.getX())), Math.max(0,Math.min(getHeight(),event.getY())));
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                setPressed(false);
                indexStr=null;
                invalidate();
                if (listener != null) {
                    listener.over();
                }
                break;
        }
        return true;
    }

    private int getIndexsHeight() {
        int height = 0;
        if (indexs != null) {
            for (String s : indexs) {
                paint.getTextBounds(s, 0, s.length(), rect);
                height += rect.height();
            }
            height += (indexs.size() - 1) * verticalSpace;
        }
        return height;
    }

    private void computeState() {
        indexStates.clear();
        float bottom;
        //gravity解析参考FrameLayout
        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.CENTER_VERTICAL:
                bottom = (getMeasuredHeight() - getIndexsHeight()) / 2f;
                break;
            case Gravity.BOTTOM:
                bottom = getMeasuredHeight() - getIndexsHeight();
                break;
            case Gravity.TOP:
            default:
                bottom = getPaddingTop();
        }
        float left;
        for (int i = 0; indexs != null && i < indexs.size(); i++) {
            String s = indexs.get(i);
            paint.getTextBounds(s, 0, s.length(), rect);
            int absoluteGravity;
            if (Build.VERSION.SDK_INT >= 17) {
                absoluteGravity = Gravity.getAbsoluteGravity(gravity, getLayoutDirection());
            } else {
                absoluteGravity = Gravity.getAbsoluteGravity(gravity, 0);
            }
            switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.RIGHT:
                    left = getMeasuredWidth() - getPaddingRight() - rect.width();
                    break;
                case Gravity.LEFT:
                    left = getPaddingLeft();
                    break;
                case Gravity.CENTER_HORIZONTAL:
                default:
                    left = getPaddingLeft()+(getMeasuredWidth()-getPaddingLeft()-getPaddingRight() - rect.width()) / 2f;
            }
            bottom += rect.height();
            IndexState state = new IndexState();
            state.s = s;
            state.left = left;
            state.bottom = bottom;
            indexStates.add(state);
            bottom += verticalSpace;
        }

    }

    private int getIndexPosition(float y) {
        for (int i = 0; i < indexStates.size(); i++) {
            if (indexStates.get(i).bottom >= y) {
                return i;
            }
        }

        return indexStates.size() > 0 ? indexStates.size() - 1 : -1;
    }

    private static class IndexState {
        float left, bottom;
        String s;
    }

    /**
     * 索引监听器
     */
    public interface IndexListener {
        /**
         * 选中索引回调
         * @param position 索引位置
         * @param x 触摸事件x坐标
         * @param y 触摸事件y坐标
         */
        void index(int position, float x, float y);

        /**
         * 索引结束回调
         */
        void over();
    }
}
