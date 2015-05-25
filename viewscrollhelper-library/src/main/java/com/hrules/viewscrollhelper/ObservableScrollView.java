package com.hrules.viewscrollhelper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_ACTIVE = 1;

    private static final int SCROLL_STATE_CHECK_INTERVAL = 50;

    private long lastScrollUpdate;

    private int internalOldX;
    private int internalOldY;

    private ObservableScrollViewListener observableScrollViewListener;

    public ObservableScrollView(Context context) {
        this(context, null);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        lastScrollUpdate = -1;

        internalOldX = getScrollX();
        internalOldY = getScrollY();
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (observableScrollViewListener != null) {
            observableScrollViewListener.onScrollPositionChanged(getScrollX(), getScrollY());
            observableScrollViewListener.onScrolled(this, -(internalOldX - getScrollX()), -(internalOldY - getScrollY()));

            if (getScrollY() < internalOldY) {
                observableScrollViewListener.onScrollUp();
            } else {
                observableScrollViewListener.onScrollDown();
            }
        }

        if (lastScrollUpdate == -1) {
            postDelayed(new ScrollStateHandler(), SCROLL_STATE_CHECK_INTERVAL);
        }
        lastScrollUpdate = System.currentTimeMillis();

        internalOldX = getScrollX();
        internalOldY = getScrollY();
    }

    public void setObservableScrollViewListener(ObservableScrollViewListener listener) {
        observableScrollViewListener = listener;
    }

    private class ScrollStateHandler implements Runnable {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > SCROLL_STATE_CHECK_INTERVAL) {
                lastScrollUpdate = -1;
                if (observableScrollViewListener != null) {
                    observableScrollViewListener.onScrollStateChanged(ObservableScrollView.this, SCROLL_STATE_IDLE);
                }
            } else {
                postDelayed(this, SCROLL_STATE_CHECK_INTERVAL);
                if (observableScrollViewListener != null) {
                    observableScrollViewListener.onScrollStateChanged(ObservableScrollView.this, SCROLL_STATE_ACTIVE);
                }
            }
        }
    }
}
