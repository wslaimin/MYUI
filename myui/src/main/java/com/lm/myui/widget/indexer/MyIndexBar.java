package com.lm.myui.widget.indexer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
    private int unSelectedColor, selectedColor, indicatorColor;
    private float textSize;
    private float verticalSpace;
    private Rect rect = new Rect();
    private int gravity;
    private int touchIndex = -1;
    private float topLimit, bottomLimit;
    private View bubbleView;
    private ViewGroup rootView;
    private Rect offsetRect = new Rect();

    public MyIndexBar(Context context) {
        this(context, null);
    }

    public MyIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyIndexBar);

        paint = new Paint();
        paint.setAntiAlias(true);
        setTextSize(a.getDimensionPixelSize(R.styleable.MyIndexBar_android_textSize, 20));
        setUnSelectedColor(a.getColor(R.styleable.MyIndexBar_unSelectedColor, Color.BLACK));
        setSelectedColor(a.getColor(R.styleable.MyIndexBar_selectedColor, Color.WHITE));
        setIndicatorColor(a.getColor(R.styleable.MyIndexBar_indicatorColor, Color.GREEN));
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
     *
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
     *
     * @return 文本大小
     */
    public float getTextSize() {
        return textSize;
    }

    /**
     * 设置文本颜色
     *
     * @param color 文本颜色
     */
    public void setUnSelectedColor(int color) {
        if (color == unSelectedColor) {
            return;
        }
        unSelectedColor = color;
        invalidate();
    }

    /**
     * 获取文本颜色
     *
     * @return 文本颜色
     */
    public int getUnSelectedColor() {
        return unSelectedColor;
    }

    /**
     * 设置选中文本颜色
     *
     * @param color 文本颜色
     */
    public void setSelectedColor(int color) {
        if (color == selectedColor) {
            return;
        }
        selectedColor = color;
        invalidate();
    }

    /**
     * 获取选中文本颜色
     *
     * @return 文本颜色
     */
    public int getSelectedColor() {
        return selectedColor;
    }

    /**
     * 获取垂直距离
     *
     * @return 距离
     */
    public float getVerticalSpace() {
        return verticalSpace;
    }

    /**
     * 设置垂直距离
     *
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
     *
     * @return 索引列表
     */
    public List<String> getIndexs() {
        return indexs;
    }

    public View getBubbleView() {
        return bubbleView;
    }

    public void setBubbleView(View view) {
        this.bubbleView = view;
    }

    /**
     * 设置索引列表
     *
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
     *
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
     *
     * @return 监听器
     */
    public IndexListener getIndexListener() {
        return listener;
    }

    /**
     * 设置索引监听器
     *
     * @param listener 监听器
     */
    public void setIndexListener(IndexListener listener) {
        this.listener = listener;
    }

    /**
     * 获取索引文本
     *
     * @param pos 索引位置
     * @return 索引文本
     */
    public String getIndexStr(int pos) {
        if (indexs == null || pos < 0 || pos >= indexs.size()) {
            return null;
        }
        return indexs.get(pos);
    }

    /**
     * @param color 设置标识颜色
     */
    public void setIndicatorColor(int color) {
        indicatorColor = color;
    }

    /**
     * @return 获取标识颜色
     */
    public int getIndicatorColor() {
        return indicatorColor;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        rootView = (ViewGroup) getRootView().findViewById(android.R.id.content);
        rootView.offsetDescendantRectToMyCoords(this, offsetRect);
        resetBubbleView();
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
        for (int i = 0; i < indexStates.size(); i++) {
            IndexState state = indexStates.get(i);
            if (i == touchIndex) {
                paint.setColor(indicatorColor);
                canvas.drawCircle((state.left + state.right) / 2, (state.top + state.bottom) / 2, textSize / 2, paint);
                paint.setColor(selectedColor);
            } else {
                paint.setColor(unSelectedColor);
            }
            canvas.drawText(state.s, state.left, state.bottom, paint);

            paint.setStrokeWidth(0.5f);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
            case MotionEvent.ACTION_MOVE:
                int index = getIndexPosition(getTouchY(event.getY()));
                if (index != -1 && touchIndex != index) {
                    touchIndex = index;

                    if (bubbleView != null) {
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) bubbleView.getLayoutParams();
                        params.gravity = Gravity.RIGHT;
                        params.rightMargin = getWidth() + 80;

                        IndexState state = indexStates.get(index);
                        params.topMargin = Math.round((state.top + state.bottom) / 2f + offsetRect.top - bubbleView.getMeasuredHeight() / 2f);

                        bubbleView.setLayoutParams(params);
                    }

                    if (listener != null) {
                        listener.touchIndex(index);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                touchIndex = -1;
                setPressed(false);
                resetBubbleView();
                if (listener != null) {
                    listener.touchOver();
                }
                invalidate();
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
                topLimit = bottom;
                bottomLimit = getMeasuredHeight() - bottom;
                break;
            case Gravity.BOTTOM:
                bottom = getMeasuredHeight() - getIndexsHeight();
                topLimit = bottom;
                bottomLimit = getMeasuredHeight();
                break;
            case Gravity.TOP:
            default:
                bottom = getPaddingTop();
                topLimit = bottom;
                bottomLimit = bottom + getIndexsHeight();
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
                    left = (getMeasuredWidth() - rect.width()) / 2f;
            }
            IndexState state = new IndexState();
            state.top = bottom;
            bottom += rect.height();
            state.s = s;
            state.left = left;
            //字符绘制会偏移rect.left，为了左右对称向右扩大rect.left
            state.right = left + rect.width() + 2 * rect.left;
            state.bottom = bottom;
            indexStates.add(state);
            bottom += verticalSpace;
        }
    }

    private int getIndexPosition(float y) {
        for (int i = 0; i < indexStates.size(); i++) {
            IndexState state = indexStates.get(i);
            if (state.bottom >= y && y <= state.top) {
                return i;
            }
        }

        return -1;
    }

    private float getTouchY(float y) {
        return Math.min(Math.max(topLimit, y), bottomLimit);
    }

    private void resetBubbleView(){
        if(bubbleView==null){
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bubbleView.getLayoutParams() != null ?
                bubbleView.getLayoutParams() :
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        params.leftMargin = getResources().getDisplayMetrics().widthPixels + 100;
        params.topMargin = 0;
        bubbleView.setLayoutParams(params);
        if (bubbleView.getParent() == null) {
            rootView.addView(bubbleView);
        } else {
            bubbleView.setLayoutParams(params);
        }
    }

    private static class IndexState {
        float left, right, top, bottom;
        String s;
    }

    /**
     * 索引监听器
     */
    public interface IndexListener {
        /**
         * 选中索引回调
         *
         * @param position 索引位置
         */
        void touchIndex(int position);

        /**
         * 索引结束回调
         */
        void touchOver();
    }
}
