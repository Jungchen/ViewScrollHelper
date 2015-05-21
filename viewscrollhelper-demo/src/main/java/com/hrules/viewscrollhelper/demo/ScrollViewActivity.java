package com.hrules.viewscrollhelper.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.hrules.viewscrollhelper.ObservableScrollView;
import com.hrules.viewscrollhelper.OnViewScrollVisibilityChanged;
import com.hrules.viewscrollhelper.ViewScrollHelper;

public class ScrollViewActivity extends AppCompatActivity implements OnViewScrollVisibilityChanged {
    private static final String TAG = "RecyclerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ObservableScrollView observableScrollView = (ObservableScrollView) findViewById(R.id.observableScrollView);
        new ViewScrollHelper(observableScrollView, toolbar, this);
    }

    @Override
    public void onShow() {
        Log.e(TAG, "onShow");
    }

    @Override
    public void onHide() {
        Log.e(TAG, "onHide");
    }

    @Override
    public void onMove(int distance) {

    }
}