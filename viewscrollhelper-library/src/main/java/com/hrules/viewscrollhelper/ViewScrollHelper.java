package com.hrules.viewscrollhelper;

import android.animation.TimeInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ScrollView;

public class ViewScrollHelper {
    private static final String TAG = "ViewScrollHelper";

    private static final float DEFAULT_HIDE_THRESHOLD = 10;
    private static final float DEFAULT_SHOW_THRESHOLD = 70;

    private float hideThreshold;
    private float showThreshold;

    private View view;
    private int viewHeight;
    private int viewHeightOld;
    private int viewOffset;
    private boolean viewVisible;
    private int viewScrolledDistance;

    private RecyclerView recyclerView;
    private ObservableScrollView observableScrollView;

    private boolean enabled;

    private TimeInterpolator showInterpolator;
    private TimeInterpolator hideInterpolator;

    private ViewScrollHelperListener listener;
    private final RecyclerView.OnScrollListener recyclerViewScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newScrollState) {
                    super.onScrollStateChanged(recyclerView, newScrollState);
                    if (enabled && newScrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        checkScrollStateChanged();
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (enabled) {
                        checkOnScrolled(dy);
                    }
                }
            };
    private final ObservableScrollViewListener observableScrollViewListener =
            new ObservableScrollViewListener() {
                @Override
                public void onScrollStateChanged(ScrollView scrollView, int newScrollState) {
                    if (enabled && newScrollState == ObservableScrollView.SCROLL_STATE_IDLE) {
                        checkScrollStateChanged();
                    }
                }

                @Override
                public void onScrolled(ScrollView scrollView, int dx, int dy) {
                    if (enabled) {
                        checkOnScrolled(dy);
                    }
                }

                @Override
                public void onScrollPositionChanged(float posx, float posy) {
                }

                @Override
                public void onScrollDown() {
                }

                @Override
                public void onScrollUp() {
                }
            };

    public ViewScrollHelper(RecyclerView recyclerView, View view) {
        init(view);

        this.recyclerView = recyclerView;
        this.recyclerView.setPadding(recyclerView.getPaddingLeft(),
                recyclerView.getPaddingTop() + viewHeight, recyclerView.getPaddingRight(),
                recyclerView.getPaddingBottom());
        this.recyclerView.setOnScrollListener(recyclerViewScrollListener);
    }

    public ViewScrollHelper(RecyclerView recyclerView, View view,
                            ViewScrollHelperListener viewScrollHelperListener) {
        this(recyclerView, view);
        setListener(viewScrollHelperListener);
    }

    public ViewScrollHelper(ObservableScrollView observableScrollView, View view) {
        init(view);

        this.observableScrollView = observableScrollView;
        this.observableScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        this.observableScrollView.setPadding(observableScrollView.getPaddingLeft(),
                observableScrollView.getPaddingTop() + viewHeight, observableScrollView.getPaddingRight(),
                observableScrollView.getPaddingBottom());
        this.observableScrollView.setObservableScrollViewListener(observableScrollViewListener);
    }

    public ViewScrollHelper(ObservableScrollView observableScrollView, View view,
                            ViewScrollHelperListener viewScrollHelperListener) {
        this(observableScrollView, view);
        setListener(viewScrollHelperListener);
    }

    private void init(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View must not be null");
        }

        this.recyclerView = null;
        this.observableScrollView = null;

        enabled = true;

        this.view = view;
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        viewHeight = this.view.getMeasuredHeight();
        viewHeightOld = viewHeight;
        viewOffset = 0;
        viewVisible = true;
        viewScrolledDistance = 0;

        hideThreshold = DEFAULT_HIDE_THRESHOLD;
        showThreshold = DEFAULT_SHOW_THRESHOLD;

        showInterpolator = new DecelerateInterpolator(2);
        hideInterpolator = new AccelerateInterpolator(2);
    }

    private void setListener(ViewScrollHelperListener listener) {
        this.listener = listener;
    }

    private void checkOnScrolled(int dy) {
        clipViewOffset();

        view.setTranslationY(-viewOffset);
        notifyOnMove(viewOffset);

        if ((viewOffset < viewHeight && dy > 0) || (viewOffset > 0 && dy < 0)) {
            viewOffset += dy;
        }
        if (viewScrolledDistance < 0) {
            viewScrolledDistance = 0;
        } else {
            viewScrolledDistance += dy;
        }
    }

    private void checkScrollStateChanged() {
        if (viewScrolledDistance < viewHeight && viewOffset >= 0) {
            internalShow();
        } else {
            if (viewVisible) {
                if (viewOffset > hideThreshold) {
                    internalHide();
                } else {
                    internalShow();
                }
            } else {
                if ((viewHeight - viewOffset) > showThreshold) {
                    internalShow();
                } else {
                    internalHide();
                }
            }
        }
    }

    private void clipViewOffset() {
        if (viewOffset > viewHeight) {
            viewOffset = viewHeight;
            notifyOnHide();
        } else if (viewOffset < 0) {
            viewOffset = 0;
            notifyOnShow();
        }
    }

    private void internalShow() {
        view.animate().translationY(0).setInterpolator(showInterpolator).start();
        notifyOnShow();
        viewOffset = 0;
        viewVisible = true;
    }

    private void internalHide() {
        if (viewOffset <= viewHeight) {
            view.animate().translationY(-viewHeight).setInterpolator(hideInterpolator).start();
            notifyOnHide();
            viewOffset = viewHeight;
        }
        viewVisible = false;
    }

    private void notifyOnHide() {
        if (listener != null) {
            listener.onHide();
        }
    }

    private void notifyOnShow() {
        if (listener != null) {
            listener.onShow();
        }
    }

    private void notifyOnMove(int distance) {
        if (listener != null) {
            listener.onMove(distance);
        }
    }

    public void show() {
        if (enabled && !viewVisible) {
            if (recyclerView != null) {
                recyclerView.smoothScrollBy(0, -viewHeight);
            }
            if (observableScrollView != null) {
                observableScrollView.smoothScrollBy(0, -viewHeight);
            }
        }
    }

    public void hide() {
        if (enabled && viewVisible) {
            if (recyclerView != null) {
                recyclerView.smoothScrollBy(0, viewHeight);
            }
            if (observableScrollView != null) {
                observableScrollView.smoothScrollBy(0, viewHeight);
            }
        }
    }

    public void setViewToHide(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int customViewHeight = view.getMeasuredHeight();
        if (customViewHeight > viewHeightOld) {
            throw new IllegalArgumentException("View height must be less than parent View height");
        }
        viewHeight = customViewHeight;
    }

    public void setViewToHideHeight(int height) {
        if (height > viewHeightOld) {
            throw new IllegalArgumentException("View height must be less than parent View height");
        }
        viewHeight = height;
    }

    public void setViewStickyHeight(int height) {
        if (height > viewHeightOld) {
            throw new IllegalArgumentException("View height must be less than parent View height");
        }
        viewHeight = viewHeightOld - height;
    }

    public void setViewSticky(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int customViewHeight = view.getMeasuredHeight();
        if (customViewHeight > viewHeightOld) {
            throw new IllegalArgumentException("View height must be less than parent View height");
        }
        viewHeight = viewHeightOld - customViewHeight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public float getHideThreshold() {
        return hideThreshold;
    }

    public void setHideThreshold(float hideThreshold) {
        this.hideThreshold = hideThreshold;
    }

    public float getShowThreshold() {
        return showThreshold;
    }

    public void setShowThreshold(float showThreshold) {
        this.showThreshold = showThreshold;
    }

    public TimeInterpolator getShowInterpolator() {
        return showInterpolator;
    }

    public void setShowInterpolator(TimeInterpolator showInterpolator) {
        this.showInterpolator = showInterpolator;
    }

    public TimeInterpolator getHideInterpolator() {
        return hideInterpolator;
    }

    public void setHideInterpolator(TimeInterpolator hideInterpolator) {
        this.hideInterpolator = hideInterpolator;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public ObservableScrollView getObservableScrollView() {
        return observableScrollView;
    }
}
