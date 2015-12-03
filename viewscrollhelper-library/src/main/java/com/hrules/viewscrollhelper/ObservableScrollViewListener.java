package com.hrules.viewscrollhelper;

import android.widget.ScrollView;

public interface ObservableScrollViewListener {

  void onScrollStateChanged(ScrollView scrollView, int newScrollState);

  void onScrolled(ScrollView scrollView, int dx, int dy);

  void onScrollPositionChanged(float posx, float posy);

  void onScrollDown();

  void onScrollUp();
}
