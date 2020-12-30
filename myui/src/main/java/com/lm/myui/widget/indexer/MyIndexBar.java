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
 * view for index quickly
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
     * set text size
     *
     * @param size desired size in px
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
     * get text size
     *
     * @return size in px
     */
    public float getTextSize() {
        return textSize;
    }

    /**
     * set unselected text color
     *
     * @param color a color value in the form 0xAARRGGBB
     */
    public void setUnSelectedColor(int color) {
        if (color == unSelectedColor) {
            return;
        }
        unSelectedColor = color;
        invalidate();
    }

    /**
     * get unselected text color
     *
     * @return a color value in the form 0xAARRGGBB
     */
    public int getUnSelectedColor() {
        return unSelectedColor;
    }

    /**
     * set selected text color
     *
     * @param color a color value in the form 0xAARRGGBB
     */
    public void setSelectedColor(int color) {
        if (color == selectedColor) {
            return;
        }
        selectedColor = color;
        invalidate();
    }

    /**
     * get selected text color
     *
     * @return a color value in the form 0xAARRGGBB
     */
    public int getSelectedColor() {
        return selectedColor;
    }

    /**
     * get vertical space between text
     *
     * @return dimension in px
     */
    public float getVerticalSpace() {
        return verticalSpace;
    }

    /**
     * set vertical space between text
     *
     * @param space dimension in px
     */
    public void setVerticalSpace(float space) {
        if (verticalSpace == space) {
            return;
        }
        verticalSpace = space;
        requestLayout();
    }

    /**
     * get index text list
     *
     * @return index text list
     */
    public List<String> getIndexs() {
        return indexs;
    }

    /**
     * set displayed text list
     *
     * @param list text list
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

    /**
     *
     * @return bubble view
     */
    public View getBubbleView() {
        return bubbleView;
    }

    /**
     * set bubble view
     *
     * @param view custom bubble view
     */
    public void setBubbleView(View view) {
        this.bubbleView = view;
    }

    public int getGravity() {
        return gravity;
    }

    /**
     * set gravity of text in MyIndexBar
     *
     * @param gravity value of {@link Gravity}
     */
    public void setGravity(int gravity) {
        if (this.gravity == gravity) {
            return;
        }
        this.gravity = gravity;
        invalidate();
    }

    /**
     * get index listener
     *
     * @return listener
     */
    public IndexListener getIndexListener() {
        return listener;
    }

    /**
     * set index listener
     *
     * @param listener instance of {@link IndexListener}
     */
    public void setIndexListener(IndexListener listener) {
        this.listener = listener;
    }

    /**
     * get text at specific position
     *
     * @param pos position in 0 to indexs length
     * @return text at specific position
     */
    public String getIndexStr(int pos) {
        if (indexs == null || pos < 0 || pos >= indexs.size()) {
            return null;
        }
        return indexs.get(pos);
    }

    /**
     * set indicator color of selected index
     *
     * @param color a color value in the form 0xAARRGGBB
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
     * listener for index changed
     */
    public interface IndexListener {
        /**
         * @param position selected position
         */
        void touchIndex(int position);

        /**
         * called when touch action is up
         */
        void touchOver();
    }
}
