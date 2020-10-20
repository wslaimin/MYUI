package com.lm.myui.widget.recycler.hf;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

//TODO: support material design
public class MyHFLayout extends ViewGroup implements NestedScrollingParent2 {
    private static int DEFAULT = 0;
    private static int LOADING = 1;
    private static int COMPLETING = 2;
    private static float FRACTION = 2f;
    private int MAX_HEADER_HEIGHT = 500;
    private View header;
    private View footer;
    private RecyclerView nestedView;
    private int footerState;
    private int headerState;
    private HFListener listener;
    private int headerVisibleHeight;
    private int footerVisibleHeight;
    private int scrollHeight;
    private int maxHeaderHeight = MAX_HEADER_HEIGHT;
    private Scroller scroller;
    private ViewFlinger viewFlinger;
    private boolean headerEnable = true;
    private boolean footerEnable = true;

    public MyHFLayout(Context context) {
        this(context, null);
    }

    public MyHFLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context, new LinearInterpolator());
        viewFlinger = new ViewFlinger();
    }

    public View getHeader() {
        return header;
    }

    public void setHeader(View view) {
        if(header!=null){
            throw new RuntimeException("already have header");
        }
        header = view;
        addView(header, 0);
    }

    public View getFooter() {
        return footer;
    }

    public void setFooter(View view) {
        if(footer!=null){
            throw new RuntimeException("already have footer");
        }
        footer = view;
        addView(footer);
    }

    public void completeLoadMore() {
        footerState = DEFAULT;
        stopScroll();
        if (footerVisibleHeight != 0) {
            scrollBy(0, -footerVisibleHeight);
            nestedView.scrollBy(0, footerVisibleHeight);
            scrollHeight += footerVisibleHeight;
            footerVisibleHeight = 0;
        }
    }

    public void completeRefresh() {
        headerState = COMPLETING;
        stopScroll();
        if (headerVisibleHeight != 0) {
            scrollBy(0, headerVisibleHeight);
            headerVisibleHeight = 0;
        }
        nestedView.scrollBy(0, -scrollHeight);
        scrollHeight = 0;
    }

    public HFListener getLoadListener() {
        return listener;
    }

    public void setLoadListener(HFListener listener) {
        this.listener = listener;
    }

    public void setMaxHeaderHeight(int height) {
        maxHeaderHeight = height;
    }

    public void enableHeader(boolean enable) {
        headerEnable = enable;
    }

    public boolean headerEnable() {
        return headerEnable;
    }

    public void enableFooter(boolean enable) {
        footerEnable = enable;
    }

    public boolean footerEnable(boolean enable) {
        return footerEnable;
    }

    public void callRefresh() {
        nestedView.post(new Runnable() {
            @Override
            public void run() {
                stopScroll();
                nestedView.scrollBy(0, -scrollHeight);
                scrollTo(0, 0);
                headerVisibleHeight=0;
                footerVisibleHeight=0;
                startViewFlinger(header.getMeasuredHeight(), -1);
                if(listener!=null){
                    listener.onRefresh();
                }
            }
        });
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (child instanceof RecyclerView) {
            acceptNestedChild((RecyclerView) child);
        }
        super.addView(child, index, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(nestedView, widthMeasureSpec, heightMeasureSpec);

        int widthSpec = MeasureSpec.makeMeasureSpec(nestedView.getMeasuredWidth(), MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        if (header != null) {
            measureChild(header, widthSpec, heightSpec);
        }
        if (footer != null) {
            measureChild(footer, widthSpec, heightSpec);
        }
        setMeasuredDimension(resolveSize(nestedView.getMeasuredWidth(), widthMeasureSpec), resolveSize(nestedView.getMeasuredHeight(), heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        nestedView.layout(left, top, left + nestedView.getMeasuredWidth(), top + getMeasuredHeight());

        if (header != null) {
            top =  - header.getMeasuredHeight();
            header.layout(left, top, left + header.getMeasuredWidth(), top + header.getMeasuredHeight());
        }

        if (footer != null) {
            top = getMeasuredHeight();
            footer.layout(left, top, left + footer.getMeasuredWidth(), top + footer.getMeasuredHeight());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            stopScroll();
            if (headerState == COMPLETING) {
                headerState = DEFAULT;
            }
        } else if (MotionEvent.ACTION_UP == ev.getAction() || MotionEvent.ACTION_CANCEL == ev.getAction()) {
            if (headerVisibleHeight >= header.getMeasuredHeight() && headerState == DEFAULT) {
                headerState = LOADING;
                if (listener != null) {
                    listener.onRefresh();
                }
            }

            if (headerVisibleHeight > header.getMeasuredHeight()) {
                startViewFlinger(headerVisibleHeight - header.getMeasuredHeight(), 1);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return target == nestedView;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {

    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        scrollHeight += dyConsumed;
        int remainY;
        int consumedY=0;
        if (dyUnconsumed < 0) {
            //expand header
            if (headerState == COMPLETING || !headerEnable) {
                return;
            } else if (type == ViewCompat.TYPE_TOUCH) {
                remainY = maxHeaderHeight - headerVisibleHeight;
                consumedY = Math.max(dyUnconsumed, -remainY);
                consumedY = Math.round(consumedY / FRACTION);
                headerVisibleHeight -= consumedY;
                scrollBy(0, consumedY);
            } else if (headerState == LOADING) {
                remainY = header.getMeasuredHeight() - headerVisibleHeight;
                if (remainY > 0) {
                    consumedY = Math.max(dyUnconsumed, -remainY);
                    headerVisibleHeight -= consumedY;
                    scrollBy(0, consumedY);
                }
            }
        }

        //expand footer
        if (footerEnable && dyUnconsumed > 0) {
            remainY = footer.getMeasuredHeight() - footerVisibleHeight;
            consumedY = Math.min(dyUnconsumed, remainY);
            footerVisibleHeight += consumedY;
            scrollBy(0, consumedY);
            if (footerState != LOADING) {
                footerState = LOADING;
                if (listener != null) {
                    listener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        int remainY;
        int consumedY = 0;
        if (dy < 0) {
            //collapse footer
            if (footerVisibleHeight != 0) {
                remainY = footerVisibleHeight;
                consumedY = Math.max(dy, -remainY);
                footerVisibleHeight += consumedY;
                scrollBy(0, consumedY);
            }
        } else {
            //collapse header
            if (headerVisibleHeight != 0) {
                remainY = headerVisibleHeight;
                consumedY = Math.min(dy, remainY);
                headerVisibleHeight -= consumedY;
                scrollBy(0, consumedY);
            }
        }
        consumed[1] = consumedY;
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {

    }

    private void startViewFlinger(int dy, int direction) {
        scroller.forceFinished(true);
        removeCallbacks(viewFlinger);
        viewFlinger.lastY = 0;
        viewFlinger.totalY = dy;
        viewFlinger.direction = direction;
        scroller.startScroll(0, 0, 0, dy);
        postOnAnimation(viewFlinger);
    }

    private void stopScroll() {
        nestedView.stopScroll();
        scroller.forceFinished(true);
        removeCallbacks(viewFlinger);
        viewFlinger.lastY = 0;
        viewFlinger.totalY = 0;
    }

    private void acceptNestedChild(RecyclerView view) {
        if (nestedView != null) {
            throw new RuntimeException("only one RecyclerView child");
        }
        nestedView = view;
    }

    class ViewFlinger implements Runnable {
        int totalY;
        int lastY;
        //1:scroll up,-1:scroll down
        int direction;

        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                int nowY = scroller.getCurrY();
                int offsetY = nowY - lastY;
                int consumedY = Math.min(totalY, offsetY);
                lastY = nowY;
                scrollBy(0, consumedY * direction);
                headerVisibleHeight -= consumedY * direction;
                totalY -= consumedY;
                postOnAnimation(this);
            }
        }
    }

    public interface HFListener {
        void onRefresh();

        void onLoadMore();
    }

}
