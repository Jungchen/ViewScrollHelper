package com.hrules.viewscrollhelper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_ACTIVE = 1;

    private static final int SCROLL_STATE_CHECK_INTERVAL = 50;
    private long lastScrollUpdate = -1;

    private OnObservableScrollViewChanged onObservableScrollViewChanged;

    public ObservableScrollView(Context context) {
        this(context, null);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (onObservableScrollViewChanged != null) {
            onObservableScrollViewChanged.onScrolled(this, -(oldX - x), -(oldY - y));
        }

        if (lastScrollUpdate == -1) {
            postDelayed(new ScrollStateHandler(), SCROLL_STATE_CHECK_INTERVAL);
        }
        lastScrollUpdate = System.currentTimeMillis();

    }

    public void setOnObservableScrollViewChangedListener(OnObservableScrollViewChanged listener) {
        onObservableScrollViewChanged = listener;
    }

    private class ScrollStateHandler implements Runnable {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > SCROLL_STATE_CHECK_INTERVAL) {
                lastScrollUpdate = -1;
                if (onObservableScrollViewChanged != null) {
                    onObservableScrollViewChanged.onScrollStateChanged(ObservableScrollView.this, SCROLL_STATE_IDLE);
                }
            } else {
                postDelayed(this, SCROLL_STATE_CHECK_INTERVAL);
                if (onObservableScrollViewChanged != null) {
                    onObservableScrollViewChanged.onScrollStateChanged(ObservableScrollView.this, SCROLL_STATE_ACTIVE);
                }
            }
        }
    }

}
