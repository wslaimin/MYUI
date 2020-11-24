package com.lm.myui.widget.recycler.hf;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;
import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.ViewCompat;

/**
 * This class provides ability of refresh and load more for nested View that implements {@link NestedScrollingChild}.
 * Compatible with material design widgets.
 *
 * @param <T> nested view type
 */
public abstract class MyBaseHFLayout<T extends View> extends FrameLayout implements NestedScrollingParent2 {
    private static final float FRACTION = 2f;
    private static final int IDLE = 0;
    private static final int DRAGGING = 1;
    private static final int LOADING = 2;
    protected View header;
    protected View footer;
    private T nestedView;
    private boolean headerEnable = true;
    private boolean footerEnable = true;
    private int headerVisibleHeight;
    private int footerVisibleHeight;
    private int nestedViewScrollHeight;
    private int headerState;
    private int footerState;
    private final NestedScrollingChildHelper scrollingChildHelper;
    private final HeaderAnimation headerAnimation;
    private MyHFListener hfListener;
    private MyPullListener pullListener;

    public MyBaseHFLayout(Context context) {
        this(context, null);
    }

    public MyBaseHFLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        header = createHeader();
        footer = createFooter();
        nestedView = createNestedView();
        if (header != null) {
            addView(header);
        }
        if (footer != null) {
            addView(footer);
        }
        nestedView.setLayoutParams(generateLayoutParams(attrs));
        addView(nestedView);

        scrollingChildHelper = new NestedScrollingChildHelper(this);
        scrollingChildHelper.setNestedScrollingEnabled(true);
        headerAnimation = new HeaderAnimation();
    }

    /**
     * @return header view
     */
    public View getHeader() {
        return header;
    }

    /**
     * @return footer view
     */
    public View getFooter() {
        return footer;
    }

    /**
     * @return nested scroll target
     */
    public T getNestedView() {
        return nestedView;
    }

    /**
     * enable or disable header
     *
     * @param bool true for enable header
     */
    public void enableHeader(boolean bool) {
        headerEnable = bool;
        if (!headerEnable && headerVisibleHeight != 0) {
            scrollTo(0, 0);
            resetHeader();
        }
    }

    /**
     * enable or disable footer
     *
     * @param bool true for enable footer
     */
    public void enableFooter(boolean bool) {
        footerEnable = bool;
        if (!footerEnable && footerVisibleHeight != 0) {
            scrollTo(0, 0);
            resetFooter();
        }
    }

    /**
     * @return true if header is enable
     */
    public boolean isHeaderEnable() {
        return headerEnable;
    }

    /**
     * @return true if footer is enable
     */
    public boolean isFooterEnable() {
        return footerEnable;
    }

    /**
     * should be called when refresh completed
     */
    public void completeRefresh() {
        headerState = IDLE;
        headerAnimation.stop();
        if (headerVisibleHeight != 0) {
            scrollBy(0, headerVisibleHeight);
            headerVisibleHeight = 0;
        }
        nestedView.scrollBy(0, -nestedViewScrollHeight);
    }

    /**
     * should be called when load more completed
     */
    public void completeLoadMore() {
        footerState = IDLE;
        if (footerVisibleHeight != 0) {
            stopNestedViewScroll();
            scrollBy(0, -footerVisibleHeight);
            nestedView.scrollBy(0, footerVisibleHeight);
            footerVisibleHeight = 0;
        }
    }

    /**
     * register a callback to be invoked when refreshing or loading more
     *
     * @param listener the callback that will run
     */
    public void setHFListener(MyHFListener listener) {
        hfListener = listener;
    }

    /**
     * register a callback to be invoked when pull header/footer down or up
     *
     * @param listener
     */
    public void setPullListener(MyPullListener listener) {
        pullListener = listener;
    }

    /**
     * @return header scroll range
     */
    public int getMaxHeaderScrollExtent() {
        if (header != null) {
            return header.getMeasuredHeight();
        }
        return 0;
    }

    /**
     * @return header visible height now
     */
    public int getHeaderVisibleHeight() {
        return headerVisibleHeight;
    }

    /**
     * call refreshing manually
     */
    public void callRefresh() {
        if(headerEnable) {
            stopNestedViewScroll();
            headerAnimation.stop();
            extraScroll(0);
            nestedView.scrollBy(0, -nestedViewScrollHeight);
            scrollTo(0, -getHeaderHeight() - 1);
            headerVisibleHeight = getHeaderHeight() + 1;
            headerAnimation.start(1, 1);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(nestedView.getMeasuredWidth() + getPaddingLeft() + getPaddingRight(), nestedView.getMeasuredHeight() + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        nestedView.layout(left, top, left + nestedView.getMeasuredWidth(), top + getMeasuredHeight());

        if (header != null) {
            top = -header.getMeasuredHeight();
            header.layout(left, top, left + header.getMeasuredWidth(), top + header.getMeasuredHeight());
        }

        if (footer != null) {
            top = getMeasuredHeight() - getPaddingBottom();
            footer.layout(left, top, left + footer.getMeasuredWidth(), top + footer.getMeasuredHeight());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            headerAnimation.stop();
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (headerVisibleHeight > getHeaderHeight()) {
                headerAnimation.start(headerVisibleHeight - getHeaderHeight(), 1);
                return true;
            } else if (headerState == DRAGGING && headerVisibleHeight > 0 && headerVisibleHeight < getHeaderHeight()) {
                headerAnimation.start(headerVisibleHeight, 1);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        scrollingChildHelper.startNestedScroll(axes, type);
        return target == nestedView;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {

    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        scrollingChildHelper.stopNestedScroll(type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        nestedViewScrollHeight += dyConsumed;
        int remainY;
        int consumedY;
        if (dyUnconsumed < 0 && headerEnable) {
            //expand header
            int unConsumedY = dyUnconsumed + getExtraHeight();
            if (unConsumedY >= 0) {
                scrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null, type);
                return;
            }
            if (type == ViewCompat.TYPE_TOUCH) {
                remainY = getMaxHeaderScrollExtent() - headerVisibleHeight;
                consumedY = Math.max(unConsumedY, -remainY);
                consumedY = Math.round(consumedY / FRACTION);
                headerVisibleHeight -= consumedY;
                scrollBy(0, consumedY);
                if (headerState == IDLE) {
                    headerState = DRAGGING;
                }
                if (headerState == DRAGGING && pullListener != null) {
                    pullListener.pullHeader();
                }
            } else if (headerState == LOADING) {
                remainY = getHeaderHeight() - headerVisibleHeight;
                if (remainY > 0) {
                    consumedY = Math.max(unConsumedY, -remainY);
                    headerVisibleHeight -= consumedY;
                    scrollBy(0, consumedY);
                }
            }
        } else if (footerEnable && dyUnconsumed > 0) {
            //expand footer
            remainY = getFooterHeight() - footerVisibleHeight;
            consumedY = Math.min(dyUnconsumed, remainY);
            footerVisibleHeight += consumedY;
            scrollBy(0, consumedY);
            if (footerVisibleHeight > 0 && footerState != LOADING) {
                footerState = LOADING;
                if (hfListener != null) {
                    hfListener.onLoadMore();
                }
            }
            if (pullListener != null) {
                pullListener.pullFooter();
            }
        }

        scrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        int consumedY = 0;
        if (dy < 0) {
            //collapse footer
            if (footerVisibleHeight > 0) {
                consumedY = Math.max(dy, -footerVisibleHeight);
                footerVisibleHeight += consumedY;
                scrollBy(0, consumedY);

                if (pullListener != null) {
                    pullListener.pullFooter();
                }
            }

        } else {
            //collapse header
            if (headerVisibleHeight > 0) {
                consumedY = dy;
                int h = Math.min(headerVisibleHeight, dy);
                headerVisibleHeight -= h;
                scrollBy(0, h);

                if (headerState == DRAGGING && pullListener != null) {
                    pullListener.pullHeader();
                }
            }
        }
        consumed[1] = consumedY;

        scrollingChildHelper.dispatchNestedPreScroll(dx, dy - consumedY, consumed, null, type);
    }

    /**
     * subclass provides header view
     *
     * @return header view
     */
    protected abstract View createHeader();

    /**
     * subclass provides footer view
     *
     * @return footer view
     */
    protected abstract View createFooter();

    /**
     * subclass provides nested scroll view
     *
     * @return nested view
     */
    protected abstract T createNestedView();

    /**
     * height left to show header(for example,use with AppBarLayout)
     *
     * @return height left now
     */
    protected abstract int getExtraHeight();

    /**
     * subclass implements for stop nest view scroll
     */
    protected abstract void stopNestedViewScroll();

    protected abstract void extraScroll(int dy);

    private int getHeaderHeight() {
        if (header != null) {
            return header.getMeasuredHeight();
        }
        return 0;
    }

    private int getFooterHeight() {
        if (footer != null) {
            return footer.getMeasuredHeight();
        }
        return 0;
    }

    private void resetHeader() {
        headerVisibleHeight = 0;
    }

    private void resetFooter() {
        footerVisibleHeight = 0;
    }

    private class HeaderAnimation implements Runnable {
        int totalY;
        int lastY;
        //1:scroll up,-1:scroll down
        int direction;
        Scroller scroller;

        public HeaderAnimation() {
            scroller = new Scroller(getContext(), new DecelerateInterpolator());
        }

        public void start(int dy, int direction) {
            totalY = dy;
            lastY = 0;
            this.direction = direction;
            scroller.startScroll(0, 0, 0, dy);
            postOnAnimation(this);
        }

        public void stop() {
            totalY = lastY = 0;
            scroller.forceFinished(true);
            removeCallbacks(this);
        }

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
            } else {
                if (headerVisibleHeight == 0) {
                    headerState = IDLE;
                } else {
                    if (headerState != LOADING) {
                        headerState = LOADING;
                        if (hfListener != null) {
                            hfListener.onRefresh();
                        }
                    }
                }
            }
        }
    }

    public interface MyHFListener {
        void onRefresh();

        void onLoadMore();
    }

    public interface MyPullListener {
        void pullHeader();

        void pullFooter();
    }
}
