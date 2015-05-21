package com.hrules.viewscrollhelper;

import android.widget.ScrollView;

public interface OnObservableScrollViewChanged {

    void onScrollStateChanged(ScrollView scrollView, int newScrollState);

    void onScrolled(ScrollView scrollView, int dx, int dy);

}
